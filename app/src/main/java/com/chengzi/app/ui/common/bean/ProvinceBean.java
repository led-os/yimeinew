package com.chengzi.app.ui.common.bean;

import java.util.ArrayList;
import java.util.List;

public class ProvinceBean {


    private String province_id;
    private String province_name;
    private List<CityBean> child;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public List<CityBean> getChild() {
        return child == null ? (child = new ArrayList<>()) : child;
    }

    public void setChild(List<CityBean> child) {
        this.child = child;
    }

}
