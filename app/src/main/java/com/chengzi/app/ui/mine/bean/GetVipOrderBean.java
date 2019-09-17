package com.chengzi.app.ui.mine.bean;

import java.io.Serializable;

public class GetVipOrderBean implements Serializable {
    /**
     * vip_id : 4
     * price : 8.80
     * order_number_id : CZ_VIP606152859135244
     * id : 15
     * prepaylog_id : 116
     * vip_name : 30天包月
     */

    private String vip_id;    // 会员规格ID
    private String price;     // 充值金额
    private String order_number_id;// 订单编号
    private String id;        // 订单ID
    private String prepaylog_id;   // prepaylog id
    private String vip_name;  // VIPName

    public String getVip_id() {
        return vip_id;
    }

    public void setVip_id(String vip_id) {
        this.vip_id = vip_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOrder_number_id() {
        return order_number_id;
    }

    public void setOrder_number_id(String order_number_id) {
        this.order_number_id = order_number_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrepaylog_id() {
        return prepaylog_id;
    }

    public void setPrepaylog_id(String prepaylog_id) {
        this.prepaylog_id = prepaylog_id;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }
}
