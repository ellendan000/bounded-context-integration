package top.bujiaban.seataat.order.interfaces;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.seataat.order.appservice.OrderApplicationService;
import top.bujiaban.seataat.order.domain.Order;

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
