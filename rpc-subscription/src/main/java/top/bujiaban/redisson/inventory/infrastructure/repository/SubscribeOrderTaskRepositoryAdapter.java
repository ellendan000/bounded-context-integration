package top.bujiaban.rpc.inventory.infrastructure.repository;

import org.springframework.stereotype.Repository;
import top.bujiaban.rpc.inventory.domain.SubscribeOrderTask;
import top.bujiaban.rpc.inventory.domain.SubscribeOrderTaskRepository;

@Repository
public class SubscribeOrderTaskRepositoryAdapter implements SubscribeOrderTaskRepository {
    private JpaSubscribeOrderTaskRepository jpaSubscribeOrderTaskRepository;

    public SubscribeOrderTaskRepositoryAdapter(JpaSubscribeOrderTaskRepository jpaSubscribeOrderTaskRepository) {
        this.jpaSubscribeOrderTaskRepository = jpaSubscribeOrderTaskRepository;
    }

    @Override
    public SubscribeOrderTask save(SubscribeOrderTask subscribeOrderTask) {
        return jpaSubscribeOrderTaskRepository.save(subscribeOrderTask);
    }

    @Override
    public SubscribeOrderTask findByProductId(String productId) {
        return jpaSubscribeOrderTaskRepository.findByProductId(productId);
    }
}
