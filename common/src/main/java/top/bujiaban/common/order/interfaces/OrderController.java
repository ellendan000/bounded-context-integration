package top.bujiaban.common.order.interfaces;

import org.springframework.web.bind.annotation.*;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.ProductStorage;
import top.bujiaban.common.order.service.OrderApplicationService;
import top.bujiaban.common.order.service.OrderCreateCommand;

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

    @GetMapping
    ProductStorage initInventoryCount(@RequestParam("productId") String productId) {
        return orderApplicationService.readStorage(productId);
    }
}
