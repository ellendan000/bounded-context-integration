package top.bujiaban.inventory.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.bujiaban.inventory.domain.InventoryOrder;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.ProductStorage;

import java.util.List;

@FeignClient(name = "orderClient", url = "127.0.0.1:8081")
public interface OrderFeignClient {

    @PostMapping("/subscribe-orders")
    List<InventoryOrder> subscribeNewOrders(@RequestBody SubscribeOrderRequest subscribeOrderRequest);
}
