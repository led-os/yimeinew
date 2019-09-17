package com.chengzi.app.ui.home.bean;

import android.text.TextUtils;

public class TradeListBean {

    /**
     * id : 221
     * goods_id : 7
     * user_id : 34
     * type : 1
     * create_time : 1559032624
     * goods_name : 双眼皮
     * user_name : 这些
     * user_img :
     * timeBefore : 56分钟前
     */

    private String id;          // 订单id
    private String goods_id;    // 商品id
    private String user_id;     // 用户id
    private String type;        // 订单类型  1-普通订单 2-拼购订单 (已删除：3-秒杀订单)
    private String create_time; // 创建时间
    private String goods_name;  // 用户昵称
    private String user_name;   // 商品名称
    private String user_img;    // 用户头像
    private String timeBefore;  // 时间差
    private String surplus;     // 拼购剩余时间（秒）-- 只针对于拼购订单

    /**
     * {
     * "id": 256,
     * "goods_id": 8,
     * "user_id": 34,
     * "type": 1,
     * "create_time": 1559042282,
     * "goods_name": "眼部抽111",
     * "user_name": "这些",
     * "user_img": "",
     * "surplus": 202648,
     * "timeBefore": "15小时前"
     * }
     */

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getGoods_name() {
        return !TextUtils.isEmpty(goods_name) ? goods_name : "";
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_names() {
        if (!TextUtils.isEmpty(user_name)) {
            return user_name.length() > 4 ? user_name.substring(0, 4) + "…" : user_name;
        }

        return "";
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getTimeBefore() {
        return !TextUtils.isEmpty(timeBefore) ? timeBefore : "0分钟前";
    }

    public void setTimeBefore(String timeBefore) {
        this.timeBefore = timeBefore;
    }

    public String getSurplus() {
        return !TextUtils.isEmpty(surplus) ? surplus : "0天0时0分";
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }
}
