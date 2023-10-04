package top.bujiaban.common.order.infrastructure.client;

import lombok.Data;

@Data
public class MinusProductStorageRequest {
    private final String productId;
    private final Integer quantity;
}
