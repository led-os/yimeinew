package com.chengzi.app.ui.search.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.search.bean.SearchBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface SearchService {

    /**
     * 首页-搜索历史(小芸)
     *
     * @param userId
     * @param token
     */
    @GET("searchHistory")
    Observable<ResponseBean<List<SearchBean>>> searchHistory(@Query("user_id") String userId, @Query("token") String token);


    /**
     * 首页-搜索关键字入库(小芸)
     *
     * @param userId
     * @param keyword
     */
    @FormUrlEncoded
    @POST("searchKey")
    Observable<ResponseBean> searchKey(@Field("user_id") String userId, @Field("key") String keyword);


    /**
     * 首页-热搜列表(小芸)
     */
    @GET("searchHot")
    Observable<ResponseBean<List<SearchBean>>> hotSearch();


    /**
     * 首页-搜索历史删除(小芸)
     *
     * @param usreId
     * @param token
     */
    @FormUrlEncoded
    @POST("searchHistoryDel")
    Observable<ResponseBean> delHistorySearch(@Field("user_id") String usreId, @Field("token") String token);


    //Goods - 商品分类(syx)
    @GET("goodsCatrgory")
    Observable<ResponseBean<List<CategoryItem>>> goodsCatrgory();

    /**
     * 首页-搜索商品结果(小芸) 完成
     */
    @GET("searchGoodsList")
    Observable<ResponseBean<PageBean<GoodBean>>> searchGoods(@QueryMap Map<String, String> params);


    /**
     * 首页-搜索医生结果(小芸) 完成
     */
    @GET("searchDoctorList")
    Observable<ResponseBean<PageBean<DoctorBean>>> searchDoctor(@QueryMap Map<String,String> params);


    /**
     * 首页-搜索咨询师结果(小芸) 完成
     */
    @GET("searchConsultantList")
    Observable<ResponseBean<PageBean<DoctorBean>>> searchConsultant(@QueryMap Map<String,String> params);


    /**
     * 首页-搜索机构结果（小芸）
     * @param params
     */
    @GET("searchHospitlList")
    Observable<ResponseBean<PageBean<HospitalBean>>> searchOrg(@QueryMap Map<String,String> params);

    /**
     *  首页-搜索案例结果(小芸) 完成
     */
    @GET("searchCaseList")
    Observable<ResponseBean<PageBean<CaseBean>>> searchCase(@QueryMap Map<String,String> params);
}