package top.bujiaban.rpc.order.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderCreatedEvent implements DomainEvent<String> {
    private String aggregationId;
    private LocalDateTime createdTime;
    private String productId;
    private Integer quantity;
}
