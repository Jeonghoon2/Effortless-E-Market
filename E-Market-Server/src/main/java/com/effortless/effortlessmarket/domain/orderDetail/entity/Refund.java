package com.effortless.effortlessmarket.domain.orderDetail.entity;

public enum Refund {

    SIMPLE_CHANGE_OF_MIND("단순 변심");

    private String reason;

    Refund(String reason) {
        this.reason = reason;
    }
}
