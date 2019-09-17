package com.chengzi.app.ui.goods.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.bean.SpellBean;

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

public interface GoodService {

    /**
     * 获取商品详情（冀）
     *
     * @param goodId
     */
    @GET("goodsDetails")
    Observable<ResponseBean<GoodDetailBean>> goodsDetails(@Query("goods_id") String goodId,
                                                          @Query("longitude") String longitude,
                                                          @Query("latitude") String latitude);

    /**
     * 确认下单（冀）
     */
    @FormUrlEncoded
    @POST("confirmOrder")
    Observable<ResponseBean<List<String>>> confirmOrder(@FieldMap Map<String, String> params);

    /**
     * 商品拼购列表（冀）
     *
     * @param goodsId
     * @param pageSize
     * @param page
     */
    @GET("goodsSpellList")
    Observable<ResponseBean<PageBean<SpellBean>>> goodsSpellList(@Query("goods_id") String goodsId,
                                                                 @Query("limit") String pageSize,
                                                                 @Query("page") String page);

    /**
     * 加入购物车（冀）
     *
     * @param params
     */
    @GET("addToCart")
    Observable<ResponseBean> addToCart(@QueryMap Map<String, String> params);

    /**
     * 获取商品库存（冀）
     *
     * @param goodsId
     * @return data:15     // 商品库存  -1 时不限库存 （秒杀商品，在秒杀时段会限制库存）
     */
    @GET("getGoodsStock")
    Observable<ResponseBean<Integer>> getGoodsStock(@Query("goods_id") String goodsId);


    /**
     * 扣除商品专区推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutZone")
    Observable<ResponseBean> promotionCutZone(@Field("id") String id,
                                              @Field("uid") String userId,
                                              @Field("code")String code);

    /**
     * 扣除商品搜索推广的费用（黄）
     */
    @FormUrlEncoded
    @POST("promotionCutProductSearch")
    Observable<ResponseBean> promotionCutProductSearch(@Field("id") String id,
                                                       @Field("uid") String userId,
                                                       @Field("code")String code);

}
