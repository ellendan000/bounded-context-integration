package top.bujiaban.redisson.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.redisson.inventory.domain.ProductStorage;
import top.bujiaban.redisson.inventory.domain.ProductStorageRepository;

import java.util.Optional;

@Repository
public interface ProductStorageRepositoryImpl extends ProductStorageRepository, JpaRepository<ProductStorage, String> {
    Optional<ProductStorage> findByProductId(String productId);
}
