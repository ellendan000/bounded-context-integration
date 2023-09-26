package top.bujiaban.rpc.inventory.domain;

public interface SubscribeOrderTaskRepository {
    SubscribeOrderTask save(SubscribeOrderTask subscribeOrderTask);
    SubscribeOrderTask findByProductId(String productId);
}
