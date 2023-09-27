package top.bujiaban.mqsub.order.domain;

import java.time.LocalDateTime;

public interface DomainEvent<ID> {
    ID getAggregationId();
    LocalDateTime getCreatedTime();
}
