package top.bujiaban.mqsub.order.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;
import top.bujiaban.mqsub.order.domain.Status;

import java.util.List;
import java.util.Set;

public interface EventMessageRepositoryImpl extends EventMessageRepository, JpaRepository<EventMessage, Long> {
    @Query("from EventMessage em where em.status in (:statusSet) order by em.createdTime asc")
    List<EventMessage> findByStatusAndOrderByCreatedTimeAsc(Set<Status> statusSet);
}
