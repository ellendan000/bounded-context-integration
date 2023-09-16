package top.bujiaban.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.order.client.InventoryFeignClient;
import top.bujiaban.order.domain.ProductInventory;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private InventoryFeignClient inventoryFeignClient;

    public OrderController(InventoryFeignClient inventoryFeignClient) {
        this.inventoryFeignClient = inventoryFeignClient;
    }

    @GetMapping
    ProductInventory initInventoryCount(@RequestParam("productId") String productId) {
        return inventoryFeignClient.fetchLatestCount(productId);
    }
}
