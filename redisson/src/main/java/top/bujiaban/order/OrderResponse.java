package top.bujiaban.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    String orderId;
    String productId;
    Integer number;
    Long remainInventoryCount;
}
