package bujiaban.order;

import lombok.Data;

@Data
public class OrderRequest {
    private String productId;
    private Integer number;
}
