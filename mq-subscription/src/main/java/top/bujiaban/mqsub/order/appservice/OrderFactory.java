package top.bujiaban.mqsub.order.appservice;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderStatus;
import top.bujiaban.common.order.service.OrderCreateCommand;
import top.bujiaban.mqsub.order.domain.OrderCreatedEvent;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderFactory {
    public Pair<Order, OrderCreatedEvent> createOrderFromCommand(OrderCreateCommand command) {
        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .createdTime(LocalDateTime.now())
                .build();

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .aggregationId(newOrder.getId())
                .occurrenceOn(newOrder.getCreatedTime())
                .productId(newOrder.getProductId())
                .quantity(newOrder.getQuantity())
                .build();

        return Pair.of(newOrder, orderCreatedEvent);
    }
}
