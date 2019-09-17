package com.chengzi.app.ui.home.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.AdvListBean;
import com.chengzi.app.ui.home.bean.AdvertisingBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.GetCustomerServiceStaffBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HomeArticleBean;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;
import com.chengzi.app.ui.home.bean.NormalBean;
import com.chengzi.app.ui.home.bean.TradeListBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HomeService {

    /**
     * 首页App导航 VIP专区分类、普通分类(小芸)
     */
//    @FormUrlEncoded
//    @POST("categoryList")
//    Observable<ResponseBean<NavigatorListBean>> categoryList(@Field("token") String token);

    /**
     * Advertisement - 广告(小芸)
     */
    @FormUrlEncoded
    @POST("advertisement")
    Observable<ResponseBean<AdvertisingBean.AdvertisingHolderBean>> advertisement(@Field("place") String place);

    /**
     * Home - 首页-商品列表(小芸)
     */
    @GET("homepageGoodsList")
    Observable<ResponseBean<List<HomeRecommandBean<GoodBean>>>> homeRecommandList(@QueryMap Map<String, String> params);

    /**
     * Home - 首页-猜你喜欢列表(小芸)
     */
    @GET("likeGoodsList")
    Observable<ResponseBean<List<GoodBean>>> likeGoodsList(@QueryMap Map<String, String> params);

    /**
     * Home - 普通频道-某个专区列表(小芸)
     *
     * @param params
     * @return
     */
    @GET("specialList")
    Observable<ResponseBean<NormalBean>> specialList(@QueryMap Map<String, String> params);

    /**
     * Home - 首页- 广告banner图
     */
    @GET("positionList")
    Observable<ResponseBean<List<AdvListBean>>> positionList(@Query("city_id") String city_id,
                                                             @Query("cate_id") String cate_id);

    /**
     * Home - 首页- 交易动态列表
     */
    @GET("tradeList")
    Observable<ResponseBean<List<TradeListBean>>> tradeList();

    /**
     * Home - 首页- 学术动态列表
     */
    @GET("homeArticle")
    Observable<ResponseBean<List<HomeArticleBean>>> homeArticle();

    /**
     * 用户切换城市
     *
     * @param token
     * @param userId
     * @param cityId
     */
    @GET("changeCity")
    Observable<ResponseBean> changeCity(@Query("token") String token,
                                        @Query("user_id") String userId,
                                        @Query("city_id") String cityId);

    /**
     * 扣除医生搜索推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutBanner")
    Observable<ResponseBean> promotionCutBanner(@Field("id") String id,
                                                @Field("uid") String userId,
                                                @Field("code")String code);

    /**
     * 首页普通频道
     * @return
     */
    @GET("homeCategoryList")
    Observable<ResponseBean<List<CategoryItem>>> homeCategoryList();

    /**
     *联系网站获取客服
     * @return
     */
    @GET("getCustomerServiceStaff")
    Observable<ResponseBean<GetCustomerServiceStaffBean>> getCustomerServiceStaff();

}
