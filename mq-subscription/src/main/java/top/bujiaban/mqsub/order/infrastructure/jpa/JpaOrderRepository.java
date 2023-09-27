package top.bujiaban.mqsub.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.mqsub.order.domain.Order;
import top.bujiaban.mqsub.order.domain.OrderRepository;

@Repository
public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, String> {
    Order save(Order order);
}
