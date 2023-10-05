package top.bujiaban.common.order.domain;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    void deleteById(String id);
    Optional<Order> findById(String id);
}
