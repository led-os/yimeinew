package com.chengzi.app.ui.mine.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.CartGetPriceBean;
import com.chengzi.app.ui.mine.bean.GetEvaluationObjectBean;
import com.chengzi.app.ui.mine.bean.ShopCarBean;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.ui.mine.bean.UserOrderListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ShopCartAndOrderService {

    //Cart - 购物车商品列表（冀）
    @GET("cartGoodsList")
    Observable<ResponseBean<PageBean<ShopCarBean>>> cartGoodsList(@Query("token") String token,
                                                                  @Query("user_id") String user_id,
                                                                  @Query("limit") int limit,
                                                                  @Query("page") int page);

    //Cart - 更新购物车中商品数量（冀）
    @GET("cartUpdateNum")
    Observable<ResponseBean> cartUpdateNum(@Query("token") String token,
                                           @Query("user_id") String user_id,
                                           @Query("id") String id,
                                           @Query("num") String num);

    //Cart - 获取购物车金额（冀）
    //购物车信息id （必须） Array
    @GET("cartGetPrice")
    Observable<ResponseBean<CartGetPriceBean>> cartGetPrice(@Query("token") String token,
                                                            @Query("user_id") String user_id,
                                                            @Query("cart_info_id[]") List<String> cart_info_id);

    //Cart - 删除购物车商品（冀）
    @GET("cartDelete")
    Observable<ResponseBean> cartDelete(@Query("token") String token,
                                        @Query("user_id") String user_id,
                                        @Query("cart_info_id[]") List<String> cart_info_id);

    //Order - 购物车下单
    @FormUrlEncoded
    @POST("cartAddOrder")
    Observable<ResponseBean<List<String>>> cartAddOrder(@Field("token") String token,
                                                        @Field("user_id") String user_id,
                                                        @Field("cart_info_id[]") List<String> cart_info_id,
                                                        @Field("longitude") String longitude,
                                                        @Field("latitude") String latitude);

    //Order - 确认使用
    @FormUrlEncoded
    @POST("orderConfirmUse")
    Observable<ResponseBean> orderConfirmUse(@Field("token") String token,
                                             @Field("user_id") String user_id,
                                             @Field("order_id") String order_id,
                                             @Field("type") String type,
                                             @Field("order_type") String order_type); //1普通订单,拼购,秒杀 2美人筹

    //Order - 获取普通用户的订单列表（冀）
    @GET("userOrderList")
    Observable<ResponseBean<PageBean<UserOrderListBean>>> userOrderList(@QueryMap Map<String, String> map);

    //Order - 普通用户订单详情
    @GET("userOrderDetails")
    Observable<ResponseBean<UserOrderListBean>> userOrderDetails(@Query("token") String token,
                                                                 @Query("user_id") String user_id,
                                                                 @Query("order_id") String order_id);

    //Order - 用户手动取消订单（冀）
    @GET("userOrderCancel")
    Observable<ResponseBean> userOrderCancel(@Query("token") String token,
                                             @Query("user_id") String user_id,
                                             @Query("order_id") String order_id);

    //Order - 获取评价对象(冀)
    @GET("getEvaluationObject")
    Observable<ResponseBean<GetEvaluationObjectBean>> getEvaluationObject(@Query("token") String token,
                                                                          @Query("user_id") String user_id,
                                                                          @Query("order_id") String order_id);


    //Order - 订单评价（冀）
    @FormUrlEncoded
    @POST("orderEvaluation")
    Observable<ResponseBean> orderEvaluation(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST("orderEvaluation")
    Observable<ResponseBean> orderEvaluation(@FieldMap Map<String, Object> params,
                                             @Field("image[]") List<String> image);

    //Complaint - 投诉(小芸)
    @FormUrlEncoded
    @POST("ComplaintEvent")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> complaintEvent(@FieldMap Map<String, Object> params,
                                            @Field("image[]") List<String> image);

    /**
     * Order - 补差价下单（冀）
     *
     * @param token
     * @param user_id
     * @param order_id     订单id
     * @param payment_type 支付方式 1-微信 2-支付宝（
     * @param spreads      补差金额，单位元
     * @return
     */
    @FormUrlEncoded
    @POST("spreadsOrder")
    Observable<ResponseBean<SpreadsOrderBean>> spreadsOrder(@Field("token") String token,
                                                            @Field("user_id") String user_id,
                                                            @Field("order_id") String order_id,
                                                            @Field("payment_type") String payment_type,
                                                            @Field("spreads") String spreads);

}