package top.bujiaban.rpcsub.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.rpcsub.order.domain.Order;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, String> {
}
