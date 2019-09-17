package com.chengzi.app.ui.account.model;

import com.chengzi.app.ui.account.bean.AuditInfoBean;
import com.chengzi.app.ui.account.bean.HospitalCertifiedBean;
import com.chengzi.app.ui.account.bean.ThirdLoginBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.bean.PromotionAgreementBean;
import com.handong.framework.base.ResponseBean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountService {

    //注册 密码会有特殊字符
    @FormUrlEncoded
    @POST("register")
    Observable<ResponseBean<UserInfoBean>> register(@FieldMap Map<String, String> params);

    //TEXT - PC获取用户使用协议和规则详情（冀）
    @GET("getAgreementDetails")
    Observable<ResponseBean<PromotionAgreementBean>> getAgreementDetails(@Query("user_type") String user_type,
                                                                         @Query("info_type") String info_type);

    //登录 密码会有特殊字符
    @FormUrlEncoded
    @POST("login")
    Observable<ResponseBean<UserInfoBean>> login(@Field("mobile") String mobile,
                                                 @Field("password") String password,
                                                 @Field("c_type") String c_type,
                                                 @Field("device_id") String device_id);

    @FormUrlEncoded //android-1
    @POST("sendCode")
    Observable<ResponseBean> sendCode(@Field("mobile") String mobile,
                                      @Field("c_type") String c_type,
                                      @Field("device_id") String device_id);

    //重置/找回密码
    @FormUrlEncoded
    @POST("retrievePassword")
    Observable<ResponseBean> retrievePassword(@Field("mobile") String mobile,
                                              @Field("password") String password,
                                              @Field("repassword") String repassword,
                                              @Field("m_code") String m_code,
                                              @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("upload")
    Observable<ResponseBean<List<String>>> upload(@Field("file[]") List<File> file);


    /**
     * 三方登录
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/otherLogin")
    Observable<ThirdLoginBean> thirdLogin(@FieldMap HashMap<String, String> params);

    /**
     * Member - 微信绑定 阿布
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/wxBind")
    Observable<ResponseBean<UserInfoBean>> wxBind(@FieldMap HashMap<String, String> params);

    //    /**
//     * 咨询师认证 / 咨询师重新认证
//     *
//     * @param params
//     * @return
//     */
////    @FormUrlEncoded
////    @POST("counselorApprove")
////    Observable<ResponseBean> counselorApprove(@FieldMap HashMap<String, String> params);
//
////    @FormUrlEncoded
////    @POST("counselorRestart")
////    Observable<ResponseBean> counselorRestart(@FieldMap HashMap<String, String> params);
//
//    /**
//     * 医生认证 / 医生重新认证
//     *
//     * @param params
//     * @return
//     */
////    @FormUrlEncoded
////    @POST("doctorApprove")
////    Observable<ResponseBean> doctorApprove(@FieldMap HashMap<String, String> params);
//
////    @FormUrlEncoded
////    @POST("doctorRestart")
////    Observable<ResponseBean> doctorRestart(@FieldMap HashMap<String, String> params);
//
//    /**
//     * 医院认证 / 医院重新认证
//     *
//     * @param params
//     * @return
//     */
////    @FormUrlEncoded
////    @POST("hospitalApprove")
////    Observable<ResponseBean> hospitalApprove(@FieldMap HashMap<String, String> params);
////
////    @FormUrlEncoded
////    @POST("hospitalRestart")
////    Observable<ResponseBean> hospitalRestart(@FieldMap HashMap<String, String> params);
//
//

    /**
     * 医生/咨询师/医院 的 认证/重新认证
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("user-audit")
    Observable<ResponseBean> user_audit(@FieldMap HashMap<String, String> params);


    @GET("audit-info")
    Observable<ResponseBean<AuditInfoBean>> getAuditInfo();

    @GET("Authentication")
    Observable<ResponseBean<HospitalCertifiedBean>> authentication(@Query("token") String token,
                                                                   @Query("user_id") String user_id);
}