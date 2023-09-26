package top.bujiaban.rpc.order;

import lombok.Data;
import top.bujiaban.rpc.order.appservice.OrderCreateCommand;

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
