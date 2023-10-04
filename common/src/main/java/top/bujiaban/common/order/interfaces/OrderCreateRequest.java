package top.bujiaban.common.order.interfaces;

import lombok.Data;
import top.bujiaban.common.order.service.OrderCreateCommand;

@Data
public class OrderCreateRequest {
    private String productId;
    private Integer quantity;

    public OrderCreateCommand toCommand() {
        return OrderCreateCommand.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
