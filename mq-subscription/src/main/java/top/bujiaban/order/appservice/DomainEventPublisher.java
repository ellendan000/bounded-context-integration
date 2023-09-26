package top.bujiaban.order.appservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.bujiaban.order.domain.DomainEvent;
import top.bujiaban.order.domain.EventMessage;
import top.bujiaban.order.domain.EventMessageRepository;

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

    @Transactional
    public void publish(final DomainEvent domainEvent) {
        if (domainEvent instanceof EventMessage) {
            eventMessageRepository.save((EventMessage) domainEvent);
        }
        applicationEventPublisher.publishEvent(domainEvent);
    }

    @Async
    @EventListener
    public void sendByMQ(EventMessage eventMessage) {
        log.info("Ready to send event message: {}", eventMessage.getId());
        eventMessageRepository.findById(eventMessage.getId()).orElseThrow(() -> {
            log.error("message data not exists, product id [{}]", eventMessage.getId());
            return new RuntimeException("message data not exists");
        });

        for (int i = 0; i < 3; i++) {
            SendResult sendResult = rocketMQTemplate.syncSend("order_topic", eventMessage);
            if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                eventMessage.setStatus(EventMessage.Status.PUBLISHED);
                break;
            } else if (i == 2) {
                eventMessage.setStatus(EventMessage.Status.PUBLISH_FAILED);
            }
        }
    }
}