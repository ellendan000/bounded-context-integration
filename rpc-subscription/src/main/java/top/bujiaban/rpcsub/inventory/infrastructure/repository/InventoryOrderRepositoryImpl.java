package top.bujiaban.rpcsub.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.rpcsub.inventory.domain.InventoryOrder;
import top.bujiaban.rpcsub.inventory.domain.InventoryOrderRepository;

public interface InventoryOrderRepositoryImpl extends InventoryOrderRepository, JpaRepository<InventoryOrder, String> {
}
