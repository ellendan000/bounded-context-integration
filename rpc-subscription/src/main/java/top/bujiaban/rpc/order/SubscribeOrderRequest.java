package top.bujiaban.rpc.order;

import lombok.Data;

@Data
public class SubscribeOrderRequest {
    private String productId;
    private Long timestamp;
    private Integer pageNo;
    private Integer pageSize;
}
