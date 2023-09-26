package top.bujiaban.redisson.order.appservice;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import top.bujiaban.redisson.order.domain.Order;
import top.bujiaban.redisson.order.domain.OrderRepository;
import top.bujiaban.redisson.order.domain.ProductStorage;
import top.bujiaban.redisson.order.infrastructure.client.InventoryFeignClient;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderApplicationService {
    private final InventoryFeignClient inventoryFeignClient;
    private final RedissonClient redissonClient;
    private final OrderRepository orderRepository;

    public OrderApplicationService(InventoryFeignClient inventoryFeignClient, RedissonClient redissonClient,
                                   OrderRepository orderRepository) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.redissonClient = redissonClient;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        RAtomicLong count = redissonClient.getAtomicLong("product-" + order.getProductId());
        if(count.get() == 0) {
            this.initInventoryCount(order.getProductId());
        }
        count = redissonClient.getAtomicLong("product-" + order.getProductId());
        Long remainCount = count.addAndGet(order.getQuantity() * -1L);
        if(remainCount < 0) {
            count.getAndAdd(order.getQuantity());
            throw new RuntimeException("over quantity");
        }
        try {
            Order newOrder = Order.builder()
                    .id(UUID.randomUUID().toString())
                    .productId(order.getProductId())
                    .quantity(order.getQuantity())
                    .build();
            return orderRepository.save(newOrder);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(order.getQuantity());
            throw e;
        }
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
