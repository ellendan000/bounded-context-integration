package top.bujiaban.seataat.inventory.interfaces;

import lombok.Data;

@Data
public class MinusProductStorageRequest {
    private String productId;
    private Integer quantity;
}
