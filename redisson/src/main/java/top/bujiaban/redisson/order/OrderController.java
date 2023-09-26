package top.bujiaban.redisson.order;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.redisson.order.appservice.OrderApplicationService;
import top.bujiaban.redisson.order.domain.Order;

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
}
