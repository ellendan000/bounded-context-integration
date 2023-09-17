package top.bujiaban.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/product-inventory")
public class ProductInventoryController {

    @GetMapping("/{productId}")
    ProductInventoryResponse fetchLatestCount(@PathVariable String productId) {
        return ProductInventoryResponse.builder()
                .productId(productId)
                .createdTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .count(100)
                .build();
    }
}
