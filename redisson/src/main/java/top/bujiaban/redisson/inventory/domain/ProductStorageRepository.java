package top.bujiaban.redisson.inventory.domain;

import java.util.Optional;

public interface ProductStorageRepository {

    Optional<ProductStorage> findByProductId(String productId);
}
