package top.bujiaban.rpcsub.inventory.domain;

import java.util.Optional;

public interface SubscribeOrderTaskRepository {
    SubscribeOrderTask save(SubscribeOrderTask subscribeOrderTask);
    Optional<SubscribeOrderTask> findByProductId(String productId);
}
