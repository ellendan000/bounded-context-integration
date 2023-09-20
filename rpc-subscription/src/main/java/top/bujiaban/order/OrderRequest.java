package top.bujiaban.order;

import lombok.Data;
import top.bujiaban.order.domain.Order;

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
