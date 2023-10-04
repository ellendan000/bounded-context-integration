package top.bujiaban.common.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.bujiaban.common.order.domain.ProductStorage;

@FeignClient(name = "inventoryClient", url = "127.0.0.1:8081")
public interface InventoryFeignClient {

    @GetMapping("/product-storage/{productId}")
    ProductStorage fetchLatestCount(@PathVariable("productId") String productId);

    @PutMapping("/product-storage/{productId}")
    ProductStorage minusStorage(@PathVariable("productId") String productId,
                                @RequestBody MinusProductStorageRequest minusProductStorageRequest);
}
