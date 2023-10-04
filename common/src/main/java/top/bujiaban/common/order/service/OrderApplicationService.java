package top.bujiaban.common.order.service;

import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.ProductStorage;

public interface OrderApplicationService {
    Order createOrder(OrderCreateCommand orderCommand);

    ProductStorage readStorage(String productId);
}
