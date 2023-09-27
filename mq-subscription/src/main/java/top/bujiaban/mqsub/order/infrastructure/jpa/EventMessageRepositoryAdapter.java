package top.bujiaban.mqsub.order.infrastructure.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.bujiaban.mqsub.order.domain.EventMessage;
import top.bujiaban.mqsub.order.domain.EventMessageRepository;

import java.util.Optional;

@Slf4j
@Repository
public class EventMessageRepositoryAdapter implements EventMessageRepository {
    private final JpaEventMessageRepository jpaMessageRepository;

    public EventMessageRepositoryAdapter(JpaEventMessageRepository jpaMessageRepository) {
        this.jpaMessageRepository = jpaMessageRepository;
    }

    @Override
    public void save(EventMessage<?> message) {
        jpaMessageRepository.save(message);
    }

    @Override
    public Optional<EventMessage<?>> findById(Long id) {
        return jpaMessageRepository.findById(id);
    }
}
