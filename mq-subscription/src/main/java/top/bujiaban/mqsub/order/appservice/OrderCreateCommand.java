package top.bujiaban.mqsub.order.appservice;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreateCommand {
    private String productId;
    private Integer quantity;
}
