package top.bujiaban.seataat.order.domain;

// 非电商订单（CREATED基本就代表付款）
public enum OrderStatus {
    CREATED, UPDATED, CANCELLED, RETURNED
}
