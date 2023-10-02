package top.bujiaban.seataat.order.infrastructure.client;

import lombok.Data;

@Data
public class MinusProductStorageRequest {
    private final String productId;
    private final Integer quantity;
}
