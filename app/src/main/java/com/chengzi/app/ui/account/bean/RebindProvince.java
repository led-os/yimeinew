package com.chengzi.app.ui.account.bean;

import android.text.TextUtils;

import java.util.List;

public class RebindProvince {

    private String provinceCode;
    private String cityCode;

    private BankCityBean province;
    private BankCityBean city;

    private List<BankCityBean> list;


    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setList(List<BankCityBean> list) {
        this.list = list;
    }


    public BankCityBean getProvince() {
        return province;
    }

    public BankCityBean getCity() {
        return city;
    }

    public void match(){
        if (list == null || provinceCode == null || cityCode == null) {
            return;
        }
        for (BankCityBean bean : list) {
            if (TextUtils.equals(bean.getCode(),provinceCode)) {
                province = bean;
                List<BankCityBean> childrens = province.getChildrens();
                for (BankCityBean cityBean : childrens) {
                    if (TextUtils.equals(cityCode,cityBean.getCode())) {
                        city = cityBean;
                        break;
                    }
                }
                break;
            }
        }
    }
}
