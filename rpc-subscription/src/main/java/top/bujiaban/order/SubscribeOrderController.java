package top.bujiaban.order;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.order.appservice.OrderApplicationService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscribe-orders")
public class SubscribeOrderController {
    private OrderApplicationService orderApplicationService;

    public SubscribeOrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    List<OrderResponse> subscribeNewOrders(@RequestBody SubscribeOrderRequest subscribeOrderRequest) {
        return Objects.requireNonNull(
                orderApplicationService.getCacheOrders(subscribeOrderRequest.getProductId(),
                        subscribeOrderRequest.getTimestamp(),
                        subscribeOrderRequest.getPageNo(),
                        subscribeOrderRequest.getPageSize()))
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
