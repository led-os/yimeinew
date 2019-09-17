package com.chengzi.app.ui.home.bean;

import java.io.Serializable;

public class LocationBean implements Serializable {

    private String province;
    private String cityName;

    public LocationBean(String province, String cityName) {
        this.province = province;
        this.cityName = cityName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
