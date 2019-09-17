package com.chengzi.app.ui.vip.bean;

import com.chengzi.app.ui.home.bean.GoodBean;

import java.util.List;

public class VipNormalChannelBean {

    private List<GoodBean> promotion_goods_list;

    private List<VipUserInfoBean> doctor_list;
    private List<VipUserInfoBean> consultant_list;

    private List<GoodBean> goods_list;

    private List<VipUserInfoBean> hospital_list;

    public List<GoodBean> getPromotion_goods_list() {
        return promotion_goods_list;
    }

    public void setPromotion_goods_list(List<GoodBean> promotion_goods_list) {
        this.promotion_goods_list = promotion_goods_list;
    }

    public List<VipUserInfoBean> getDoctor_list() {
        return doctor_list;
    }

    public void setDoctor_list(List<VipUserInfoBean> doctor_list) {
        this.doctor_list = doctor_list;
    }

    public List<VipUserInfoBean> getConsultant_list() {
        return consultant_list;
    }

    public void setConsultant_list(List<VipUserInfoBean> consultant_list) {
        this.consultant_list = consultant_list;
    }

    public List<GoodBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodBean> goods_list) {
        this.goods_list = goods_list;
    }

    public List<VipUserInfoBean> getHospital_list() {
        return hospital_list;
    }

    public void setHospital_list(List<VipUserInfoBean> hospital_list) {
        this.hospital_list = hospital_list;
    }
}
