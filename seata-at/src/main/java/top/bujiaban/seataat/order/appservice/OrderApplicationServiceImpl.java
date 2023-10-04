package top.bujiaban.seataat.order.appservice;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.seataat.order.infrastructure.client.MinusProductStorageRequest;

import java.util.UUID;

@Slf4j
@Service
public class OrderApplicationServiceImpl {
//    private final InventoryFeignClient inventoryFeignClient;
//    private final OrderRepository orderRepository;
//
//    public OrderApplicationServiceImpl(InventoryFeignClient inventoryFeignClient,
//                                       OrderRepository orderRepository) {
//        this.inventoryFeignClient = inventoryFeignClient;
//        this.orderRepository = orderRepository;
//    }

//    @GlobalTransactional
//    @Transactional(rollbackFor = Exception.class)
//    public Order createOrder(Order order) {
//        Order newOrder = Order.builder()
//                .id(UUID.randomUUID().toString())
//                .productId(order.getProductId())
//                .quantity(order.getQuantity())
//                .orderStatus(OrderStatus.CREATED)
//                .build();
//        newOrder = orderRepository.save(newOrder);
//        inventoryFeignClient.minusStorage(order.getProductId(),
//                new MinusProductStorageRequest(order.getProductId(), order.getQuantity())
//        );
//        if (System.currentTimeMillis() % 3 == 0) {
//            log.error("Random rollback");
//            throw new RuntimeException("Random rollback order created");
//        }
//        return newOrder;
//    }
}
