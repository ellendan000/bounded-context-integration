package top.bujiaban.rpc.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.rpc.inventory.appservice.MinusStorageApplicationService;
import top.bujiaban.rpc.inventory.domain.ProductStorage;

@RestController
@RequestMapping("/minus-storage")
public class MinusStorageController {
    private MinusStorageApplicationService minusStorageApplicationService;

    public MinusStorageController(MinusStorageApplicationService minusStorageApplicationService) {
        this.minusStorageApplicationService = minusStorageApplicationService;
    }

    @GetMapping
    public ProductStorage subscribeNewOrders(@RequestParam String productId) {
        return minusStorageApplicationService.minusStorageByOrders(productId);
    }
}