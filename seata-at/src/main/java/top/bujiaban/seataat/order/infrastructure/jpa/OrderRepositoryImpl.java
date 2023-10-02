package top.bujiaban.seataat.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.seataat.order.domain.Order;
import top.bujiaban.seataat.order.domain.OrderRepository;

@Repository
public interface OrderRepositoryImpl extends OrderRepository, JpaRepository<Order, String> {
    Order save(Order order);
}
