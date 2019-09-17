package com.chengzi.app.ui.mine.bean;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.chengzi.app.BR;

/**
 * @Desc:
 * @ClassName:CoAccountBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/26 0026
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class CoAccountBean implements Observable {
    private String name;//真实姓名

    private String card_id;// 身份证号

    private String bank_id;//银行卡号

    private String bank_name;//银行名称

    private String tel;//预留手机号

    private String openingBranch;

    private String m_code;// 手机验证码
    private transient PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();

    @Bindable
    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
        notifyChange(BR.name);
    }

    @Bindable
    public String getCard_id() {
        return card_id == null ? "" : card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
        notifyChange(BR.card_id);
    }

    @Bindable
    public String getBank_id() {
        return bank_id == null ? "" : bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
        notifyChange(BR.bank_id);
    }

    @Bindable
    public String getBank_name() {
        return bank_name == null ? "" : bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
        notifyChange(BR.bank_name);
    }

    @Bindable
    public String getTel() {
        return tel == null ? "" : tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
        notifyChange(BR.tel);
    }

    @Bindable
    public String getM_code() {
        return m_code == null ? "" : m_code;
    }

    public void setM_code(String m_code) {
        this.m_code = m_code;
        notifyChange(BR.m_code);
    }

    @Bindable
    public String getOpeningBranch() {
        return openingBranch;
    }

    public void setOpeningBranch(String openingBranch) {
        this.openingBranch = openingBranch;
        notifyChange(BR.openingBranch);
    }

    private synchronized void notifyChange(int propertyId) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.notifyChange(this, propertyId);
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry == null) {
            propertyChangeRegistry = new PropertyChangeRegistry();
        }
        propertyChangeRegistry.add(callback);

    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (propertyChangeRegistry != null) {
            propertyChangeRegistry.remove(callback);
        }
    }
}
