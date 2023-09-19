package bujiaban.inventory.client;

import bujiaban.order.domain.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "orderClient", url = "127.0.0.1:8081")
public interface OrderFeignClient {

    @GetMapping("/orders")
    List<Order> fetchLatestCount(@RequestParam("productId") String productId);
}
