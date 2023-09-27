package top.bujiaban.mqsub.order.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductStorage {
    private String productId;
    private LocalDateTime lastModifiedTime;
    private Integer quantity;
}
