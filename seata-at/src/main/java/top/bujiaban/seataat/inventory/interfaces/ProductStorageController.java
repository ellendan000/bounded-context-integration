package top.bujiaban.seataat.inventory.interfaces;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.seataat.inventory.appservice.ProductStorageService;
import top.bujiaban.seataat.inventory.domain.ProductStorage;

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
