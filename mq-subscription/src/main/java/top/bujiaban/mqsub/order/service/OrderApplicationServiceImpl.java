package top.bujiaban.mqsub.order.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderRepository;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;
import top.bujiaban.mqsub.order.domain.*;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderApplicationServiceImpl implements OrderApplicationService {
    private final InventoryFeignClient inventoryFeignClient;
    private final RedissonClient redissonClient;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final EventPublisher domainEventPublisher;

    public OrderApplicationServiceImpl(InventoryFeignClient inventoryFeignClient, RedissonClient redissonClient,
                                       OrderRepository orderRepository, OrderFactory orderFactory,
                                       EventPublisher domainEventPublisher) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.redissonClient = redissonClient;
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Order createOrder(OrderCreateCommand orderCommand) {
        RAtomicLong count = redissonClient.getAtomicLong(cacheKeyForStorage(orderCommand.getProductId()));
        if(count.get() == 0) {
            this.initInventoryCount(orderCommand.getProductId());
        }
        count = redissonClient.getAtomicLong(cacheKeyForStorage(orderCommand.getProductId()));
        Long remainCount = count.addAndGet(orderCommand.getQuantity() * -1L);
        if(remainCount < 0) {
            count.getAndAdd(orderCommand.getQuantity());
            throw new RuntimeException("over quantity");
        }

        Pair<Order, OrderCreatedEvent> newOrderPair = orderFactory.createOrderFromCommand(orderCommand);
        try {
            Order newOrder = orderRepository.save(newOrderPair.getLeft());
            domainEventPublisher.publish(newOrderPair.getRight());
            return newOrder;
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(orderCommand.getQuantity());
            throw e;
        }
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

}
