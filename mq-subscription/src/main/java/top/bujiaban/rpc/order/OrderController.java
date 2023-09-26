package top.bujiaban.rpc.order;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.rpc.order.appservice.OrderApplicationService;
import top.bujiaban.rpc.order.appservice.OrderCreateCommand;
import top.bujiaban.rpc.order.domain.Order;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        OrderCreateCommand orderCreateCommand = request.toCommand();
        Order order = this.orderApplicationService.createOrder(orderCreateCommand);
        return OrderResponse.fromEntity(order);
    }
}
