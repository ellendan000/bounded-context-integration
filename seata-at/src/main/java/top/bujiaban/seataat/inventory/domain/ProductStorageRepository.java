package top.bujiaban.seataat.inventory.domain;

import java.util.Optional;

public interface ProductStorageRepository {

    Optional<ProductStorage> findByProductId(String productId);

    ProductStorage save(ProductStorage productStorage);
}
