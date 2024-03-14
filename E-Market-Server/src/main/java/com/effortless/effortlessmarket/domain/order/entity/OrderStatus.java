package com.effortless.effortlessmarket.domain.order.entity;

public enum OrderStatus {
    CANCEL, /* 취소됨 */
    READY, /* 준비중 */
    DELIVERY, /* 배송중 */
    COMPLETE  /* 배송 완료 */
}
