package top.bujiaban.seatatcc.inventory.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.inventory.domain.ProductStorage;
import top.bujiaban.common.inventory.domain.ProductStorageRepository;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.infrastructure.client.MinusProductStorageRequest;

import javax.persistence.criteria.CriteriaBuilder;

@Slf4j
@Service
public class MinusStorageTccServiceImpl implements MinusStorageTccService {
    private final ProductStorageRepository productStorageRepository;

    public MinusStorageTccServiceImpl(ProductStorageRepository productStorageRepository) {
        this.productStorageRepository = productStorageRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductStorage prepare(BusinessActionContext actionContext,
                        String productId, Integer quantity) {
        log.info("Minus storage prepare, xid {}", actionContext.getXid());
        if (System.currentTimeMillis() % 3 == 0) {
            log.error("Random rollback");
            throw new RuntimeException("Random rollback order created");
        }

        ProductStorage productStorage = productStorageRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("product storage data not exists, product id [{}]", productId);
                    return new RuntimeException("product storage data not exists");
                });
        productStorage = productStorage.minusStorage(quantity);
        return productStorageRepository.save(productStorage);
    }

    @Override
    public void commit(BusinessActionContext actionContext) {
        log.info("minus storage succeed, xid {}", actionContext.getXid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(BusinessActionContext actionContext) {
        String productId = actionContext.getActionContext("productId", String.class);
        Integer quantity = actionContext.getActionContext("quantity", Integer.class);
        log.info("minus storage rollback, xid {}, productId {}, quantity {}",
                actionContext.getXid(), productId, quantity);

        ProductStorage productStorage = productStorageRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("product storage data not exists, product id [{}]", productId);
                    return new RuntimeException("product storage data not exists");
                });
        productStorage = productStorage.minusStorage(quantity * -1);
        productStorageRepository.save(productStorage);
    }
}
