package top.bujiaban.order;

import lombok.Builder;
import lombok.Data;
import top.bujiaban.order.domain.Order;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private String id;
    private String productId;
    private Integer quantity;
    private LocalDateTime createdTime;
    private Long remainStorageCount;

    public static OrderResponse fromEntity(Order order, Long remainStorageCount) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .createdTime(order.getCreatedTime())
                .remainStorageCount(remainStorageCount)
                .build();
    }
}
