package top.bujiaban.rpcsub.order.interfaces;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.common.order.interfaces.OrderResponse;
import top.bujiaban.rpcsub.order.service.OrderApplicationServiceImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscribe-orders")
public class SubscribeOrderController {
    private final OrderApplicationServiceImpl orderApplicationService;

    public SubscribeOrderController(OrderApplicationServiceImpl orderApplicationService) {
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
