package top.bujiaban.seatatcc.order.service;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.common.order.infrastructure.client.MinusProductStorageRequest;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;

@Slf4j
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {
    private final OrderCreateTccService orderCreateService;
    private final InventoryFeignClient inventoryFeignClient;

    public OrderApplicationServiceImpl(OrderCreateTccService orderCreateService,
                                       InventoryFeignClient inventoryFeignClient) {
        this.orderCreateService = orderCreateService;
        this.inventoryFeignClient = inventoryFeignClient;
    }

    @GlobalTransactional
    public Order createOrder(OrderCreateCommand command) {
        Order newOrder = orderCreateService.prepare(null, command);
        inventoryFeignClient.minusStorage(command.getProductId(),
                new MinusProductStorageRequest(newOrder.getProductId(), newOrder.getQuantity()));
        return newOrder;
    }

    @Override
    public ProductStorage readStorage(String productId) {
        return null;
    }
}
