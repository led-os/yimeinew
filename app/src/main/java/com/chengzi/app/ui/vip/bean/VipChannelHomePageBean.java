package com.chengzi.app.ui.vip.bean;

import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.home.bean.GoodBean;

import java.util.ArrayList;
import java.util.List;

public class VipChannelHomePageBean {

    private List<FindBaseBean<GoodBean>> category_list;
    private VipUserInfoBean userinfo;

    public List<FindBaseBean<GoodBean>> getCategory_list() {
        return category_list == null?(category_list = new ArrayList<>()):category_list;
    }

    public void setCategory_list(List<FindBaseBean<GoodBean>> category_list) {
        this.category_list = category_list;
    }

    public VipUserInfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(VipUserInfoBean userinfo) {
        this.userinfo = userinfo;
    }
}
