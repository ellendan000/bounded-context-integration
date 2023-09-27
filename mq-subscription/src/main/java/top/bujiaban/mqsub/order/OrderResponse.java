package top.bujiaban.mqsub.order;

import lombok.Builder;
import lombok.Data;
import top.bujiaban.mqsub.order.domain.Order;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String productId;
    private Integer quantity;
    private LocalDateTime createdTime;

    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .createdTime(order.getCreatedTime())
                .build();
    }
}
