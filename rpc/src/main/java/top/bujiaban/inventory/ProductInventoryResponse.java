package top.bujiaban.inventory;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductInventoryResponse {
    String productId;
    LocalDateTime createdTime;
    LocalDateTime lastModifiedTime;
    Integer count;
}
