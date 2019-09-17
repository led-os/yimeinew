package com.chengzi.app.ui.mine.bean;


import android.text.TextUtils;

/**
 * 开票历史的bean类
 *
 * @ClassName:HistoryInvoiceBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/29 002911:18
 * @Site:http://www.handongkeji.com
 * @author: wanghongfu
 * @Copyrights 2019/4/29 0029 handongkeji All rights reserved.
 */
public class HistoryInvoiceBean {

    /**
     * id : 16
     * user_id : 1
     * order_id : 0
     * rise_type : 1
     * rise : 抬个头
     * identify_number : Nashuirenshibiehao111
     * invoice_content : 这个发票有内容
     * invoice_money : 3200
     * receive_mode :
     * email : 923468188@qq.com
     * image : null
     * create_time : 2019/04/19 17:28
     * update_time : null
     * delete_time : null
     * status : 0
     */

    private String id;
    private String user_id;
    private String order_id;
    private String rise_type;
    private String rise;            // 发票抬头
    private String identify_number;   // 纳税人识别号
    private String invoice_content;
    private String invoice_money;        // 开票金额
    private String receive_mode;
    private String email;
    private String image;
    private String create_time;    // 开票时间
    private String update_time;
    private String delete_time;
    private String status;          // 开票状态 0-未开票，1-已开票

    public String getStatusName() {
        return !TextUtils.isEmpty(status) && status.equals("1") ? "已开票" : "未开票";
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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRise_type() {
        return rise_type;
    }

    public void setRise_type(String rise_type) {
        this.rise_type = rise_type;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getIdentify_number() {
        return identify_number;
    }

    public void setIdentify_number(String identify_number) {
        this.identify_number = identify_number;
    }

    public String getInvoice_content() {
        return invoice_content;
    }

    public void setInvoice_content(String invoice_content) {
        this.invoice_content = invoice_content;
    }

    public String getInvoice_money() {
        return invoice_money;
    }

    public void setInvoice_money(String invoice_money) {
        this.invoice_money = invoice_money;
    }

    public String getReceive_mode() {
        return receive_mode;
    }

    public void setReceive_mode(String receive_mode) {
        this.receive_mode = receive_mode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return !TextUtils.isEmpty(image) ? image : "";
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
