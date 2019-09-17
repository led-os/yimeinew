package com.chengzi.app.ui.homepage.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.bean.CasesCategoryByHospitalBean;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HospitalHomeService {

    /**
     * 我的（医院）-我的主页（sys/黄）
     */
    @GET("hospitalIndex")
    Observable<ResponseBean<HospitalHomeInfoBean>> hospitalDetailInfo(@Query("uid") String userId,
                                                                      @Query("click_id") String targetId);

    /**
     * 机构主页下医生/咨询师列表（小芸）
     */
    @GET("bindPeople")
    Observable<ResponseBean<PageBean<DoctorBean>>> bindedDoctor(@Query("user_id") String hospitalId,
                                                                @Query("user_type") String userType,
                                                                @Query("limit") String pageSize,
                                                                @Query("page") String page);


//    //CaseNote - 案例管理--案例展示
//    @GET("caseList")
//    Observable<ResponseBean<PageBean<CaseBean>>> hospitalCaseList(@Query("token") String token,
//                                                                  @Query("uid") String uid,
//                                                                  @Query("user_id") String user_id,
//                                                                  @Query("page") String page,
//                                                                  @Query("limit") String limit);

    /**
     * 扣除医生搜索推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutHospitalSearch")
    Observable<ResponseBean> promotionCutHospitalSearch(@Field("id") String id,
                                                        @Field("uid") String userId,
                                                        @Field("code")String code);


    //CaseNote - (3.15.3)医院案例列表-查医院已有案例的分类集合（非所有分类集合）
    @GET("casesCategoryByHospital")
    Observable<ResponseBean<List<CasesCategoryByHospitalBean>>> casesCategoryByHospital(@Query("hospital_id") String hospital_id);


    //CaseNote - 案例管理--案例展示
    @GET("caseList")
    Observable<ResponseBean<PageBean<CaseBean>>> hospitalCaseList(@QueryMap Map<String, String> params);

    //Visitcount - 记录用户访问四大类的数据（黄）
    //uid	Number    【必填】当前访问的用户ID
    //id	Number    【必填】当前访问的对象ID（医生ID/咨询师ID/医院ID/商品ID等）
    //type	Number    【必填】访问对象类型，1/咨询师；2/医生；3/机构；4/商品。
    //GET /addVisit
    @GET("addVisit")
    Observable<ResponseBean> addVisit(@Query("token") String token,
                                      @Query("uid") String uid,
                                      @Query("user_id") String user_id,
                                      @Query("id") String id,
                                      @Query("type") String type);
}
