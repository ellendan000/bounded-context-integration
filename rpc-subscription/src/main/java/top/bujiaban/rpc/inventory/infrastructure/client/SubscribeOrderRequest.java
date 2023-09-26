package top.bujiaban.rpc.inventory.infrastructure.client;

import lombok.Builder;
import lombok.Data;
import top.bujiaban.rpc.inventory.domain.SubscribeOrderTask;

@Data
@Builder
public class SubscribeOrderRequest {
    private String productId;
    private Long timestamp;
    private Integer pageNo;
    private Integer pageSize;

    public static SubscribeOrderRequest fromEntity(SubscribeOrderTask subscribeOrderTask) {
        return SubscribeOrderRequest.builder()
                .productId(subscribeOrderTask.getProductId())
                .timestamp(subscribeOrderTask.getTimestamp())
                .pageNo(subscribeOrderTask.getCurrentPage())
                .pageSize(subscribeOrderTask.getPageSize())
                .build();
    }
}
