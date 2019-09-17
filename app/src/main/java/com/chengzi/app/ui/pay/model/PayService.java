package com.chengzi.app.ui.pay.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;
import com.chengzi.app.ui.pay.bean.PaymentBean;
import com.chengzi.app.ui.pay.bean.VipPrePaymentBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PayService {

    //Order - 获取支付信息-支付前（冀）
    @GET("getPayOrder")
    Observable<ResponseBean<List<UserOrderListBean>>> getPayOrder(@Query("token") String token,
                                                                  @Query("user_id") String user_id,
                                                                  @Query("order_id[]") List<String> order_id);

    //
//    //VIP - 预支付页面
//    @FormUrlEncoded
//    @POST("vipPrePayment")
//    Observable<ResponseBean<VipPrePaymentBean>> vipPrePayment(@Field("token") String token,
//                                                              @Field("order_id") String order_id);
    //VIP - 生成VIP订单
    @FormUrlEncoded
    @POST("getVipPreOrder")
    Observable<ResponseBean<VipPrePaymentBean>> getVipPreOrder(@Field("token") String token,
                                                               @Field("user_id") String user_id,
                                                               @Field("vip_id") String vip_id); //【必填】会员规格ID

    //Plan - 美人筹支付订单页面（冀）
    @GET("planPayOrder")
    Observable<ResponseBean<VipPrePaymentBean>> planPayOrder(@Query("token") String token,
                                                             @Query("user_id") String user_id,
                                                             @Query("plan_order_id") String plan_order_id);

    /**
     * @param prepayId    支付id
     * @param paymentType wechat_app和ali_h5
     */
    @GET("paymentServer/trade")
    Observable<ResponseBean<PaymentBean>> payment(@Query("prepaylog_id") String prepayId, @Query("payment_type") String paymentType);

    /**
     * 查询订单支付状态
     *
     * @param prepaylogId
     * @return 1-已支付 2-未支付成功
     */
    @GET("getPayResults")
    Observable<ResponseBean<Integer>> getPayStatus(@Query("prepaylog_id") String prepaylogId);


    @GET("paymentServer/tradeNotify")
    Observable<ResponseBean> payNotify(@Query("prepaylog_id") String prepaylogId);

}