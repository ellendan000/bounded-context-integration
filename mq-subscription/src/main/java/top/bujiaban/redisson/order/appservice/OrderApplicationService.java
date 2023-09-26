package top.bujiaban.rpc.order.appservice;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import top.bujiaban.rpc.order.domain.Order;
import top.bujiaban.rpc.order.domain.OrderRepository;
import top.bujiaban.rpc.order.domain.ProductStorage;
import top.bujiaban.rpc.order.infrastructure.client.InventoryFeignClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OrderApplicationService {
    private final InventoryFeignClient inventoryFeignClient;
    private final RedissonClient redissonClient;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final DomainEventPublisher domainEventPublisher;

    public OrderApplicationService(InventoryFeignClient inventoryFeignClient, RedissonClient redissonClient,
                                   OrderRepository orderRepository, OrderFactory orderFactory,
                                   DomainEventPublisher domainEventPublisher) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.redissonClient = redissonClient;
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
        this.domainEventPublisher = domainEventPublisher;
    }

    public Order createOrder(OrderCreateCommand orderCommand) {
        RAtomicLong count = redissonClient.getAtomicLong("product-" + orderCommand.getProductId());
        if(count.get() == 0) {
            this.initInventoryCount(orderCommand.getProductId());
        }
        count = redissonClient.getAtomicLong("product-" + orderCommand.getProductId());
        Long remainCount = count.addAndGet(orderCommand.getQuantity() * -1L);
        if(remainCount < 0) {
            count.getAndAdd(orderCommand.getQuantity());
            throw new RuntimeException("over quantity");
        }

        Order newOrder = orderFactory.createOrderFromCommand(orderCommand);
        try {
            newOrder = orderRepository.save(newOrder);
            domainEventPublisher.publish(newOrder.getEvent());
            return newOrder;
        } catch (Exception e){
            log.error(e.getMessage(), e);
            count.getAndAdd(orderCommand.getQuantity());
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
