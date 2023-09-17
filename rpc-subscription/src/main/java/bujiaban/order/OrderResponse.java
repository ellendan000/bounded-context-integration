package bujiaban.order;

import bujiaban.order.domain.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    String orderId;
    String productId;
    Integer number;

    public static OrderResponse fromOrder(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .number(order.getNumber())
                .build();
    }
}
