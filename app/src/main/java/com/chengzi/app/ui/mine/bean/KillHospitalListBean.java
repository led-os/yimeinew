package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.io.Serializable;

public class KillHospitalListBean implements Serializable {

    /**
     * {
     * "id": 1,
     * "user_id": 9,
     * "city_id": 110100,
     * "name": "222开内眼角 可升级贝塞尔技术 眼综合双眼皮修改",
     * "category_id": 1,
     * "category_id2": 38,
     * "category_id3": 39,
     * "buy_price": "45452.00",
     * "spell_price": "11255.00",
     * "doctor_id": "41",
     * "counselling_id": "7",
     * "image": [
     * "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"
     * ],
     * "video_image": null,
     * "goods_info": "详情描述",
     * "status": 1,
     * "feature": 1,
     * "grade": "6.9",
     * "sales_volume": 0,
     * "preset_amount": "0.00",
     * "is_extension": 2,
     * "create_time": 1555555556,
     * "update_time": null,
     * "delete_time": null,
     * "good_name": "222开内眼角 可升级贝塞尔技术 眼综合双眼皮修改",
     * "practical_price": "45452.00",
     * "plan": "6.9",
     * "is_participation": 0,
     * "logo": "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"
     * }
     */

    private String id;
    private String user_id;
    private String city_id;
    private String name;
    private String category_id;
    private String category_id2;
    private String category_id3;
    private String buy_price;
    private String spell_price; //拼团价
    private String doctor_id;
    private String counselling_id;
    //    private List<String> image;
    private String video_image;
    private String goods_info;
    private String status;
    private String feature;
    private String grade;
    private String sales_volume;
    private String preset_amount;
    private String is_extension;
    private String create_time;
    private String update_time;
    private String delete_time;
    private String goods_id;              //商品id
    private String goods_name;           // 商品名称
    private String practical_price;     // 实际价格
    private String plan;                // 评分
    private String is_participation;    // 是否参与过 0-否 1-是
    private String kill_price;       // 秒杀价格
    private String num;              // 限购数量
    private String time;             // 秒杀时间段
    private String logo;             // 商品图

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
        return buy_price;
    }

    public void setBuy_price(String buy_price) {
        this.buy_price = buy_price;
    }

    public String getSpell_price() {
        return spell_price == null ? (spell_price = "") : spell_price;
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

//    public List<String> getImage() {
//        return image;
//    }
//
//    public void setImage(List<String> image) {
//        this.image = image;
//    }

    public String getVideo_image() {
        return video_image;
    }

    public void setVideo_image(String video_image) {
        this.video_image = video_image;
    }

    public String getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(String goods_info) {
        this.goods_info = goods_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getGrade() {
        return grade;
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

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return !TextUtils.isEmpty(goods_name) ? goods_name : "";
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPractical_price() {
        return !TextUtils.isEmpty(practical_price) ? practical_price : "0.00";
    }

    public void setPractical_price(String practical_price) {
        this.practical_price = practical_price;
    }

    public String getPlan() {
        return !TextUtils.isEmpty(plan) ? plan : "0.0";
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getIs_participation() {  // 是否参与过 0-否 1-是
        return !TextUtils.isEmpty(is_participation) ? is_participation : "0";
    }

    public void setIs_participation(String is_participation) {
        this.is_participation = is_participation;
    }

    public String getKill_price() {
        return !TextUtils.isEmpty(kill_price) ? kill_price : "0.00";
    }

    public void setKill_price(String kill_price) {
        this.kill_price = kill_price;
    }

    public String getNum() {
        return !TextUtils.isEmpty(num) ? num : "0";
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTime() {
        return !TextUtils.isEmpty(time) ? time : "";
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
