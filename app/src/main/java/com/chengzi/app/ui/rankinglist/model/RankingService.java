package com.chengzi.app.ui.rankinglist.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.rankinglist.bean.SearchRankingBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 排行榜
 *
 * @ClassName:RankingService
 * @PackageName:com.yimei.app.ui.rankinglist.model
 * @Create On 2019/5/10 0010   10:50
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/10 0010 handongkeji All rights reserved.
 */
public interface RankingService {


    /**
     * 排行榜 --医生/ 咨询师
     */
    @GET("rankinglist")
    Observable<ResponseBean<PageBean<DoctorBean>>> rankingDoctor(@Query("type") String type,
                                                                 @QueryMap Map<String, String> params);
    /**
     * 排行榜 --商品
     */
    @GET("rankinglist")
    Observable<ResponseBean<PageBean<GoodBean>>> rankingGoods(@Query("type") String type,
                                                              @QueryMap Map<String, String> params);
    /**
     * 排行榜 --机构
     */
    @GET("rankinglist")
    Observable<ResponseBean<PageBean<HospitalBean>>> rankingOrg(@Query("type") String type,
                                                                @QueryMap Map<String, String> params);

    /**
     * 排行榜 --案例
     */
    @GET("rankinglist")
    Observable<ResponseBean<PageBean<CaseBean>>> rankingCase(@Query("type") String type,
                                                             @QueryMap Map<String, String> params);

    /**
     * 排行榜 --搜索
     */
    @GET("rankinglist")
    Observable<ResponseBean<PageBean<SearchRankingBean>>> rankingSearch(@Query("type") String type,
                                                                        @QueryMap Map<String, String> params);
}
