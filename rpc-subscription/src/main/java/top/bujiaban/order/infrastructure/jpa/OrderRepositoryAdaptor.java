package top.bujiaban.order.infrastructure.jpa;

import org.springframework.stereotype.Repository;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.OrderRepository;

@Repository
public class OrderRepositoryAdaptor implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryAdaptor(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    public Order save(Order order) {
        return jpaOrderRepository.save(order);
    }
}
