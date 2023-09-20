package top.bujiaban.inventory.appservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.inventory.domain.InventoryOrder;
import top.bujiaban.inventory.domain.InventoryOrderRepository;
import top.bujiaban.inventory.domain.ProductStorage;
import top.bujiaban.inventory.domain.ProductStorageRepository;
import top.bujiaban.inventory.infrastructure.client.OrderFeignClient;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MinusStorageApplicationService {
    private InventoryOrderRepository inventoryOrderRepository;
    private ProductStorageRepository productStorageRepository;
    private OrderFeignClient orderFeignClient;

    public MinusStorageApplicationService(InventoryOrderRepository inventoryOrderRepository,
                                          ProductStorageRepository productStorageRepository,
                                          OrderFeignClient orderFeignClient) {
        this.inventoryOrderRepository = inventoryOrderRepository;
        this.productStorageRepository = productStorageRepository;
        this.orderFeignClient = orderFeignClient;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ProductStorage minusStorageByOrders(String productId) {
        List<InventoryOrder> orderList = orderFeignClient.subscribeNewOrders(productId, 20);
        inventoryOrderRepository.saveAll(orderList);

        int needMinusCount = orderList.stream()
                .mapToInt(InventoryOrder::getQuantity)
                .sum();
        Optional<ProductStorage> productStorage = productStorageRepository.findByProductId(productId);
        ProductStorage existsProductStorage = productStorage.orElseThrow(()-> {
            log.error("product storage data not exists, product id [{}]", productId);
            return new RuntimeException("product storage data not exists");
        });

        Integer oldQuantity = existsProductStorage.getQuantity();
        existsProductStorage.setQuantity(oldQuantity - needMinusCount);
        existsProductStorage = productStorageRepository.saveAndFlush(existsProductStorage);
        return existsProductStorage;
    }
}
