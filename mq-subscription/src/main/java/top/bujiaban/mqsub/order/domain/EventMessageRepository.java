package top.bujiaban.mqsub.order.domain;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventMessageRepository {
    EventMessage save(EventMessage message);
    Optional<EventMessage> findById(Long id);
    List<EventMessage> findByStatusAndOrderByCreatedTimeAsc(Set<Status> statusSet);
}
