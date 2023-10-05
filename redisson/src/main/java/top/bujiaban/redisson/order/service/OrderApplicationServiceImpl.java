package top.bujiaban.redisson.order.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderRepository;
import top.bujiaban.common.order.domain.OrderStatus;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;

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

    @Override
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
        try {
            Order newOrder = Order.builder()
                    .id(UUID.randomUUID().toString())
                    .productId(command.getProductId())
                    .quantity(command.getQuantity())
                    .orderStatus(OrderStatus.CREATED)
                    .build();
            return orderRepository.save(newOrder);
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(command.getQuantity());
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
