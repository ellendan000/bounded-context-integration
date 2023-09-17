package bujiaban.order.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private String id;
    private String productId;
    private Integer number;
}
