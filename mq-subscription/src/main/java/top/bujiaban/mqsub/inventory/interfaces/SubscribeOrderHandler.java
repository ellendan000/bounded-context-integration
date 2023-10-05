package top.bujiaban.mqsub.inventory.interfaces;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import top.bujiaban.mqsub.inventory.service.MinusStorageApplicationService;

@Slf4j
@Component
@RocketMQMessageListener(topic = "order_topic",
        consumerGroup = "inventory-service")
public class SubscribeOrderHandler implements RocketMQListener<EventMessage> {
    private final MinusStorageApplicationService minusStorageApplicationService;

    public SubscribeOrderHandler(MinusStorageApplicationService minusStorageApplicationService) {
        this.minusStorageApplicationService = minusStorageApplicationService;
    }

    @Override
    public void onMessage(EventMessage message) {
        log.info("Listening message id: {}", message.getId());
        minusStorageApplicationService.minusStorageByOrders(message);
    }
}
