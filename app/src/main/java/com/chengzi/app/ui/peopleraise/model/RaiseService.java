package com.chengzi.app.ui.peopleraise.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.peopleraise.bean.PostRaiseBean;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.bean.SubmitOrderBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RaiseService {


    /**
     * 美人筹 列表/搜索（阿布）
     *
     * @param params
     */
    @GET("planList")
    Observable<ResponseBean<PageBean<RaiseBean>>> planList(@QueryMap Map<String, String> params);


    /**
     * 美人筹详情（布）
     *
     * @param params
     */
    @GET("planDetails")
    Observable<ResponseBean<RaiseBean>> planDetails(@QueryMap Map<String, String> params);

    /**
     * 美人筹详情推荐订单列表 （阿布）
     *
     * @param categoryId
     */
    @GET("planRecommendList")
    Observable<ResponseBean<List<RaiseBean>>> planRecommendList(@Query("category_id") String categoryId,
                                                                @Query("plan_order_id") String plan_order_id);


    /**
     * 美人筹评论/复用列表页 （阿布）
     *
     * @param planOrderId
     */
    @GET("planCommentList")
    Observable<ResponseBean<PageBean<CommentBean>>> planCommentList(@Query("plan_order_id") String planOrderId,
                                                                    @Query("limit") String pageSize,
                                                                    @Query("page") String page);

    /**
     * 美人筹我要评论（布）
     *
     * @return
     */
    @FormUrlEncoded
    @POST("planCommentAdd")
    Observable<ResponseBean> planCommentAdd(@FieldMap Map<String, String> params);

    /**
     * 美人筹发布（阿布）
     *
     * @param params s
     */
    @FormUrlEncoded
    @POST("planAdd")
    Observable<ResponseBean<PostRaiseBean>> planAdd(@FieldMap Map<String, String> params);


    /**
     * 美人筹我要参与（冀）
     *
     * @param token  token （必须）
     * @param userId 用户id （必须）
     * @param planId 美人筹订单id （必须）
     */
    @GET("joinToPlan")
    Observable<ResponseBean> joinToPlan(@Query("token") String token,
                                        @Query("user_id") String userId,
                                        @Query("plan_order_id") String planId);

    /**
     * 美人筹确认订单3.8.6（布）
     *
     * @param token  （必须）
     * @param userId 用户id （必）
     * @param planId 美人筹订单 （必）
     * @return
     */
    @GET("planOrder")
    Observable<ResponseBean<RaiseBean>> planOrder(@Query("token") String token,
                                                  @Query("user_id") String userId,
                                                  @Query("plan_order_id") String planId);

    /**
     * 美人筹参与下单 （冀）
     */
    @FormUrlEncoded
    @POST("planSubmitOrder")
    Observable<ResponseBean<SubmitOrderBean>> planSubmitOrder(@Field("token") String token,
                                                              @Field("user_id") String userId,
                                                              @Field("plan_order_id") String planId);

    @GET("planCategory")
    Observable<ResponseBean<List<CategoryItem>>> planCategory();
}
