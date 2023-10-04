package top.bujiaban.rpcsub.inventory.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.bujiaban.common.inventory.domain.ProductStorage;
import top.bujiaban.rpcsub.inventory.appservice.MinusStorageApplicationService;

@RestController
@RequestMapping("/minus-storage")
public class TriggerMinusStorageController {
    private final MinusStorageApplicationService minusStorageApplicationService;

    public TriggerMinusStorageController(MinusStorageApplicationService minusStorageApplicationService) {
        this.minusStorageApplicationService = minusStorageApplicationService;
    }

    @GetMapping
    public ProductStorage subscribeNewOrders(@RequestParam String productId) {
        return minusStorageApplicationService.minusStorageByOrders(productId);
    }
}
