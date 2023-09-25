package top.bujiaban.inventory.domain;

public interface SubscribeOrderTaskRepository {
    SubscribeOrderTask save(SubscribeOrderTask subscribeOrderTask);
    SubscribeOrderTask findByProductId(String productId);
}
