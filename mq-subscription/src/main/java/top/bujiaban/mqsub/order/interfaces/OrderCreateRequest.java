package top.bujiaban.mqsub.order.interfaces;

import lombok.Data;
import top.bujiaban.mqsub.order.appservice.OrderCreateCommand;

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
