package top.bujiaban.order;

import lombok.Data;
import top.bujiaban.order.appservice.OrderCreateCommand;

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
