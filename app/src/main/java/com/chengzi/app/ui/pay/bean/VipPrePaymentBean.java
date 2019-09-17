package com.chengzi.app.ui.pay.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VipPrePaymentBean implements Serializable {

    private static final long serialVersionUID = 86834444422892189L;

    /**
     * order_number_id : CZ_VIP430898448024743
     * product_name : 开通VIP
     * payment_type : 1
     * amount_pay : 980.00
     */
    @SerializedName(value = "order_number_id", alternate = {"plan_number"})
    private String order_number_id;         // 订单号 / 美人筹订单编号（唯一）

    @SerializedName(value = "product_name", alternate = {"type_name"})
    private String product_name = "开通会员";            // 购买方式

    private String payment_type;

    @SerializedName(value = "amount_pay", alternate = {"price", "pay_price"})
    private String amount_pay;              // 支付金额

    @SerializedName(value = "goods_name", alternate = {"vip_name"})
    private String goods_name; // 会员规格名称   / 商品名称（对应三级分类名）

    private String id;              //  订单ID/美人筹参与订单id
    private String prepaylog_id;    // 发起支付id( // 首信易支付的  )

    //一下是 美人筹预支付有的
    private String user_id;         // 下单者用户id
    private String plan_id;         // 此美人筹发布订单id，即主订单id

    private String payTime;

    //VIP专有
    private int vip_id;//会员规格ID

    public int getVip_id() {
        return vip_id;
    }

    public void setVip_id(int vip_id) {
        this.vip_id = vip_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getOrder_number_id() {
        return order_number_id;
    }

    public void setOrder_number_id(String order_number_id) {
        this.order_number_id = order_number_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getAmount_pay() {
        return amount_pay;
    }

    public void setAmount_pay(String amount_pay) {
        this.amount_pay = amount_pay;
    }

    public String getPrepaylog_id() {
        return prepaylog_id;
    }

    public void setPrepaylog_id(String prepaylog_id) {
        this.prepaylog_id = prepaylog_id;
    }

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

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPayTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        payTime = sdf.format(new Date());
        return payTime;
    }
}
