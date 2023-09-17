package top.bujiaban.order.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductInventory {
    private String productId;
    private LocalDateTime lastModifiedTime;
    private Integer count;
}
