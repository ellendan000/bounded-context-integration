package top.bujiaban.rpc.inventory.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.bujiaban.rpc.inventory.domain.InventoryOrder;

import java.util.List;

@FeignClient(name = "orderClient", url = "127.0.0.1:8081")
public interface OrderFeignClient {

    @PostMapping("/subscribe-orders")
    List<InventoryOrder> subscribeNewOrders(@RequestBody SubscribeOrderRequest subscribeOrderRequest);
}
