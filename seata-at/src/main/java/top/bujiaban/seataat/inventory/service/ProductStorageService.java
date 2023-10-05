package top.bujiaban.seataat.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.inventory.domain.ProductStorage;
import top.bujiaban.common.inventory.domain.ProductStorageRepository;

@Slf4j
@Service
public class ProductStorageService {
    private final ProductStorageRepository productStorageRepository;

    public ProductStorageService(ProductStorageRepository productStorageRepository) {
        this.productStorageRepository = productStorageRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductStorage minusStorageByNewOrder(String productId, Integer quantity) {
        ProductStorage productStorage = productStorageRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("product storage data not exists, product id [{}]", productId);
                    return new RuntimeException("product storage data not exists");
                });
        productStorage = productStorage.minusStorage(quantity);
        productStorageRepository.save(productStorage);
        return productStorage;
    }

    public ProductStorage fetchLatest(String productId) {
        return productStorageRepository.findByProductId(productId)
                .orElseGet(null);
    }
}
