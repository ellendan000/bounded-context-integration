package top.bujiaban.rpc.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.rpc.inventory.domain.ProductStorage;
import top.bujiaban.rpc.inventory.domain.ProductStorageRepository;

@RestController
@RequestMapping("/product-storage")
public class ProductStorageController {
    private ProductStorageRepository productStorageRepository;

    public ProductStorageController(ProductStorageRepository productStorageRepository) {
        this.productStorageRepository = productStorageRepository;
    }

    @GetMapping("/{productId}")
    ProductStorage fetchLatestCount(@PathVariable String productId) {
        return productStorageRepository.findByProductId(productId)
                .orElseGet(null);
    }
}
