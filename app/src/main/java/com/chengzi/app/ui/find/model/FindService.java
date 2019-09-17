package com.chengzi.app.ui.find.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.find.bean.ArticleManageBean;
import com.chengzi.app.ui.find.bean.CreditSearchDetailBean;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HospitalBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FindService {

    /**
     * 找  案例
     *
     * @param type goods|doctor|consultant|service_organization|history_cases
     * @return
     */
    @GET("recommendProduct")
    Observable<ResponseBean<List<FindBaseBean<CaseBean>>>> findCase(@Query("type") String type,
                                                                    @Query("user_id")String userId);

    /**
     * 找  商品
     *
     * @param type goods|doctor|consultant|service_organization|history_cases
     * @return
     */
    @GET("recommendProduct")
    Observable<ResponseBean<List<FindBaseBean<GoodBean>>>> findGoods(@Query("type") String type);

    /**
     * 找  医生
     *
     * @param type goods|doctor|consultant|service_organization|history_cases
     * @return
     */
    @GET("recommendProduct")
    Observable<ResponseBean<List<FindBaseBean<DoctorBean>>>> findDoctor(@Query("type") String type);

    /**
     * 找  咨询师
     *
     * @param type goods|doctor|consultant|service_organization|history_cases
     * @return
     */
    @GET("recommendProduct")
    Observable<ResponseBean<List<FindBaseBean<DoctorBean>>>> findCounselor(@Query("type") String type);


    /**
     * 找  机构
     *
     * @param type goods|doctor|consultant|service_organization|history_cases
     * @return
     */
    @GET("recommendProduct")
    Observable<ResponseBean<List<FindBaseBean<HospitalBean>>>> findHospital(@Query("type") String type);


    /**
     * Article - 首页-三方文章列表+详情(小芸)
     *
     * @param type // 文章类型 1-人才招聘 2-行业会议 3-培训发布
     * @return
     */
    @GET("articleManage")
    Observable<ResponseBean<PageBean<ArticleManageBean>>> articleManage(@Query("page") String page,
                                                                        @Query("limit") String limit,
                                                                        @Query("type") String type);

    /**
     * Credit - 首页-信用查询列表(小芸)
     *
     * @param key // 关键字（必传）
     * @return
     */
    @GET("creditSearchList")
    Observable<ResponseBean<List<CreditSearchDetailBean>>> creditSearchList(@Query("token") String token,
                                                                      @Query("key") String key);

    /**
     * Credit - 首页-信用查询详情(小芸)
     *
     * @param search_id // 被搜索人id（必传）
     * @return
     */
    @GET("creditSearchDetail")
    Observable<ResponseBean<CreditSearchDetailBean>> creditSearchDetail(@Query("token") String token,
                                                                        @Query("search_id") String search_id);


}
