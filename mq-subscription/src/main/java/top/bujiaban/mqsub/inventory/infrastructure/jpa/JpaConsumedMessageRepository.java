package top.bujiaban.mqsub.inventory.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import top.bujiaban.mqsub.inventory.domain.ConsumedMessage;
import top.bujiaban.mqsub.order.domain.EventMessage;

public interface JpaConsumedMessageRepository extends JpaRepository<ConsumedMessage, Long> {
}
