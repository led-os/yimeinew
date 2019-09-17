package com.chengzi.app.ui.seckill.model;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.seckill.bean.CountDownBean;
import com.chengzi.app.ui.seckill.bean.KillGoodsListBean;
import com.chengzi.app.ui.seckill.bean.KillListBean;
import com.chengzi.app.ui.seckill.bean.KillTimeBean;

import java.util.List;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SeckillService {

    /**
     * Home - 首页- 秒杀商品列表
     */
    @GET("killGoodsList")
    Observable<ResponseBean<KillGoodsListBean>> killGoodsList();

    /**
     * Reuse - 秒杀时间段（阿布）
     */
    @GET("killTime")
    Observable<ResponseBean<List<KillTimeBean>>> killTime();

    /**
     * Reuse - 秒杀倒计时(复)3.1（阿布）
     */
    @GET("countDown")
    Observable<ResponseBean<CountDownBean>> countDown();

    /**
     * kill - 秒杀中3.18（阿布）  列表+倒计时
     *
     * @param times       正点时间  (10,12,14..)
     * @param category_id 分类id
     * @param page        当前页
     * @param limit       每页的条数
     * @return
     */
    @GET("killIndex")
    Observable<ResponseBean<KillListBean>> killIndex(@Query("token") String token,
                                                     @Query("user_id") String user_id,
                                                     @Query("times") String times,
                                                     @Query("category_id") String category_id,
                                                     @Query("page") String page,
                                                     @Query("limit") String limit);

    /**
     * kill - 提醒3.18.2（布）
     *
     * @param plan_order_id 秒杀活动id
     */
    @GET("killRemind")
    Observable<ResponseBean> killRemind(@Query("token") String token,
                                        @Query("user_id") String user_id,
                                        @Query("plan_order_id") String plan_order_id);
}