package com.chengzi.app.ui.account.bean;

import com.chengzi.app.dialog.SingleChoiceDialog;

import java.util.List;

public class BankCityBean implements SingleChoiceDialog.Choice {

    private String name;
    private String code;
    private List<BankCityBean> childrens;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<BankCityBean> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<BankCityBean> childrens) {
        this.childrens = childrens;
    }
}
