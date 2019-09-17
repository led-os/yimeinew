package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

public class SeeOrderBean {

    /**
     * id : 1
     * order_number_id : CcVIPB408169605605290
     * user_id : 1
     * hospital_id : 3
     * goods_id : 1
     * doctor_id : 26
     * counselling_id : 26
     * status : 5
     * h_status : 1
     * amount : 980.00
     * amount_spreads : 0.00
     * amount_discount : 0.00
     * amount_pay : 980.00
     * coupon_id : null
     * number : 1
     * type : 1
     * cancel_reason :
     * payment_type : 1
     * complete_time : null
     * order_code : null
     * order_qrcode :
     * create_time : 2019/04/08 17:49
     * update_time : null
     * delete_time : null
     * name : 阿布
     * orange_create : 800
     * gender : 1
     * image : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
     * grade : 2.7
     * goods_name : 全切双眼皮+开内眼角 可升级贝塞尔技术 眼综合双眼皮修改
     * price : 980.00
     * num : 49
     * hospital_name : 阿姐
     * goods_image : ["https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=692017529,2231912299&fm=27&gp=0.jpg"]
     * type_name : 直接购买
     */

    private String id;               // 订单id
    private String order_number_id;
    private String user_id;          // 用户id
    private String hospital_id;      // 机构id
    private String goods_id;         // 商品id
    private String doctor_id;        // 医生id
    private String counselling_id;   // 咨询师id
    private String status;           // 订单状态 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消
    private String h_status;         // 机构订单状态（结合status使用），默认0待使用，1待评价，2已完成
    private String amount;           // 订单金额
    private String amount_spreads;   // 补差价
    private String amount_discount;  // 优惠金额
    private String amount_pay;       // 支付金额       // 订单价格
    private String coupon_id;        // 优惠券id
    private String number;           // 购买数量
    private String type;             // 用户类型 1-用户 2-医生 3-咨询师 4-机构
    private String cancel_reason;    // 订单取消原因
    private String payment_type;
    private String complete_time;    // 订单验证时间，完成时间，确认使用时间
    private String order_code;       // 订单验证码
    private String order_qrcode;     // 订单验证二维码
    private String create_time;      // 创建时间
    private String update_time;
    private String delete_time;
    private String name;             // 用户名称
    private String orange_create;    // 橙子信用
    private String gender;           // 用户性别
    private String grade;            // 用户评分
    private String goods_name;       // 商品名称
    private String goods_status;   //商品状态 1-上架 2-下架
    private String price;
    private String num;             // xx人购买
    private String hospital_name;   //医院名称
    private String type_name;       //购买方式  1-直接购买, 2-拼购, 3-限时秒杀
    private String image;           //用户头像
    private String goods_image;   //商品头像
    private String age;   //年龄
    private String distance;   //距离
    private String goods_grade; //商品评分

    public String getDistance() {
        return !TextUtils.isEmpty(distance) ? distance + "km" : "0km";
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAge() {
        return TextUtils.isEmpty(age) || age.equals("0") ? "" : age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_number_id() {
        return order_number_id;
    }

    public void setOrder_number_id(String order_number_id) {
        this.order_number_id = order_number_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(String hospital_id) {
        this.hospital_id = hospital_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getH_status() {
        return h_status;
    }

    public void setH_status(String h_status) {
        this.h_status = h_status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount_spreads() {
        return amount_spreads;
    }

    public void setAmount_spreads(String amount_spreads) {
        this.amount_spreads = amount_spreads;
    }

    public String getAmount_discount() {
        return amount_discount;
    }

    public void setAmount_discount(String amount_discount) {
        this.amount_discount = amount_discount;
    }

    public String getAmount_pay() {
        return amount_pay;
    }

    public void setAmount_pay(String amount_pay) {
        this.amount_pay = amount_pay;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_qrcode() {
        return order_qrcode;
    }

    public void setOrder_qrcode(String order_qrcode) {
        this.order_qrcode = order_qrcode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrange_create() {
        return orange_create;
    }

    public void setOrange_create(String orange_create) {
        this.orange_create = orange_create;
    }

    public String getGender() {
        return !TextUtils.isEmpty(gender) ? gender : "0";
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return !TextUtils.isEmpty(grade) ? grade : "0.0";
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public String getGoods_grade() {
        return !TextUtils.isEmpty(goods_grade) ? goods_grade : "0.0";
    }

    public void setGoods_grade(String goods_grade) {
        this.goods_grade = goods_grade;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    ////商品状态 1-上架 2-下架
    public String getGoods_status() {
        return !TextUtils.isEmpty(goods_status) ? goods_status : "1";
    }

    public void setGoods_status(String goods_status) {
        this.goods_status = goods_status;
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

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }
}
