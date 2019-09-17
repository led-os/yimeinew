package com.chengzi.app.event;

public class OrderPaymentTypeEvent {
    private int type;    //  1-普通订单和秒杀订单 2-拼购订单

    public OrderPaymentTypeEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
