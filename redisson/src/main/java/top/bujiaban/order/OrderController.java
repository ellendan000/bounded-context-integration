package top.bujiaban.order;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.OrderRepository;
import top.bujiaban.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.order.domain.ProductStorage;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private InventoryFeignClient inventoryFeignClient;
    private RedissonClient redissonClient;
    private OrderRepository orderRepository;

    public OrderController(InventoryFeignClient inventoryFeignClient,
                           OrderRepository orderRepository,
                           RedissonClient redissonClient) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.orderRepository = orderRepository;
        this.redissonClient = redissonClient;
    }

    @PostMapping
    OrderResponse createOrder(@RequestBody OrderRequest request) {
        RAtomicLong count = redissonClient.getAtomicLong("product-" + request.getProductId());
        if(count.get() == 0) {
            this.initInventoryCount(request.getProductId());
        }
        count = redissonClient.getAtomicLong("product-" + request.getProductId());
        Long remainCount = count.addAndGet(request.getQuantity() * -1L);
        if(remainCount < 0) {
            count.getAndAdd(request.getQuantity());
            throw new RuntimeException("over quantity");
        }
        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();
        newOrder = orderRepository.save(newOrder);
        return OrderResponse.fromEntity(newOrder, remainCount);
    }

    private void initInventoryCount(String productId) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(productId);
        RLock writeLock = readWriteLock.writeLock();
        try {
            writeLock.tryLock(6, 5, TimeUnit.SECONDS);
            ProductStorage productInventory = inventoryFeignClient.fetchLatestCount(productId);
            RAtomicLong count = redissonClient.getAtomicLong("product-" + productId);
            count.compareAndSet(0, productInventory.getQuantity());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }

}
