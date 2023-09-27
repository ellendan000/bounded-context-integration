package top.bujiaban.mqsub.inventory.domain;

import java.util.Optional;

public interface ConsumedMessageRepository {
    void save(ConsumedMessage message);
    Optional<ConsumedMessage> findById(Long id);
}
