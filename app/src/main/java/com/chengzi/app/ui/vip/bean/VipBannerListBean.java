package com.chengzi.app.ui.vip.bean;

public class VipBannerListBean {

    /**
     * id : 10
     * city_id : 110100
     * cate_id : 2
     * goods_id : 25
     * hospital_id : null
     * amount : null
     * number : null
     * typeKey : banner
     * is_vip : false
     * is_advert : true
     * banner_img :
     * create_time : 2019-04-19 11:56:10
     */

    private String id;           // 推广的ID
    private String city_id;      // 城市ID
    private String cate_id;      // 分类的ID
    private String goods_id;     // 商品的ID
    private String hospital_id;  // 医院的ID
    private String amount;
    private String number;
    private String typeKey;      // 该推广是属于哪个推广。banner/广告位；goods/商品
    private boolean is_vip = false;    // 是否VIP
    private boolean is_advert = false; // 推广是否处于有效期
    private String banner_img;   // Banner推广时的广告位图
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public boolean isIs_vip() {
        return is_vip;
    }

    public void setIs_vip(boolean is_vip) {
        this.is_vip = is_vip;
    }

    public boolean isIs_advert() {
        return is_advert;
    }

    public void setIs_advert(boolean is_advert) {
        this.is_advert = is_advert;
    }

    public String getBanner_img() {
        return banner_img;
    }

    public void setBanner_img(String banner_img) {
        this.banner_img = banner_img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
