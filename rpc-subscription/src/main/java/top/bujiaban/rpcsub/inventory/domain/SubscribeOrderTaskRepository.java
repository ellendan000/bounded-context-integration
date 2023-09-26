package top.bujiaban.rpcsub.inventory.domain;

public interface SubscribeOrderTaskRepository {
    SubscribeOrderTask save(SubscribeOrderTask subscribeOrderTask);
    SubscribeOrderTask findByProductId(String productId);
}
