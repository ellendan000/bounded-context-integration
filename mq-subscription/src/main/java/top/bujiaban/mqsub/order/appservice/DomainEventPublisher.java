package top.bujiaban.mqsub.order.appservice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.mqsub.common.ObjectMapperHolder;
import top.bujiaban.mqsub.order.domain.DomainEvent;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;

import java.util.Objects;

@Slf4j
@Component
public class DomainEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EventMessageRepository eventMessageRepository;
    private final RocketMQTemplate rocketMQTemplate;

    public DomainEventPublisher(ApplicationEventPublisher applicationEventPublisher,
                                EventMessageRepository eventMessageRepository, RocketMQTemplate rocketMQTemplate) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.eventMessageRepository = eventMessageRepository;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(final DomainEvent domainEvent) {
        log.info("publish event thread id: {}", Thread.currentThread().getName());
        if (domainEvent.needRemoteSent()) {
            EventMessage eventMessage = EventMessage.builder()
                    .aggregationId(domainEvent.getAggregationId().toString())
                    .eventName(domainEvent.getClass().getSimpleName())
                    .data(ObjectMapperHolder.objectToJsonNode(domainEvent))
                    .status(EventMessage.Status.CREATED)
                    .build();
            eventMessageRepository.save(eventMessage);
            applicationEventPublisher.publishEvent(eventMessage);
        }else{
            applicationEventPublisher.publishEvent(domainEvent);
        }
    }

    @Async
    @Retryable(include = RetryException.class, exclude = IllegalArgumentException.class,
            maxAttempts = 3, backoff = @Backoff(delay = 1000L, maxDelay = 2000L))
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendByMQ(EventMessage eventMessage) throws InterruptedException {
        log.info("EventListener thread id: {}", Thread.currentThread().getName());
        log.info("Ready to send event message: {}", eventMessage.getId());

        EventMessage existsEventMessage = eventMessageRepository.findById(eventMessage.getId()).orElseThrow(() -> {
            log.error("message data not exists, product id [{}]", eventMessage.getAggregationId());
            throw new RuntimeException("message data not exists");
        });

        SendResult sendResult = rocketMQTemplate.syncSend("order_topic", existsEventMessage);
        if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
            existsEventMessage.setStatus(EventMessage.Status.PUBLISHED);
            eventMessageRepository.save(existsEventMessage);
        } else {
            throw new RuntimeException("mq send failed");
        }
    }

    @Recover
    public void setFailedStatus(RetryException e, EventMessage eventMessage) {
        log.info("Retry recover invoke: {}", eventMessage.getId());
        Throwable sourceException = e.getRootCause();
        if(Objects.nonNull(sourceException) && sourceException
                .getMessage().equals("message data not exists")){
           return;
        }
        EventMessage existsEventMessage = eventMessageRepository.findById(eventMessage.getId()).get();
        existsEventMessage.setStatus(EventMessage.Status.PUBLISH_FAILED);
        eventMessageRepository.save(existsEventMessage);
    }

}