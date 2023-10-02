package top.bujiaban.seataat.inventory.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.seataat.inventory.domain.ProductStorage;
import top.bujiaban.seataat.inventory.domain.ProductStorageRepository;

import java.util.Optional;

@Repository
public interface ProductStorageRepositoryImpl extends ProductStorageRepository, JpaRepository<ProductStorage, String> {
    Optional<ProductStorage> findByProductId(String productId);
}
