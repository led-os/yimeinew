package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class ShopCarBean {
    /**
     * id : 1
     * goods_id : 1
     * goods_num : 1
     * doctor_id : 7
     * advisers_id : 3
     * cart_info_id : 1
     * goods_image : ["\/public\/uploads\/2019-04-12\/5cb00bbd0bf3b.jpg"]
     * goods_name : 全切双眼皮+开内眼角 可升级贝塞尔技术 眼综合双眼皮修改
     * goods_price : 100.00
     * doctor_name : 笑笑二号
     * advisers_name : 阿姐
     */

    private String id;             // 购物车信息id，同cart_info_id
    private String goods_id;       // 商品id
    private String goods_num;      // 商品数量
    private String doctor_id;      // 选择的医生uid
    private String advisers_id;    // 选择的咨询师uid
    private String cart_info_id;   // 购物车信息id
    private String goods_image;    // 商品封面图片
    private String goods_name;     // 商品名称
    private String goods_price = "0.00";    // 商品价格
    private String doctor_name;    // 选择的医生姓名
    private String advisers_name;  // 选择的咨询师姓名


    //选中状态
    private boolean isChoose = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getDoctor_id() {
        return TextUtils.equals(doctor_id, "0") ? "" : doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getAdvisers_id() {
        return TextUtils.equals(advisers_id, "0") ? "" : advisers_id;
    }

    public void setAdvisers_id(String advisers_id) {
        this.advisers_id = advisers_id;
    }

    public String getCart_info_id() {
        return cart_info_id;
    }

    public void setCart_info_id(String cart_info_id) {
        this.cart_info_id = cart_info_id;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return !TextUtils.isEmpty(goods_price) ? goods_price : "0.00";
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getAdvisers_name() {
        return advisers_name;
    }

    public void setAdvisers_name(String advisers_name) {
        this.advisers_name = advisers_name;
    }

    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
    }
}
