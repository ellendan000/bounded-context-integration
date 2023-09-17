package top.bujiaban.order;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;
import top.bujiaban.order.infrastructure.client.InventoryFeignClient;
import top.bujiaban.order.domain.ProductInventory;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private InventoryFeignClient inventoryFeignClient;
    private RedissonClient redissonClient;

    public OrderController(InventoryFeignClient inventoryFeignClient, RedissonClient redissonClient) {
        this.inventoryFeignClient = inventoryFeignClient;
        this.redissonClient = redissonClient;
    }

    @PostMapping
    OrderResponse createOrder(@RequestBody OrderRequest request) {
        RAtomicLong count = redissonClient.getAtomicLong("product-" + request.getProductId());
        if(count.get() == 0) {
            this.initInventoryCount(request.getProductId());
        }
        count = redissonClient.getAtomicLong("product-" + request.getProductId());
        Long remainCount = count.addAndGet(request.getNumber() * -1L);
        return OrderResponse.builder()
                .orderId(UUID.randomUUID().toString())
                .productId(request.getProductId())
                .number(request.getNumber())
                .remainInventoryCount(remainCount)
                .build();
    }

    private void initInventoryCount(String productId) {
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(productId);
        RLock writeLock = readWriteLock.writeLock();
        try {
            writeLock.tryLock(6, 5, TimeUnit.SECONDS);
            ProductInventory productInventory = inventoryFeignClient.fetchLatestCount(productId);
            RAtomicLong count = redissonClient.getAtomicLong("product-" + productId);
            count.compareAndSet(0, productInventory.getCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writeLock.unlock();
    }

}
