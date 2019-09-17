package com.chengzi.app.ui.home.bean;

import android.text.TextUtils;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.chengzi.app.ui.vip.bean.VipUserInfoBean;
import com.chengzi.app.utils.DoubleTypeAdapter;
import com.chengzi.app.utils.FloatTypeAdapter;
import com.chengzi.app.utils.IntTypeAdapter;
import com.chengzi.app.utils.ScoreUtils;

public class GoodBean {

    private String id;      // 商品id
    private String user_id;   // 机构id

    @SerializedName(value = "goods_name", alternate = {"name"})
    private String name;       // 商品名称

    @JsonAdapter(value = DoubleTypeAdapter.class)
    private double buy_price;      // 价格

    @JsonAdapter(value = DoubleTypeAdapter.class)
    private double spell_price;     //  拼团价

    private String grade;            // 评分
    private int create_time;        // 创建时间

    @JsonAdapter(value = IntTypeAdapter.class)
    @SerializedName(value = "is_VIP", alternate = {"is_vip"})
    private int is_VIP;               // 是否是vip   0 否 1是',

//    @JsonAdapter(value = IntTypeAdapter.class)
//    private int is_extension;              // 是否是推广产品   0 否 1是',

    private String city_id;           //   城市id

    @SerializedName(value = "orderNum", alternate = {"sales_volume", "people_num", "sellCount"})
    private int orderNum;              // 购买数量

    @SerializedName(value = "hospital_name", alternate = {"institution", "user_name", "organization_name"})
    private String hospital_name;        // 机构名称
    private int hosipital_type;
    private String sub_name;     //  民营机构

    @SerializedName(value = "logo", alternate = {"image"})
    private String logo;              //  商品图片
    private int evaluateNum;
    private int caseNoteNum;

    @JsonAdapter(value = FloatTypeAdapter.class)
    private float distance;

    private String promotion_id;    //   推广id
    private VipUserInfoBean user;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(double buy_price) {
        this.buy_price = buy_price;
    }

    public String getGrade() {
        return ScoreUtils.processScore(grade);
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public int getIs_VIP() {
        return is_VIP;
    }

    public void setIs_VIP(int is_VIP) {
        this.is_VIP = is_VIP;
    }

    public int getIs_extension() {
        if (TextUtils.isEmpty(promotion_id)) {
            return 0;
        }
        long id = Long.valueOf(promotion_id);
        return id > 0 ? 1 : 0;
    }

//    public void setIs_extension(int is_extension) {
//        this.is_extension = is_extension;
//    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getHospital_name() {
        return user != null ? user.getName() : hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getHosipital_type() {
        return hosipital_type;
    }

    public void setHosipital_type(int hosipital_type) {
        this.hosipital_type = hosipital_type;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public int getCaseNoteNum() {
        return caseNoteNum;
    }

    public void setCaseNoteNum(int caseNoteNum) {
        this.caseNoteNum = caseNoteNum;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getSpell_price() {
        return spell_price;
    }

    public void setSpell_price(double spell_price) {
        this.spell_price = spell_price;
    }

    public String getPromotion_id() {
        return promotion_id;
    }

    public void setPromotion_id(String promotion_id) {
        this.promotion_id = promotion_id;
    }

    public VipUserInfoBean getUser() {
        return user;
    }

    public void setUser(VipUserInfoBean user) {
        this.user = user;
    }
}
