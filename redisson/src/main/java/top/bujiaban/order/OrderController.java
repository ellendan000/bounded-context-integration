package top.bujiaban.order;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;
import top.bujiaban.order.appservice.OrderApplicationService;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.OrderRepository;
import top.bujiaban.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.order.domain.ProductStorage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    OrderResponse createOrder(@RequestBody OrderRequest request) {
        Order order = request.toEntity();
        order = this.orderApplicationService.createOrder(order);
        return OrderResponse.fromEntity(order);
    }
}
