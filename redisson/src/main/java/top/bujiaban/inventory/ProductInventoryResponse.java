package top.bujiaban.inventory;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductInventoryResponse {
    private String productId;
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private Integer count;
}
