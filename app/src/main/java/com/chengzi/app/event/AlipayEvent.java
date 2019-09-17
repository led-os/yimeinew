package com.chengzi.app.event;

public class AlipayEvent {

    private int payStatus;      //  1 success, 0 fail

    public AlipayEvent(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPayStatus() {
        return payStatus;
    }
}
