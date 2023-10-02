package top.bujiaban.mqsub.order.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderCreatedEvent implements DomainEvent<String> {
    private String aggregationId;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime occurrenceOn;
    private String productId;
    private Integer quantity;
    public Boolean needRemoteSent(){
        return true;
    }
}
