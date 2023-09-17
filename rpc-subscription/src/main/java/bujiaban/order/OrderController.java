package bujiaban.order;

import bujiaban.order.domain.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private RedisTemplate<String, Object> redisTemplate;

    public OrderController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping
    OrderResponse createOrder(@RequestBody OrderRequest request) {
        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(request.getProductId())
                .number(request.getNumber())
                .build();
        redisTemplate.opsForList().rightPush(this.redisCacheKey(request.getProductId()), newOrder);
        return OrderResponse.fromOrder(newOrder);
    }

    @GetMapping
    List<OrderResponse> subscribeNewOrders(@RequestParam String productId) {
        return Objects.requireNonNull(redisTemplate.opsForList()
                .range(redisCacheKey(productId), 0, 20))
            .stream()
            .map(order -> OrderResponse.fromOrder((Order) order))
            .collect(Collectors.toList());
    }

    private String redisCacheKey(String productId) {
        return "order-" + productId + "-" + Instant.now().toEpochMilli();
    }


}
