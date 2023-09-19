package bujiaban.inventory;

import bujiaban.inventory.client.OrderFeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-inventory")
public class ProductInventoryController {
    private OrderFeignClient orderFeignClient;

    public ProductInventoryController(OrderFeignClient orderFeignClient) {
        this.orderFeignClient = orderFeignClient;
    }

    @GetMapping("/{productId}")
    void subscribeNewOrder(@PathVariable String productId){
        orderFeignClient.fetchLatestCount(productId);
    }
}
