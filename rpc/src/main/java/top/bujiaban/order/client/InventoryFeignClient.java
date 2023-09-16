package top.bujiaban.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.bujiaban.order.domain.ProductInventory;

@FeignClient(name = "productInventoryClient", url = "127.0.0.1:8081")
public interface InventoryFeignClient {

    @GetMapping("/product-inventory/{productId}")
    ProductInventory fetchLatestCount(@PathVariable("productId") String productId);
}
