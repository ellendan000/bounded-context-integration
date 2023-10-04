package top.bujiaban.rpc.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;

@Slf4j
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {
    private final InventoryFeignClient inventoryFeignClient;

    public OrderApplicationServiceImpl(InventoryFeignClient inventoryFeignClient) {
        this.inventoryFeignClient = inventoryFeignClient;
    }

    @Override
    public Order createOrder(OrderCreateCommand orderCommand) {
        throw new RuntimeException("No support method");
    }

    @Override
    public ProductStorage readStorage(String productId) {
        return inventoryFeignClient.fetchLatestCount(productId);
    }
}
