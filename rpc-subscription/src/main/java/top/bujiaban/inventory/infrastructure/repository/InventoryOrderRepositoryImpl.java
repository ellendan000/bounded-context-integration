package top.bujiaban.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.inventory.domain.InventoryOrder;

public interface InventoryOrderRepositoryImpl extends JpaRepository<InventoryOrder, String> {
}
