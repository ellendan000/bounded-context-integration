package top.bujiaban.mqsub.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import top.bujiaban.mqsub.common.ObjectMapperHolder;
import top.bujiaban.mqsub.inventory.domain.ConsumedMessage;
import top.bujiaban.mqsub.inventory.domain.DomainEvent;
import top.bujiaban.mqsub.inventory.interfaces.EventMessage;

@Slf4j
@Service
public class ConsumedMessageFactory {
    public Pair<ConsumedMessage, DomainEvent> createConsumedMessage(EventMessage eventMessage){
        String eventName = eventMessage.getEventName();
        Class clazzForDomainEvent = getDomainEventClass(eventMessage.getEventName());
        if(clazzForDomainEvent == null){
            log.info("message eventName doesn't support, {}", eventName);
            throw new RuntimeException("message eventName doesn't support");
        }

        try {
            DomainEvent domainEvent = (DomainEvent) ObjectMapperHolder.jsonNodeToObject(eventMessage.getData(),
                    clazzForDomainEvent);

            ConsumedMessage consumedMessage = ConsumedMessage.builder()
                    .id(eventMessage.getId())
                    .aggregationId(eventMessage.getAggregationId())
                    .eventName(eventMessage.getEventName())
                    .build();
            return Pair.of(consumedMessage, domainEvent);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            throw new RuntimeException("json str convert failed");
        }
    }

    private Class getDomainEventClass(String eventName) {
        String packageName = this.getClass().getPackageName();
        String targetPackagePrefix = packageName.substring(0, packageName.lastIndexOf('.')) + ".domain.";
        try {
            return Class.forName(targetPackagePrefix + eventName);
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage(), e);
            return null;
        }
    }
}
