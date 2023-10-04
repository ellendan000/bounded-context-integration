package top.bujiaban.common.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderRepository;

public interface OrderRepositoryImpl extends OrderRepository, JpaRepository<Order, String> {
}