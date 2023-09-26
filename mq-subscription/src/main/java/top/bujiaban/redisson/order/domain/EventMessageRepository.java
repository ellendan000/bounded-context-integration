package top.bujiaban.rpc.order.domain;

import java.util.Optional;

public interface EventMessageRepository {
    void save(EventMessage<?> message);
    Optional<EventMessage<?>> findById(Long id);
}