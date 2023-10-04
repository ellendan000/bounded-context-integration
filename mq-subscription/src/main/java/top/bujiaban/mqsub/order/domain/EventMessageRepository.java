package top.bujiaban.mqsub.order.domain;

import java.util.Optional;

public interface EventMessageRepository {
    EventMessage save(EventMessage message);
    Optional<EventMessage> findById(Long id);
}
