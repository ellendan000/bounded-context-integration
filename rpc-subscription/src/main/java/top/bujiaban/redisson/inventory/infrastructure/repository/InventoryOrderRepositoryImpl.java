package top.bujiaban.rpc.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.rpc.inventory.domain.InventoryOrder;

public interface InventoryOrderRepositoryImpl extends JpaRepository<InventoryOrder, String> {
}
