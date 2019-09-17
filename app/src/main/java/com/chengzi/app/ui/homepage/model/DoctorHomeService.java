package com.chengzi.app.ui.homepage.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.homepage.bean.ChooseAppointmentBean;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.bean.EvaluateBean;
import com.chengzi.app.ui.homepage.bean.RelationCategoryBean;
import com.chengzi.app.ui.homepage.bean.UserRecordListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DoctorHomeService {

    /**
     * 医生/咨询师 主页 的信息   我的“医生”(李金泽)
     *
     * @param userId
     */
    @GET("doctorHomePage")
    Observable<ResponseBean<DoctorHomeInfoBean>> doctorHomePage(@Query("uid") String userId,
                                                                @Query("click_id") String targetId,
                                                                @Query("longitude") String longitude,
                                                                @Query("latitude") String latitude);

    /**
     * 我的全部评价“医生”8.2.3(李金泽)
     *
     * @param type       必传(1 全部评价| 2 好评| 3 有图| 4 效果显著| 5 优质服务)
     * @param targetId   目标id，医生id/ 咨询师id/ 商品id/ 医院id
     * @param targetType doctor|consultant|goods|serviceOrganization
     * @param pageSize
     * @param page
     * @return
     */
    @GET("DoctorEvaluate")
    Observable<ResponseBean<PageBean<EvaluateBean>>> evaluateList(@Query("type") String type,
                                                                  @Query("to_id") String targetId,
                                                                  @Query("query_range") String targetType,
                                                                  @Query("page_size") String pageSize,
                                                                  @Query("page") String page);

    /**
     * 我的商品“医生”8.2(李金泽)
     */
    @GET("relationGoods")
    Observable<ResponseBean<PageBean<GoodBean>>> relationGoods(@Query("uid") String userId,
                                                               @Query("category_id") String categoryId,
                                                               @Query("page_size") String pageSize,
                                                               @Query("page") String page);

    @GET("relationGoodsCategorys")
    Observable<ResponseBean<List<RelationCategoryBean>>> relationGoodsCategorys(@Query("uid") String userId);

    /**
     * 医生/咨询师 主页 推荐的 医生/咨询师
     *
     * @param uerType
     */
    @GET("recommendUsers")
    Observable<ResponseBean<List<DoctorBean>>> recommendUsers(@Query("user_type") String uerType);

    /**
     * 预约医生/机构
     *
     * @param params
     */
    @FormUrlEncoded
    @POST("chooseAppointment")
    Observable<ResponseBean<ChooseAppointmentBean>> postAppointment(@FieldMap Map<String, String> params);

    /**
     * UserRecord - 履历列表(小芸)
     */
    @FormUrlEncoded
    @POST("UserRecordList")
    Observable<ResponseBean<PageBean<UserRecordListBean>>> userRecordList(@Field("token") String token,
                                                                          @Field("user_id") String user_id,
                                                                          @Field("page") String page,
                                                                          @Field("limit") String limit);

    /**
     * UserRecord - 添加履历(小芸)
     */
    @FormUrlEncoded
    @POST("UserRecordAdd")
    Observable<ResponseBean<String>> userRecordAdd(@Field("token") String token,
                                                   @Field("user_id") String user_id,
                                                   @Field("start_time") String start_time,
                                                   @Field("end_time") String end_time,
                                                   @Field("content") String content,
                                                   @Field("image[]") List<String> image);

    /**
     * 扣除医生搜索推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutDoctorSearch")
    Observable<ResponseBean> promotionCutDoctorSearch(@Field("id") String id,
                                                      @Field("uid") String userId,
                                                      @Field("code")String code);

    /**
     * 扣除咨询师搜索推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutConsultantSearch")
    Observable<ResponseBean> promotionCutConsultantSearch(@Field("id") String id,
                                                          @Field("uid") String userId,
                                                          @Field("code")String code);

}
