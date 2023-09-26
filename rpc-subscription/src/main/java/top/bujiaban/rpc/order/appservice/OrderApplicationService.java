package top.bujiaban.rpc.order.appservice;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.*;
import org.springframework.stereotype.Service;
import top.bujiaban.rpc.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.rpc.order.domain.Order;
import top.bujiaban.rpc.order.domain.OrderRepository;
import top.bujiaban.rpc.order.domain.ProductStorage;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .build();
        try {
            newOrder = orderRepository.save(newOrder);
            RList<Order> newOrderCache = redissonClient.getList(
                    redisCacheKey(newOrder.getProductId(), null));
            if(newOrderCache.isEmpty()){
                newOrderCache.expire(3, TimeUnit.DAYS);
            }
            newOrderCache.add(newOrder);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(order.getQuantity());
            throw e;
        }
        return newOrder;
    }

    public List<Order> getCacheOrders(String productId, Long timestamp, Integer pageNo, Integer pageSize) {
        RList<Order> ordersCache =  redissonClient.getList(redisCacheKey(productId, timestamp));

        Pair<Integer, Integer> fromToIndex = fromToIndex(pageNo, pageSize);
        return ordersCache.range(fromToIndex.getLeft(), fromToIndex.getRight());
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

    private String redisCacheKey(String productId, Long timestamp) {
        if(timestamp == null) {
            timestamp = Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli();
        }
        return "order-" + productId + "-" + timestamp;
    }

    private Pair<Integer, Integer> fromToIndex(Integer pageNo, Integer pageSize) {
        Integer fromIndex = (pageNo - 1) * pageSize;
        Integer toIndex = pageNo * pageSize - 1;
        return Pair.of(fromIndex, toIndex);
    }

}
