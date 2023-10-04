package top.bujiaban.mqsub.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;

public interface EventMessageRepositoryImpl extends EventMessageRepository, JpaRepository<EventMessage, Long> {
}
