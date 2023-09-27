package top.bujiaban.mqsub.order.appservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.bujiaban.mqsub.order.domain.DomainEvent;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;

import javax.persistence.Table;
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
        if (domainEvent instanceof EventMessage) {
            eventMessageRepository.save((EventMessage) domainEvent);
        }
        applicationEventPublisher.publishEvent(domainEvent);
    }

    @Async
    @EventListener
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void sendByMQ(EventMessage eventMessage) {
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
        }
        eventMessageRepository.save(eventMessage);
    }
}