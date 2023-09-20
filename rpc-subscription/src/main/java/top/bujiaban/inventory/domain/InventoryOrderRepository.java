package top.bujiaban.inventory.domain;

import java.util.List;

public interface InventoryOrderRepository {
    void saveAll(List<InventoryOrder> orderList);
}
