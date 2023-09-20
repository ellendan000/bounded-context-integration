package top.bujiaban.order;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.order.appservice.OrderApplicationService;
import top.bujiaban.order.domain.Order;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    OrderResponse createOrder(@RequestBody OrderRequest request) {
        Order order = request.toEntity();
        order = this.orderApplicationService.createOrder(order);
        return OrderResponse.fromEntity(order);
    }

    @GetMapping
    List<OrderResponse> subscribeNewOrders(@RequestParam String productId, @RequestParam Integer limit) {
        return Objects.requireNonNull(
                orderApplicationService.getCacheOrders(productId, limit))
                .stream()
                .map(OrderResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
