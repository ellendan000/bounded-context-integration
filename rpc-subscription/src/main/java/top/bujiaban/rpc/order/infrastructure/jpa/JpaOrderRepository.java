package top.bujiaban.rpc.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.bujiaban.rpc.order.domain.Order;

@Repository
public interface JpaOrderRepository extends JpaRepository<Order, String> {
}
