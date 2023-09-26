package top.bujiaban.rpcsub.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.rpcsub.inventory.domain.InventoryOrder;

public interface InventoryOrderRepositoryImpl extends JpaRepository<InventoryOrder, String> {
}
