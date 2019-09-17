package com.chengzi.app.ui.account.bean;

import java.util.List;

public class AuditInfoBean {

    private CompanyBean risk_doc;
    private List<BankCityBean> bank_citys;
    private List<BankNameBean> bank_name_data;

    public CompanyBean getRisk_doc() {
        return risk_doc;
    }

    public void setRisk_doc(CompanyBean risk_doc) {
        this.risk_doc = risk_doc;
    }

    public List<BankCityBean> getBank_citys() {
        return bank_citys;
    }

    public void setBank_citys(List<BankCityBean> bank_citys) {
        this.bank_citys = bank_citys;
    }

    public List<BankNameBean> getBank_name_data() {
        return bank_name_data;
    }

    public void setBank_name_data(List<BankNameBean> bank_name_data) {
        this.bank_name_data = bank_name_data;
    }
}
