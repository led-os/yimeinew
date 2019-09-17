package com.chengzi.app.ui.mine.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.AppointmentTimeManageBean;
import com.chengzi.app.ui.mine.bean.BindHistoryBean;
import com.chengzi.app.ui.mine.bean.BindSearchBean;
import com.chengzi.app.ui.mine.bean.ExtensionRankBean;
import com.chengzi.app.ui.mine.bean.PromoteRoutingBean;
import com.chengzi.app.ui.mine.bean.SheetListBean;
import com.chengzi.app.ui.mine.bean.SheetStatusBean;
import com.chengzi.app.ui.mine.bean.TimeManageBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MineDoctorOrCounselorService {


    //Sheet_____ - 医生/咨询师抢单、医生在线面诊列表(小芸) 3.11
    @FormUrlEncoded
    @POST("sheetList")
    Observable<ResponseBean<List<SheetListBean>>> sheetList(@Field("token") String token,
                                                            @Field("user_id") String user_id,
                                                            @Field("sheet_type") String sheet_type);

    //Sheet__________ - 咨询师/医生抢单操作(小芸)
    @FormUrlEncoded
    @POST("sheetJoin")
    Observable<ResponseBean<String>> sheetJoin(@Field("token") String token,
                                       @Field("user_id") String user_id,
                                       @Field("user_type") String user_type,
                                       @Field("sheet_id") String sheet_id,
                                       @Field("sheet_type") String sheet_type);

    //Sheet__________ - 咨询师/医生抢单操作(小芸)-->抢单后刷新->每秒刷新 是否抢单后 客户选中/未选中
    @FormUrlEncoded
    @POST("sheetStatus")
    Observable<ResponseBean<SheetStatusBean>> sheetStatus(@Field("token") String token,
                                                          @Field("user_id") String user_id,
                                                          @Field("sheet_id") String sheet_id);

    //DoctorHomePage - 我的预约管理“医生”8.11 {修改同一个接口}(李金泽) -->获取
    @GET("AppointmentTimeManage")
    Observable<ResponseBean<List<AppointmentTimeManageBean>>> AppointmentTimeManage(@Query("uid") String uid,
                                                                                    @Query("start_time") String startTime,
                                                                                    @Query("end_time") String endTime);

    //DoctorHomePage - 我的预约管理“医生”8.11 {修改同一个接口}(李金泽) -->修改 可预约时间
    @FormUrlEncoded
    @POST("AppointmentTimeManage")
    Observable<ResponseBean<TimeManageBean>> AppointmentTimeManage(@Field("user_id") String user_id,
                                                                   @Field("uid") String uid,
                                                                   @Field("date") String date,
                                                                   @Field("day") String day);

    //医生/咨询师 绑定-->绑定历史
    @FormUrlEncoded
    @POST("bindHistory")
    Observable<ResponseBean<PageBean<BindHistoryBean>>> bindHistory(@Field("token") String token,
                                                                    @Field("user_id") String user_id,
                                                                    @Field("page") String page,
                                                                    @Field("limit") String limit);

    //医生/咨询师 绑定-->当前绑定
    @FormUrlEncoded
    @POST("bindHospital")
    Observable<ResponseBean<PageBean<BindHistoryBean>>> bindHospital(@Field("token") String token,
                                                                     @Field("user_id") String user_id);

    //Bind - 绑定机构(小芸)
    @FormUrlEncoded
    @POST("bind")
    Observable<ResponseBean> bind(@Field("token") String token,
                                  @Field("user_id") String user_id,
                                  @Field("binding_id") String binding_id,
                                  @Field("user_type") String user_type);

    //Bind - 解除绑定(小芸)
    @FormUrlEncoded
    @POST("bindClear")
    Observable<ResponseBean> bindClear(@Field("token") String token,
                                       @Field("user_id") String user_id,
                                       @Field("binding_id") String binding_id,
                                       @Field("user_type") String user_type);

    //Bind - 绑定机构-查询机构(小芸)
    @GET("bindSearch")
    Observable<ResponseBean<List<BindSearchBean>>> bindSearch(@Query("token") String token,
                                                              @Query("user_id") String user_id,
                                                              @Query("name") String name);

    //Consultant - 我要推荐
    @GET("promoteRouting")
    Observable<ResponseBean<PromoteRoutingBean>> promoteRouting(@Query("token") String token,
                                                                @Query("user_id") String user_id);

    //Consultant - 咨询师推广排行
    @GET("extensionRank")
    Observable<ResponseBean<List<ExtensionRankBean>>> extensionRank(@Query("token") String token,
                                                                    @Query("user_id") String user_id,
                                                                    @Query("city_id") String city_id);

    @FormUrlEncoded
    @POST("delSheetChoseList")
    Observable<ResponseBean> delSheetChoseList(@Field("sheetId") String sheetId);

}