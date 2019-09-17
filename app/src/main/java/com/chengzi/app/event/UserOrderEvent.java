package com.chengzi.app.event;

import com.chengzi.app.ui.mine.bean.UserOrderListBean;

import java.util.List;

public class UserOrderEvent {

    List<UserOrderListBean> list;

    public UserOrderEvent(List<UserOrderListBean> list) {
        this.list = list;
    }

    public List<UserOrderListBean> getList() {
        return list;
    }
}
