package top.bujiaban.seataat.order.service;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderRepository;
import top.bujiaban.common.order.domain.OrderStatus;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.common.order.infrastructure.client.MinusProductStorageRequest;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;

import java.util.UUID;

@Slf4j
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {
    private final InventoryFeignClient inventoryFeignClient;
    private final OrderRepository orderRepository;

    public OrderApplicationServiceImpl(InventoryFeignClient inventoryFeignClient,
                                       OrderRepository orderRepository) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.orderRepository = orderRepository;
    }

    @Override
    @GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(OrderCreateCommand command) {
        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .build();
        newOrder = orderRepository.save(newOrder);
        inventoryFeignClient.minusStorage(command.getProductId(),
                new MinusProductStorageRequest(command.getProductId(), command.getQuantity())
        );
        if (System.currentTimeMillis() % 3 == 0) {
            log.error("Random rollback");
            throw new RuntimeException("Random rollback order created");
        }
        return newOrder;
    }

    @Override
    public ProductStorage readStorage(String productId) {
        return inventoryFeignClient.fetchLatestCount(productId);
    }
}
