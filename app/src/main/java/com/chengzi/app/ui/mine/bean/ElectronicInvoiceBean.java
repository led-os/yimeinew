package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 电子发票列表的bean类
 *
 * @ClassName:ElectronicInvoiceBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/28 002818:38
 * @Site:http://www.handongkeji.com
 * @author: wanghongfu
 * @Copyrights 2019/4/28 0028 handongkeji All rights reserved.
 */
public class ElectronicInvoiceBean implements Serializable {

    /**
     * id : 14
     * user_id : 1
     * money : 100
     * pay_mode : 2
     * pay_type : 3
     * order_id : 7
     * order_number_id : null
     * extension_info_id : null
     * status : 1
     * is_invoice : 2
     * extension_id : 1
     * create_time : 2019/04/18 10:25
     * update_time : null
     * delete_time : null
     * pay_type_name : 订单
     * pay_mode_name : 微信
     * goods_type : 精选
     */

    private String id;             // 发票id（开发票传此参数）
    private String user_id;        // 用户id
    private String money;          // 发票金额
    private String pay_mode;
    private String pay_type;
    private String order_id;
    private String order_number_id;
    private String extension_info_id;
    private String status;
    private String is_invoice;
    private String extension_id;
    private String create_time;    // 支付时间
    private String update_time;
    private String delete_time;
    private String pay_type_name;
    private String pay_mode_name;  // 支付方式
    private String goods_type;     // 商品类型
    //选中状态
    private boolean isChoose = false;

    public boolean getIsChoose() {
        return isChoose;
    }

    public void setIsChoose(boolean isChoose) {
        this.isChoose = isChoose;
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

    public String getMoney() {
        return !TextUtils.isEmpty(money) ? money : "0";
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(String pay_mode) {
        this.pay_mode = pay_mode;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number_id() {
        return order_number_id;
    }

    public void setOrder_number_id(String order_number_id) {
        this.order_number_id = order_number_id;
    }

    public String getExtension_info_id() {
        return extension_info_id;
    }

    public void setExtension_info_id(String extension_info_id) {
        this.extension_info_id = extension_info_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_invoice() {
        return is_invoice;
    }

    public void setIs_invoice(String is_invoice) {
        this.is_invoice = is_invoice;
    }

    public String getExtension_id() {
        return extension_id;
    }

    public void setExtension_id(String extension_id) {
        this.extension_id = extension_id;
    }

    public String getCreate_time() {
        return !TextUtils.isEmpty(create_time) ? create_time : "";
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

    public String getPay_type_name() {
        return pay_type_name;
    }

    public void setPay_type_name(String pay_type_name) {
        this.pay_type_name = pay_type_name;
    }

    public String getPay_mode_name() {
        return !TextUtils.isEmpty(pay_mode_name) ? pay_mode_name : "";
    }

    public void setPay_mode_name(String pay_mode_name) {
        this.pay_mode_name = pay_mode_name;
    }

    public String getGoods_type() {
        return !TextUtils.isEmpty(goods_type) ? goods_type : "";
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }
}