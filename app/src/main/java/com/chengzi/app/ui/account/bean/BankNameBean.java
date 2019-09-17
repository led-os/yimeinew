package com.chengzi.app.ui.account.bean;

import com.chengzi.app.dialog.SingleChoiceDialog;

public class BankNameBean implements SingleChoiceDialog.Choice {

    /**
     * bank_name : 平安银行
     * code : PINGANBANK
     */

    private String bank_name;
    private String code;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return bank_name;
    }

    @Override
    public String getValue() {
        return code;
    }

    public BankNameBean() {
    }

    public BankNameBean(String bank_name, String code) {
        this.bank_name = bank_name;
        this.code = code;
    }
}
