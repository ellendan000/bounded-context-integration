package top.bujiaban.rpc.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.rpc.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.rpc.order.domain.ProductStorage;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private InventoryFeignClient inventoryFeignClient;

    public OrderController(InventoryFeignClient inventoryFeignClient) {
        this.inventoryFeignClient = inventoryFeignClient;
    }

    @GetMapping
    ProductStorage initInventoryCount(@RequestParam("productId") String productId) {
        return inventoryFeignClient.fetchLatestCount(productId);
    }
}