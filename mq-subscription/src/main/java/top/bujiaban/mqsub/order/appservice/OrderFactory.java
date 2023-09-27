package top.bujiaban.mqsub.order.appservice;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.OrderCreatedEvent;
import top.bujiaban.mqsub.order.domain.OrderStatus;
import top.bujiaban.mqsub.order.domain.Order;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderFactory {
    public Pair<Order, EventMessage<OrderCreatedEvent>> createOrderFromCommand(OrderCreateCommand command) {
        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .createdTime(LocalDateTime.now())
                .build();

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .aggregationId(newOrder.getId())
                .createdTime(newOrder.getCreatedTime())
                .productId(newOrder.getProductId())
                .quantity(newOrder.getQuantity())
                .build();

        EventMessage<OrderCreatedEvent> eventMessage = EventMessage.<OrderCreatedEvent>builder()
                .aggregationId(newOrder.getId())
                .content(orderCreatedEvent)
                .status(EventMessage.Status.CREATED)
                .build();
        return Pair.of(newOrder, eventMessage);
    }
}
