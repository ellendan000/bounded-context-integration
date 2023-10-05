package top.bujiaban.rpcsub.order.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.*;
import org.springframework.stereotype.Service;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderRepository;
import top.bujiaban.common.order.domain.OrderStatus;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {
    private final InventoryFeignClient inventoryFeignClient;
    private final RedissonClient redissonClient;
    private final OrderRepository orderRepository;

    public OrderApplicationServiceImpl(InventoryFeignClient inventoryFeignClient, RedissonClient redissonClient,
                                       OrderRepository orderRepository) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.redissonClient = redissonClient;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderCreateCommand command) {
        RAtomicLong count = redissonClient.getAtomicLong(cacheKeyForStorage(command.getProductId()));
        if(count.get() == 0) {
            this.initInventoryCount(command.getProductId());
        }
        count = redissonClient.getAtomicLong(cacheKeyForStorage(command.getProductId()));
        Long remainCount = count.addAndGet(command.getQuantity() * -1L);
        if(remainCount < 0) {
            count.getAndAdd(command.getQuantity());
            throw new RuntimeException("over quantity");
        }

        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .orderStatus(OrderStatus.CREATED)
                .build();
        try {
            newOrder = orderRepository.save(newOrder);
            RList<Order> newOrderCache = redissonClient.getList(
                    cacheKeyForOrder(newOrder.getProductId(), null));
            if(newOrderCache.isEmpty()){
                newOrderCache.expire(3, TimeUnit.DAYS);
            }
            newOrderCache.add(newOrder);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(command.getQuantity());
            throw e;
        }
        return newOrder;
    }

    @Override
    public ProductStorage readStorage(String productId) {
        RAtomicLong count = redissonClient.getAtomicLong(cacheKeyForStorage(productId));
        if(count.get() == 0) {
            this.initInventoryCount(productId);
        }
        count = redissonClient.getAtomicLong(cacheKeyForStorage(productId));
        return ProductStorage.builder()
                .productId(productId)
                .quantity((int)count.get())
                .build();
    }

    public List<Order> getCacheOrders(String productId, Long timestamp, Integer pageNo, Integer pageSize) {
        RList<Order> ordersCache =  redissonClient.getList(cacheKeyForOrder(productId, timestamp));

        Pair<Integer, Integer> fromToIndex = fromToIndex(pageNo, pageSize);
        return ordersCache.range(fromToIndex.getLeft(), fromToIndex.getRight());
    }

    private void initInventoryCount(String productId) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(productId);
        RLock writeLock = readWriteLock.writeLock();
        try {
            writeLock.tryLock(6, 5, TimeUnit.SECONDS);
            ProductStorage productInventory = inventoryFeignClient.fetchLatestCount(productId);
            RAtomicLong count = redissonClient.getAtomicLong(cacheKeyForStorage(productId));
            count.compareAndSet(0, productInventory.getQuantity());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }

    private String cacheKeyForStorage(String productId) {
        return "product-" + productId;
    }

    private String cacheKeyForOrder(String productId, Long timestamp) {
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
