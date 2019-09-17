package com.chengzi.app.event;

import java.util.List;

public class UserOrderIdEvent {

    private List<String> list;

    public UserOrderIdEvent(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }
}
