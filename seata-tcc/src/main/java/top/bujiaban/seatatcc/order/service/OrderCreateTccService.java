package top.bujiaban.seatatcc.order.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.service.OrderCreateCommand;

@LocalTCC
public interface OrderCreateTccService {
    @TwoPhaseBusinessAction(name = "orderCreateBean",
            commitMethod = "commit", rollbackMethod = "rollback", useTCCFence = true)
    @Transactional(rollbackFor = Exception.class)
    Order prepare(BusinessActionContext actionContext,
                         @BusinessActionContextParameter(paramName = "command") OrderCreateCommand command);
    void commit(BusinessActionContext actionContext);
    void rollback(BusinessActionContext actionContext);
}
