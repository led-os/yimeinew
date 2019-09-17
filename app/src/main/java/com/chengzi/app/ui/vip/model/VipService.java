package com.chengzi.app.ui.vip.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.vip.bean.VipBannerListBean;
import com.chengzi.app.ui.vip.bean.VipChannelHomePageBean;
import com.chengzi.app.ui.vip.bean.VipChannelIndexBean;
import com.chengzi.app.ui.vip.bean.VipNormalChannelBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VipService {

    /**
     * VIP - (5.1)获取VIP分类列表
     */
    @GET("getVipCategory")
    Observable<ResponseBean<List<CategoryItem>>> getVipCategory(@Query("token") String token);

    /**
     * VIP - (5.1)获取头部Banner图列表(黄)
     */
    @GET("getVipBanner")
    Observable<ResponseBean<List<VipBannerListBean>>> getVipBanner(@Query("token") String token,
                                                                   @Query("cate_id") String cate_id,
                                                                   @Query("city_id") String city_id);

    /**
     * VIP频道第一页的页面信息（大合集，返回整页的信息）(黄)
     *
     * @param userId 当前登录的用户id
     * @return
     */
    @GET("vipChannelHomepage")
    Observable<ResponseBean<VipChannelHomePageBean>> vipChannelHomepage(@Query("uid") String userId);

    /**
     * VIP - (5.1)获取VIP频道首页基本信息(刚进入时)(黄)
     * (用户信息)
     *
     * @param uid 当前登录的用户id
     * @return
     */
    @GET("vipChannelIndex")
    Observable<ResponseBean<VipChannelIndexBean>> vipChannelIndex(@Query("uid") String uid,
                                                                  @Query("city_id") String city_id);

    /**
     * VIP频道分类子页数据（大合集，返回整页的信息）(黄)
     *
     * @param categoryId 【必填】分类ID
     * @param cityId     必填】城市ID
     * @return
     */
    @GET("vipChannelCategory")
    Observable<ResponseBean<VipNormalChannelBean>> getVipNormalChannel(@Query("cate_id") String categoryId,
                                                                       @Query("city_id") String cityId);

}
