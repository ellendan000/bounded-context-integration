package top.bujiaban.seatatcc.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.inventory.domain.ProductStorage;
import top.bujiaban.common.inventory.domain.ProductStorageRepository;

@Slf4j
@Service
public class ProductStorageService {
    private final ProductStorageRepository productStorageRepository;
    private final MinusStorageTccService minusStorageTccService;

    public ProductStorageService(ProductStorageRepository productStorageRepository,
                                 MinusStorageTccService minusStorageTccService) {
        this.productStorageRepository = productStorageRepository;
        this.minusStorageTccService = minusStorageTccService;
    }

    public ProductStorage minusStorageByNewOrder(String productId, Integer quantity) {
        return minusStorageTccService.prepare(null, productId, quantity);
    }

    public ProductStorage fetchLatest(String productId) {
        return productStorageRepository.findByProductId(productId)
                .orElseGet(null);
    }
}
