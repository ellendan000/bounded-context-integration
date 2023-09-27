package top.bujiaban.mqsub.inventory.interfaces;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.bujiaban.mqsub.inventory.domain.ConsumedMessage;
import top.bujiaban.mqsub.inventory.domain.DomainEvent;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventMessage {
    private Long id;
    private String aggregationId;
    private String eventName;
    private JsonNode data;
}
