package top.bujiaban.order.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.OrderRepository;

@Repository
public interface OrderRepositoryImpl extends OrderRepository, JpaRepository<Order, String> {
    Order save(Order order);
}
