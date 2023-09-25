package top.bujiaban.inventory.infrastructure.repository;

import org.springframework.stereotype.Repository;
import top.bujiaban.inventory.domain.InventoryOrder;
import top.bujiaban.inventory.domain.InventoryOrderRepository;

import java.util.List;

@Repository
public class InventoryOrderRepositoryAdaptor implements InventoryOrderRepository {
    private final InventoryOrderRepositoryImpl inventoryOrderRepositoryImpl;

    public InventoryOrderRepositoryAdaptor(InventoryOrderRepositoryImpl inventoryOrderRepositoryImpl) {
        this.inventoryOrderRepositoryImpl = inventoryOrderRepositoryImpl;
    }

    public void saveAll(List<InventoryOrder> inventoryOrderList) {
        inventoryOrderRepositoryImpl.saveAll(inventoryOrderList);
    }
}
