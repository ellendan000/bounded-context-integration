package top.bujiaban.seatatcc.order.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.BusinessActionContextUtil;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.bujiaban.common.order.domain.Order;
import top.bujiaban.common.order.domain.OrderRepository;
import top.bujiaban.common.order.domain.OrderStatus;
import top.bujiaban.common.order.service.OrderCreateCommand;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderCreateTccServiceImpl implements OrderCreateTccService {
    private final OrderRepository orderRepository;

    public OrderCreateTccServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order prepare(BusinessActionContext actionContext,
                         @BusinessActionContextParameter(paramName = "command") OrderCreateCommand command) {
        log.info("order prepare, xid {}", actionContext.getXid());
        Order newOrder = Order.builder()
                .id(UUID.randomUUID().toString())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .orderStatus(OrderStatus.PRE_CREATED)
                .build();
        newOrder = orderRepository.save(newOrder);
        BusinessActionContextUtil.addContext("orderId", newOrder.getId());
        return newOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commit(BusinessActionContext actionContext) {
        String newOrderId = actionContext.getActionContext("orderId", String.class);
        log.info("order create succeed, xid {}, order id {}",
                actionContext.getXid(), newOrderId);
        Optional<Order> currentOrder = orderRepository.findById(newOrderId);
        if(currentOrder.isEmpty()){
            return;
        }
        Order order = currentOrder.get();
        order.setOrderStatus(OrderStatus.CREATED);
        orderRepository.save(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(BusinessActionContext actionContext) {
        String newOrderId = actionContext.getActionContext("orderId", String.class);
        log.info("order create rollback, xid {}, order id {}",
                actionContext.getXid(), newOrderId);

        Optional<Order> currentOrder = orderRepository.findById(newOrderId);
        if(currentOrder.isEmpty()){
            return;
        }
        orderRepository.deleteById(newOrderId);
    }
}
