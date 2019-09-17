package com.chengzi.app.ui.mine.bean;

import android.text.TextUtils;

/**
 * @Desc:
 * @ClassName:DataAnalysisBean
 * @PackageName:com.yimei.app.ui.mine.bean
 * @Create On 2019/4/30 0030
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class DataAnalysisBean {

    /**
     * total_order_count : 0
     * operation_order_count : 0
     * no_operation_order_count : 0
     * today_nopro_order_count : 0
     * month_nopro_order_count : 0
     * today_pro_order_count : 0
     * month_pro_order_count : 3
     * today_total_order_count : 0
     * month_total_order_count : 3
     * today_advert_price : 0
     * month_advert_price : 0
     * today_visit_count : 0
     * today_goods_visit_count : 0
     */

    private String total_order_count;       // 历史总订单数
    private String operation_order_count;   // 手术订单数
    private String no_operation_order_count;// 皮肤订单数
    private String today_nopro_order_count; // 今日没推广成交数
    private String month_nopro_order_count; // 本月没推广成交数
    private String today_pro_order_count;   // 今日推广成交数
    private String month_pro_order_count;   // 本月推广成交数
    private String today_total_order_count; // 今日成交总数(包推广和没推广的)
    private String month_total_order_count; // 本月成交总数(包推广和没推广的)
    private String today_advert_price;      // 今日推广花费
    private String month_advert_price;      // 本月推广花费
    private String today_visit_count;       // 今日访客数量
    private String today_goods_visit_count; // 今日商品访客数量

    public String getTotal_order_count() {
        return total_order_count;
    }

    public void setTotal_order_count(String total_order_count) {
        this.total_order_count = total_order_count;
    }

    public String getOperation_order_count() {
        return !TextUtils.isEmpty(operation_order_count) ? operation_order_count : "0";
    }

    public void setOperation_order_count(String operation_order_count) {
        this.operation_order_count = operation_order_count;
    }

    public String getNo_operation_order_count() {
        return !TextUtils.isEmpty(no_operation_order_count) ? no_operation_order_count : "0";
    }

    public void setNo_operation_order_count(String no_operation_order_count) {
        this.no_operation_order_count = no_operation_order_count;
    }

    public String getToday_nopro_order_count() {
        return today_nopro_order_count;
    }

    public void setToday_nopro_order_count(String today_nopro_order_count) {
        this.today_nopro_order_count = today_nopro_order_count;
    }

    public String getMonth_nopro_order_count() {
        return month_nopro_order_count;
    }

    public void setMonth_nopro_order_count(String month_nopro_order_count) {
        this.month_nopro_order_count = month_nopro_order_count;
    }

    public String getToday_pro_order_count() {
        return today_pro_order_count;
    }

    public void setToday_pro_order_count(String today_pro_order_count) {
        this.today_pro_order_count = today_pro_order_count;
    }

    public String getMonth_pro_order_count() {
        return month_pro_order_count;
    }

    public void setMonth_pro_order_count(String month_pro_order_count) {
        this.month_pro_order_count = month_pro_order_count;
    }

    public String getToday_total_order_count() {
        return today_total_order_count;
    }

    public void setToday_total_order_count(String today_total_order_count) {
        this.today_total_order_count = today_total_order_count;
    }

    public String getMonth_total_order_count() {
        return month_total_order_count;
    }

    public void setMonth_total_order_count(String month_total_order_count) {
        this.month_total_order_count = month_total_order_count;
    }

    public String getToday_advert_price() {
        return today_advert_price;
    }

    public void setToday_advert_price(String today_advert_price) {
        this.today_advert_price = today_advert_price;
    }

    public String getMonth_advert_price() {
        return month_advert_price;
    }

    public void setMonth_advert_price(String month_advert_price) {
        this.month_advert_price = month_advert_price;
    }

    public String getToday_visit_count() {
        return today_visit_count;
    }

    public void setToday_visit_count(String today_visit_count) {
        this.today_visit_count = today_visit_count;
    }

    public String getToday_goods_visit_count() {
        return today_goods_visit_count;
    }

    public void setToday_goods_visit_count(String today_goods_visit_count) {
        this.today_goods_visit_count = today_goods_visit_count;
    }
}
