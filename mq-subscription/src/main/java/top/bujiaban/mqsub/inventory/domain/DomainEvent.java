package top.bujiaban.mqsub.inventory.domain;

import java.time.LocalDateTime;

public interface DomainEvent<ID> {
    ID getAggregationId();
    LocalDateTime getOccurrenceOn();
}
