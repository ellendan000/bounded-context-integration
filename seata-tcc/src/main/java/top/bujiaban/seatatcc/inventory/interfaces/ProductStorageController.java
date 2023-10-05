package top.bujiaban.seatatcc.inventory.interfaces;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.common.inventory.domain.ProductStorage;
import top.bujiaban.seatatcc.inventory.service.ProductStorageService;

@RestController
@RequestMapping("/product-storage")
public class ProductStorageController {
    private final ProductStorageService productStorageService;

    public ProductStorageController(ProductStorageService productStorageService) {
        this.productStorageService = productStorageService;
    }

    @GetMapping("/{productId}")
    ProductStorage fetchLatestCount(@PathVariable String productId) {
        return productStorageService.fetchLatest(productId);
    }

    @PutMapping("/{productId}")
    ProductStorage minusStorage(@RequestBody MinusProductStorageRequest minusStorageRequest){
        return productStorageService.minusStorageByNewOrder(minusStorageRequest.getProductId(),
                minusStorageRequest.getQuantity());
    }
}
