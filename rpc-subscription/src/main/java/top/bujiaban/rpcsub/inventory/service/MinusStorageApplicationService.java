package top.bujiaban.rpcsub.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.inventory.domain.ProductStorage;
import top.bujiaban.common.inventory.domain.ProductStorageRepository;
import top.bujiaban.rpcsub.inventory.domain.*;
import top.bujiaban.rpcsub.inventory.infrastructure.client.OrderFeignClient;
import top.bujiaban.rpcsub.inventory.infrastructure.client.SubscribeOrderRequest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MinusStorageApplicationService {
    private final InventoryOrderRepository inventoryOrderRepository;
    private final ProductStorageRepository productStorageRepository;
    private final SubscribeOrderTaskRepository subscribeOrderTaskRepository;
    private final OrderFeignClient orderFeignClient;

    public MinusStorageApplicationService(InventoryOrderRepository inventoryOrderRepository,
                                          ProductStorageRepository productStorageRepository,
                                          SubscribeOrderTaskRepository subscribeOrderTaskRepository,
                                          OrderFeignClient orderFeignClient) {
        this.inventoryOrderRepository = inventoryOrderRepository;
        this.productStorageRepository = productStorageRepository;
        this.subscribeOrderTaskRepository = subscribeOrderTaskRepository;
        this.orderFeignClient = orderFeignClient;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ProductStorage minusStorageByOrders(String productId) {
        Optional<SubscribeOrderTask> subscribeOrderTaskOptional = subscribeOrderTaskRepository.findByProductId(productId);
        SubscribeOrderTask subscribeOrderTask = null;

        if(subscribeOrderTaskOptional.isEmpty()) {
            subscribeOrderTask = SubscribeOrderTask.builder()
                    .productId(productId)
                    .timestamp(Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli())
                    .currentPage(1)
                    .pageSize(100)
                    .build();
        } else {
            subscribeOrderTask = subscribeOrderTaskOptional.get();
        }

        List<InventoryOrder> orderList = orderFeignClient.subscribeNewOrders(SubscribeOrderRequest
                .fromEntity(subscribeOrderTask));
        inventoryOrderRepository.saveAll(orderList);

        int needMinusCount = orderList.stream()
                .mapToInt(InventoryOrder::getQuantity)
                .sum();
        Optional<ProductStorage> productStorage = productStorageRepository.findByProductId(productId);
        ProductStorage existsProductStorage = productStorage.orElseThrow(()-> {
            log.error("product storage data not exists, product id [{}]", productId);
            return new RuntimeException("product storage data not exists");
        });

        Integer oldQuantity = existsProductStorage.getQuantity();
        existsProductStorage.setQuantity(oldQuantity - needMinusCount);
        existsProductStorage = productStorageRepository.save(existsProductStorage);

        subscribeOrderTask.setCurrentPage(subscribeOrderTask.getCurrentPage() + 1);
        subscribeOrderTaskRepository.save(subscribeOrderTask);
        return existsProductStorage;
    }
}
