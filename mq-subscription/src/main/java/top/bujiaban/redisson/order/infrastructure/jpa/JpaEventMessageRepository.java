package top.bujiaban.rpc.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.rpc.order.domain.EventMessage;

public interface JpaEventMessageRepository extends JpaRepository<EventMessage<?>, Long> {
}
