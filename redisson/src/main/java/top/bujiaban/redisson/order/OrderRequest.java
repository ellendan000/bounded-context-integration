package top.bujiaban.redisson.order;

import lombok.Data;
import top.bujiaban.redisson.order.domain.Order;

@Data
public class OrderRequest {
    private String productId;
    private Integer quantity;

    public Order toEntity() {
        return Order.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
