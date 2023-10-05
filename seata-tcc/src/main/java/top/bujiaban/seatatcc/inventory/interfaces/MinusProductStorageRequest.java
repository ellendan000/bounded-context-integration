package top.bujiaban.seatatcc.inventory.interfaces;

import lombok.Data;

@Data
public class MinusProductStorageRequest {
//    private String orderId;
    private final String productId;
    private final Integer quantity;
}
