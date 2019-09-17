package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class GoodsListManageBean {

    /**
     * id : 1
     * user_id : 8
     * city_id : 110100
     * name : 全切双眼皮+开内眼角 可升级贝塞尔技术 眼综合双眼皮修改
     * category_id : 33
     * category_id2 : null
     * category_id3 : null
     * buy_price : 100.00
     * spell_price : 12000.00
     * doctor_id : 7,14
     * counselling_id : 17,11
     * image : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
     * goods_info : 详情描述
     * status : 1
     * feature : 2
     * grade : 7.4
     * sales_volume : 11
     * preset_amount : 0.00
     * is_extension : 1
     * create_time : 1555555555
     * update_time : null
     * delete_time : null
     * logo : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * organization_name : 北京人民医院五院
     * is_VIP : 1
     * user_name : 北京三甲医院
     */

    private String id;              // 商品id
    private String user_id;         // 所属机构id
    private String city_id;         // 所属城市
    private String name;            // 商品名称
    private String category_id;     // 一级分类id
    private String category_id2;    // 二级分类id
    private String category_id3;    // 三级分类id
    private String buy_price;       // 抢购价
    private String spell_price;     // 拼团价
    private String doctor_id;       // 绑定医生id
    private String counselling_id;  // 绑定咨询师id
    private String goods_info;      // 商品详情
    private String status;          // 商品状态 1-上架 2-下架
    private String feature;         // 是否是特色商品，1-非特色  2-特色',
    private String grade;           // 商品评分
    private String sales_volume;    // 销量
    private String preset_amount;   // 预消费金额
    private String is_extension;    // 是否推广 1-是 2-否
    private String create_time;     // 商品添加时间
    private String update_time;
    private String delete_time;
    private String logo;            // 所属的机构名称Logo
    private String organization_name;  // 所属的机构名称
    private String is_VIP;          // 是否是vip   0 否 1是',
    private String user_name;       // 所属的机构名称
    private String image;     // 商品图片

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_id2() {
        return category_id2;
    }

    public void setCategory_id2(String category_id2) {
        this.category_id2 = category_id2;
    }

    public String getCategory_id3() {
        return category_id3;
    }

    public void setCategory_id3(String category_id3) {
        this.category_id3 = category_id3;
    }

    public String getBuy_price() {
        return !TextUtils.isEmpty(buy_price) ? buy_price : "0.00";
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getSpell_price() {
        return !TextUtils.isEmpty(spell_price) ? spell_price : "0.00";
    }

    public void setSpell_price(String spell_price) {
        this.spell_price = spell_price;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getCounselling_id() {
        return counselling_id;
    }

    public void setCounselling_id(String counselling_id) {
        this.counselling_id = counselling_id;
    }

    public String getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(String goods_info) {
        this.goods_info = goods_info;
    }

    // 商品状态 1-上架 2-下架
    public String getStatus() {
        return !TextUtils.isEmpty(status) ? status : "2";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 是否是特色商品，1-非特色  2-特色',
    public String getFeature() {
        return !TextUtils.isEmpty(feature) ? feature : "1";
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSales_volume() {
        return sales_volume;
    }

    public void setSales_volume(String sales_volume) {
        this.sales_volume = sales_volume;
    }

    public String getPreset_amount() {
        return preset_amount;
    }

    public void setPreset_amount(String preset_amount) {
        this.preset_amount = preset_amount;
    }

    public String getIs_extension() {
        return is_extension;
    }

    public void setIs_extension(String is_extension) {
        this.is_extension = is_extension;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(String is_VIP) {
        this.is_VIP = is_VIP;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
