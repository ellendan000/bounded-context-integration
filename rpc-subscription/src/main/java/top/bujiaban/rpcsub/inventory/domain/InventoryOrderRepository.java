package top.bujiaban.rpcsub.inventory.domain;

import java.util.List;

public interface InventoryOrderRepository {
    void saveAll(List<InventoryOrder> inventoryOrderList);
}
