package top.bujiaban.order.domain;

import java.time.LocalDateTime;

public interface DomainEvent<ID> {
    ID getAggregationId();
    LocalDateTime getCreatedTime();
}
