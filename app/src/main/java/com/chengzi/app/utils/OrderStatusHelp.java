package com.chengzi.app.utils;

import com.nevermore.oceans.ob.SuperObservableManager;

public class OrderStatusHelp {
    /**
     * 刷新其他订单列表，
     * 取消订单删除订单，其他订单列表页须刷新
     *
     * @param status 0-全部 1-待付款 2未达成 3-待使用 4-待评价 5-已完成 6-已取消（非必须）
     * @param.. type 1-普通订单和秒杀订单 2-拼购订单 （必须）
     */
    public static void refreshOrderList(int status) {
        SuperObservableManager.notify(OrderTypeChange.class, orderTypeChange -> orderTypeChange.typeChange(status));
    }

    public static void refreshOrderList(int type, int status) {
        SuperObservableManager.notify(OrderTypeChange.class, orderTypeChange -> orderTypeChange.typeChange(type, status));
    }

    public interface OrderTypeChange {
        void typeChange(int status);

        void typeChange(int type, int status);
    }


    public static void refreshBeautyOrderList(int status) {
        SuperObservableManager.notify(BeautyOrderTypeChange.class, orderTypeChange -> orderTypeChange.typeChange(status));
    }

    public interface BeautyOrderTypeChange {
        void typeChange(int status);
    }


    public static void refreshPromoteList(String zone_type, String cate_id) {
        SuperObservableManager.notify(PromoteTypeChange.class, orderTypeChange -> orderTypeChange.typeChange(zone_type, cate_id));
    }

    public interface PromoteTypeChange {
        void typeChange(String zone_type, String cate_id);
    }

    ///参与秒杀
    public static void refreshTakePartSeckillList(String cate_id) {
        SuperObservableManager.notify(TakePartSeckillTypeChange.class, orderTypeChange -> orderTypeChange.typeChange(cate_id));
    }

    public interface TakePartSeckillTypeChange {
        void typeChange(String cate_id);
    }
}