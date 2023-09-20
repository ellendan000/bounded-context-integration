package top.bujiaban.inventory.domain;

import java.util.Optional;

public interface ProductStorageRepository {

    Optional<ProductStorage> findByProductId(String productId);
    ProductStorage saveAndFlush(ProductStorage productStorage);
}
