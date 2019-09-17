package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import com.handongkeji.utils.FormatUtil;

import java.io.Serializable;

public class ProductPromotionProductBean implements Serializable {

    /**
     * id : 17
     * product_name : 美容针针针针
     * category_id : 2
     * buy_price : 2000.00
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * grade : 10.0
     * userCount : 0
     * preset_amount : 0
     * is_promotion : false
     */

    private String id;              // 商品ID
    private String product_name;    // 名称
    private String category_id;     // 分类ID
    private String buy_price;       // 购买价格
    private String image;           // 图片
    private String grade;           // 评分
    private String userCount;       // 购买人数
    private String preset_amount;   // 推广花费限额
    private String hospital_name;   // 医院名称
    private boolean is_promotion;   // 是否设置为商品搜索推广

    public String getHospital_name() {
        return !TextUtils.isEmpty(hospital_name) ? hospital_name : "";
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return !TextUtils.isEmpty(product_name) ? product_name : "";
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBuy_price() {
        return !TextUtils.isEmpty(buy_price) ? buy_price : "0.00";
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getUserCount() {
        return !TextUtils.isEmpty(userCount) ? userCount : "0";
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getPreset_amount() {
//        return !TextUtils.isEmpty(preset_amount) ? preset_amount : "0";
        return !TextUtils.isEmpty(preset_amount) ? FormatUtil.format2Decimal(Double.parseDouble(preset_amount)) : "0.00";
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }

    public boolean isIs_promotion() {
        return is_promotion;
    }

    public void setIs_promotion(boolean is_promotion) {
        this.is_promotion = is_promotion;
    }


}
