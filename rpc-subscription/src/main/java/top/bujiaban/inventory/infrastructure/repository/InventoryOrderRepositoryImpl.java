package top.bujiaban.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.inventory.domain.InventoryOrder;
import top.bujiaban.inventory.domain.InventoryOrderRepository;

@Repository
public interface InventoryOrderRepositoryImpl extends InventoryOrderRepository, JpaRepository<InventoryOrder, String> {
}
