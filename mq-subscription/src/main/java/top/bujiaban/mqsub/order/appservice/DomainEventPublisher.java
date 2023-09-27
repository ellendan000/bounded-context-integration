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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.bujiaban.mqsub.common.ObjectMapperHolder;
import top.bujiaban.mqsub.order.domain.DomainEvent;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;

import javax.transaction.Transactional;

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

    @Transactional(rollbackOn = Throwable.class)
    public void publish(final DomainEvent domainEvent) {
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
    @EventListener
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendByMQ(EventMessage eventMessage) throws InterruptedException {
        Thread.sleep(5000);
        log.info("Ready to send event message: {}", eventMessage.getId());
        EventMessage existsEventMessage = eventMessageRepository.findById(eventMessage.getId()).orElseThrow(() -> {
            log.error("message data not exists, product id [{}]", eventMessage.getId());
            return new RuntimeException("message data not exists");
        });

        for (int i = 0; i < 3; i++) {
            SendResult sendResult = rocketMQTemplate.syncSend("order_topic", existsEventMessage);
            if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                existsEventMessage.setStatus(EventMessage.Status.PUBLISHED);
                break;
            } else if (i == 2) {
                existsEventMessage.setStatus(EventMessage.Status.PUBLISH_FAILED);
            }
            log.warn("Send message times {} failed", i + 1);
        }
        eventMessageRepository.save(existsEventMessage);
    }
}