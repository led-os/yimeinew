package com.chengzi.app.ui.mine.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.UserPlanOrderDetailsBean;
import com.chengzi.app.ui.mine.bean.UserPlanOrderListBean;
import com.chengzi.app.ui.mine.bean.VerificationOrderBean;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface BeauytRaiseOrderService {

    //PlanOrder______ - 我的美人筹订单
    @GET("userPlanOrderList")
    Observable<ResponseBean<PageBean<UserPlanOrderListBean>>> userPlanOrderList(@QueryMap HashMap<String, String> params);
    /*
    * @Query("token") String token,
                                                                                @Query("user_id") String user_id,
                                                                                @Query("limit") int limit,
                                                                                @Query("page") int page,
                                                                                @Query("status") int status
    * */

    //PlanOrder - 美人筹订单详情
    @GET("userPlanOrderDetails")
    Observable<ResponseBean<UserPlanOrderDetailsBean>> userPlanOrderDetails(@Query("token") String token,
                                                                            @Query("user_id") String user_id,
                                                                            @Query("id") String id);

    //PlanOrder - 取消美人筹订单（冀）
    @GET("userPlanOrderCancel")
    Observable<ResponseBean> userPlanOrderCancel(@Query("token") String token,
                                                 @Query("user_id") String user_id,
                                                 @Query("id") String id);

    //PlanOrder - 发起人选择医院（冀）
    @GET("userPlanChoiceHospital")
    Observable<ResponseBean> userPlanChoiceHospital(@Query("token") String token,
                                                    @Query("user_id") String user_id,
                                                    @Query("id") String id,
                                                    @Query("hospital_id") String hospital_id);


    // Plan____ - 机构取消参与美人筹（冀）
    @GET("cancelJoinToPlan")
    Observable<ResponseBean> cancelJoinToPlan(@Query("token") String token,
                                              @Query("user_id") String user_id,
                                              @Query("plan_order_id") String plan_order_id);


    // Order - 确认使用（冀）
    @FormUrlEncoded
    @POST("orderConfirmUse")
    Observable<ResponseBean> orderConfirmUse(@Field("token") String token,
                                             @Field("user_id") String user_id,
                                             @Field("order_id") String order_id,
                                             @Field("type") String type,
                                             @Field("order_type") String order_type); //1普通订单,拼购,秒杀 2美人筹


    // Order - 验证订单 （冀）
    @FormUrlEncoded
    @POST("verificationOrder")
    Observable<ResponseBean<VerificationOrderBean>> verificationOrder(@Field("token") String token,
                                                                      @Field("user_id") String user_id,
                                                                      @Field("order_code") String order_code); //订单验证码
}