package com.chengzi.app.ui.seckill.bean;

import android.text.TextUtils;

public class KillIndexBean {

    /**
     * id : 4
     * goods_id : 7
     * uid : 8
     * time : 14
     * price : 2000.00
     * num : 200
     * stock : 200
     * category_id : 1
     * status : 1
     * is_vip : 1
     * create_time : 1556596576
     * update_time : 1556596576
     * delete_time : null
     * good_name : 双眼皮
     * practical_price : 0.01
     * hospital_name : 北京三甲医院
     * image : https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg
     * plan : 0
     * grade : 10.0
     */

    private String id;         // 秒杀订单id
    private String goods_id;   // 商品id
    private String uid;        // 用户id
    private String time;       // 时间 对应配置里面->(整点时间的 小时)
    private String price;      // 秒杀价格
    private String num;        // 秒杀数量
    private String stock;      // 商品库存，-1不限制
    private String category_id;// 一级分类id
    private String status;     // 0-抢完  1-进行中
    private String remind;     // 0-未提醒  1-已开启
    private String is_vip;     // #是否vip  0  否   1  是
    private String create_time;// 秒杀订单id
    private String update_time;// 秒杀订单id
    private String delete_time;// 秒杀订单id
    private String good_name;  // 商品名字
    private String practical_price; // 实际价格
    private String hospital_name;   // 机构名称
    private String image;     // 头像
    private String plan;      // 进度
    private String grade;     // 机构评分

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getStatus() {
        return !TextUtils.isEmpty(status) ? status : "0";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemind() {
        return !TextUtils.isEmpty(remind) ? remind : "0";
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getIs_vip() {
        return !TextUtils.isEmpty(is_vip) ? is_vip : "0";
    }

    public void setIs_vip(String is_vip) {
        this.is_vip = is_vip;
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

    public String getGood_name() {
        return !TextUtils.isEmpty(good_name) ? good_name : "";
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public String getPractical_price() {
        return practical_price;
    }

    public void setPractical_price(String practical_price) {
        this.practical_price = practical_price;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPlan() {
        return !TextUtils.isEmpty(plan) ? plan : "0";
    }

    public int getPlan_int() {      //以防出现负数。小数。
        String plan = getPlan();
        try {
            double v = Double.parseDouble(plan);
            if (v <= 0) {
                return 0;
            }
            return (int) v;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getPlan_name() {
        return "已抢:" + getPlan_int() + "%";
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
