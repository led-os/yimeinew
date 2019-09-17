package com.chengzi.app.ui.peopleraise.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RaiseBean {

    private String id;           //
    private String plan_number;   //  美人筹订单号
    private String uid;         // 发起人id
    private String category_id;   //  3级分类id
    private String city_id;    //  城市id
    private String city;      //  城市
    private int plan_days;     //  计划时间
    private int people_number;   //  筹集人数
    private String price;       //  筹集价格
    private String hospital_id;
    private int status;     //  订单状态 0-待付款 1-未达成 2-待使用   3-已完成 4-已取消
    private String dec;     //  描述
    private String category_firstid;    //  一级分类id
    private String category_name;      //   3级分类名字
    private String plan;     //  进度

    private long end_time;   //  结束时间   10位时间戳

    @SerializedName(value = "priceing", alternate = {"schedule"})
    private String priceing;   //  已经筹集的金额

    private List<RaiseJoinUserBean> join_user;
    private List<RaiseJoinOrgBean> join_organization;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlan_number() {
        return plan_number;
    }

    public void setPlan_number(String plan_number) {
        this.plan_number = plan_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public int getPlan_days() {
        return plan_days;
    }

    public void setPlan_days(int plan_days) {
        this.plan_days = plan_days;
    }

    public int getPeople_number() {
        return people_number;
    }

    public void setPeople_number(int people_number) {
        this.people_number = people_number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getCategory_firstid() {
        return category_firstid;
    }

    public void setCategory_firstid(String category_firstid) {
        this.category_firstid = category_firstid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public String getPriceing() {
        return priceing;
    }

    public void setPriceing(String priceing) {
        this.priceing = priceing;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public List<RaiseJoinUserBean> getJoin_user() {
        return join_user == null ? (join_user = new ArrayList<>()) : join_user;
    }

    public void setJoin_user(List<RaiseJoinUserBean> join_user) {
        this.join_user = join_user;
    }

    public List<RaiseJoinOrgBean> getJoin_organization() {
        return join_organization;
    }

    public void setJoin_organization(List<RaiseJoinOrgBean> join_organization) {
        this.join_organization = join_organization;
    }
}
