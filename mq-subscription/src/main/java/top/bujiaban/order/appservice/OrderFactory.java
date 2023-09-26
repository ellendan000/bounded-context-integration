package top.bujiaban.order.appservice;

import org.springframework.stereotype.Service;
import top.bujiaban.order.domain.EventMessage;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.OrderCreatedEvent;
import top.bujiaban.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderFactory {
    public Order createOrderFromCommand(OrderCreateCommand command) {
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
        newOrder.setEvent(eventMessage);
        return newOrder;
    }
}
