package top.bujiaban.mqsub.inventory.appservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.mqsub.inventory.domain.*;
import top.bujiaban.mqsub.inventory.interfaces.EventMessage;
import java.util.Optional;

@Slf4j
@Service
public class MinusStorageApplicationService {
    private final ProductStorageRepository productStorageRepository;
    private final ConsumedMessageRepository consumedMessageRepository;
    private final ConsumedMessageFactory consumedMessageFactory;

    public MinusStorageApplicationService(ProductStorageRepository productStorageRepository,
                                          ConsumedMessageRepository consumedMessageRepository,
                                          ConsumedMessageFactory consumedMessageFactory) {
        this.productStorageRepository = productStorageRepository;
        this.consumedMessageRepository = consumedMessageRepository;
        this.consumedMessageFactory = consumedMessageFactory;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void minusStorageByOrders(EventMessage eventMessage) {
        if (consumedMessageRepository.findById(eventMessage.getId()).isPresent()) {
            log.info("Message duplicated sent");
            return;
        }
        Pair<ConsumedMessage, DomainEvent> paired = consumedMessageFactory
                .createConsumedMessage(eventMessage);
        for (int i = 0; i < 3; i++) {
            try {
                OrderCreatedEvent orderCreatedEvent = (OrderCreatedEvent) paired.getRight();
                Optional<ProductStorage> productStorage = productStorageRepository
                        .findByProductId(orderCreatedEvent.getProductId());
                ProductStorage existsProductStorage = productStorage.orElseThrow(() -> {
                    log.error("product storage data not exists, product id [{}]", orderCreatedEvent.getProductId());
                    return new RuntimeException("product storage data not exists");
                });

                Integer oldQuantity = existsProductStorage.getQuantity();
                existsProductStorage.setQuantity(oldQuantity - orderCreatedEvent.getQuantity());
                productStorageRepository.saveAndFlush(existsProductStorage);
                consumedMessageRepository.save(paired.getLeft());
                break;
            } catch (Exception e) {
                log.info(e.getMessage(), e);
                if (i == 2) {
                    throw e;
                }
                log.info("Consume OrderCreatedEvent failed, id:{}, will retry index: {}", eventMessage.getId(),
                        i + 1);
            }
        }
    }
}
