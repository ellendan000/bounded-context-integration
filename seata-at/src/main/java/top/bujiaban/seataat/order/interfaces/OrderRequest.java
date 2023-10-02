package top.bujiaban.seataat.order.interfaces;

import lombok.Data;
import top.bujiaban.seataat.order.domain.Order;

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
