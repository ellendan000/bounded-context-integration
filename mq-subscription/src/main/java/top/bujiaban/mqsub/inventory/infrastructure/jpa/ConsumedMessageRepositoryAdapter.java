package top.bujiaban.mqsub.inventory.infrastructure.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import top.bujiaban.mqsub.inventory.domain.ConsumedMessage;
import top.bujiaban.mqsub.inventory.domain.ConsumedMessageRepository;

import java.util.Optional;

@Slf4j
@Repository
public class ConsumedMessageRepositoryAdapter implements ConsumedMessageRepository {
    private final JpaConsumedMessageRepository jpaMessageRepository;

    public ConsumedMessageRepositoryAdapter(JpaConsumedMessageRepository jpaMessageRepository) {
        this.jpaMessageRepository = jpaMessageRepository;
    }

    @Override
    public void save(ConsumedMessage message) {
        jpaMessageRepository.save(message);
    }

    @Override
    public Optional<ConsumedMessage> findById(Long id) {
        return jpaMessageRepository.findById(id);
    }
}
