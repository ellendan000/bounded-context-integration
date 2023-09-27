package top.bujiaban.mqsub.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.mqsub.order.domain.EventMessage;

public interface JpaEventMessageRepository extends JpaRepository<EventMessage<?>, Long> {
}
