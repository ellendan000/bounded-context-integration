package top.bujiaban.rpcsub.inventory.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.rpcsub.inventory.domain.SubscribeOrderTask;
import top.bujiaban.rpcsub.inventory.domain.SubscribeOrderTaskRepository;

import java.util.Optional;

public interface SubscribeOrderTaskRepositoryImpl extends SubscribeOrderTaskRepository,
        JpaRepository<SubscribeOrderTask, String> {
    Optional<SubscribeOrderTask> findByProductId(String productId);
}
