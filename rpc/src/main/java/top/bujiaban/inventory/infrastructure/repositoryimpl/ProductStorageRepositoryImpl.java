package top.bujiaban.inventory.infrastructure.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.inventory.domain.ProductStorage;
import top.bujiaban.inventory.domain.ProductStorageRepository;

import java.util.Optional;

@Repository
public interface ProductStorageRepositoryImpl extends ProductStorageRepository, JpaRepository<ProductStorage, String> {
    Optional<ProductStorage> findByProductId(String productId);
}
