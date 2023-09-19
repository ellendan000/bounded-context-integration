package bujiaban.inventory.domain;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private String productId;
    private Integer number;
}
