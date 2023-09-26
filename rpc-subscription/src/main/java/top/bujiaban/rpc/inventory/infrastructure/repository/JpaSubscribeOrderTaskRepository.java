package top.bujiaban.rpc.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.rpc.inventory.domain.SubscribeOrderTask;

public interface JpaSubscribeOrderTaskRepository extends JpaRepository<SubscribeOrderTask, String> {
    SubscribeOrderTask findByProductId(String productId);
}
