package top.bujiaban.order.appservice;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Service;
import top.bujiaban.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.order.domain.Order;
import top.bujiaban.order.domain.OrderRepository;
import top.bujiaban.order.domain.ProductStorage;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderApplicationService {
    private InventoryFeignClient inventoryFeignClient;
    private RedissonClient redissonClient;
    private OrderRepository orderRepository;

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

        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .build();
        try {
            newOrder = orderRepository.save(newOrder);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(order.getQuantity());
            throw e;
        }
        RQueue<Order> newOrderCache = redissonClient.getQueue(redisCacheKey(newOrder.getProductId()));
        newOrderCache.add(newOrder);
        return newOrder;
    }

    public List<Order> getCacheOrders(String productId, Integer limit) {
        RQueue<Order> newOrderCache =  redissonClient.getQueue(redisCacheKey(productId));
        return newOrderCache.poll(limit);
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

    private String redisCacheKey(String productId) {
        return "order-" + productId + "-" + Instant.now().toEpochMilli();
    }
}
