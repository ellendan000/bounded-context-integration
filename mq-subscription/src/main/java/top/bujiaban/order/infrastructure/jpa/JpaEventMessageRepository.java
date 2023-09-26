package top.bujiaban.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.order.domain.EventMessage;

public interface JpaEventMessageRepository extends JpaRepository<EventMessage<?>, Long> {
}
