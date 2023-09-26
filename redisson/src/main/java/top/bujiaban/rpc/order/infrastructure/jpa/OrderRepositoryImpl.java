package top.bujiaban.rpc.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.rpc.order.domain.Order;
import top.bujiaban.rpc.order.domain.OrderRepository;

@Repository
public interface OrderRepositoryImpl extends OrderRepository, JpaRepository<Order, String> {
    Order save(Order order);
}
