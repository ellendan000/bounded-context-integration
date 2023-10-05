package top.bujiaban.seatatcc.inventory.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import top.bujiaban.common.inventory.domain.ProductStorage;

@LocalTCC
public interface MinusStorageTccService {
    @TwoPhaseBusinessAction(name = "orderCreateMinusStorageBean", commitMethod = "commit",
            rollbackMethod = "rollback", useTCCFence = true)
    ProductStorage prepare(BusinessActionContext actionContext,
                           @BusinessActionContextParameter("productId") String productId,
                           @BusinessActionContextParameter("quantity") Integer quantity);

    void commit(BusinessActionContext actionContext);
    void rollback(BusinessActionContext actionContext);
}
