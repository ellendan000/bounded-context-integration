package top.bujiaban.common.order.domain;

// 非电商订单（CREATED基本就代表付款）
public enum OrderStatus {
    PRE_CREATED, CREATED, UPDATED, CANCELLED, RETURNED
}
