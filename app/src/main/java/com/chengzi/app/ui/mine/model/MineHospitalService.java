package com.chengzi.app.ui.mine.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.mine.bean.BannerPromotionListBean;
import com.chengzi.app.ui.mine.bean.CaseManageListBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.mine.bean.CompetitiveAnalysisBean;
import com.chengzi.app.ui.mine.bean.DataAnalysisBean;
import com.chengzi.app.ui.mine.bean.DoctorListBean;
import com.chengzi.app.ui.mine.bean.DoctorPromotionBean;
import com.chengzi.app.ui.mine.bean.GoodsListManageBean;
import com.chengzi.app.ui.mine.bean.HospitalDoctorAppointmentBean;
import com.chengzi.app.ui.mine.bean.HospitalPromotionBean;
import com.chengzi.app.ui.mine.bean.KillHospitalListBean;
import com.chengzi.app.ui.mine.bean.LookComplaintBean;
import com.chengzi.app.ui.mine.bean.LookHospitalOrderBean;
import com.chengzi.app.ui.mine.bean.LookReviewBean;
import com.chengzi.app.ui.mine.bean.LookSheetNumBean;
import com.chengzi.app.ui.mine.bean.ProductPromotionProductBean;
import com.chengzi.app.ui.mine.bean.PromotionAgreementBean;
import com.chengzi.app.ui.mine.bean.PromotionInfoBean;
import com.chengzi.app.ui.mine.bean.PromotionSelectCategoryBean;
import com.chengzi.app.ui.mine.bean.PromotionSelectProductBean;
import com.chengzi.app.ui.mine.bean.SpreadsOrderBean;
import com.chengzi.app.ui.mine.bean.ZonePromotionBean;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * @Desc:
 * @ClassName:MineHospitalService
 * @PackageName:com.yimei.app.ui.mine.model
 * @Create On 2019/4/28 0028
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public interface MineHospitalService {

    //医生/咨询师 列表
    @FormUrlEncoded
    @POST("doctorList")
    Observable<ResponseBean<PageBean<DoctorListBean>>> doctorList(@FieldMap HashMap<String, String> params);

    // 医生/咨询师 -->接受/拒绝
    @FormUrlEncoded
    @POST("bindReview")
    Observable<ResponseBean> bindReview(@Field("token") String token,
                                        @Field("user_id") String user_id,
                                        @Field("user_type") String user_type,
                                        @Field("binding_id") String binding_id,
                                        @Field("status") String status);

    //Bind - 解除绑定(小芸)
    @FormUrlEncoded
    @POST("bindClear")
    Observable<ResponseBean> bindClear(@Field("token") String token,
                                       @Field("user_id") String user_id,
                                       @Field("binding_id") String binding_id,
                                       @Field("user_type") String user_type);

    /**
     * GoodsGL - 商品管理-列表（冀）
     */
    @GET("goodsListGuanLi")
    Observable<ResponseBean<PageBean<GoodsListManageBean>>> goodsListGuanLi(@QueryMap HashMap<String, String> params);

    /**
     * GoodsGL - 商品管理-设特色（冀）
     */
    @GET("goodsChangeFeature")
    Observable<ResponseBean> goodsChangeFeature(@Query("token") String token,
                                                @Query("user_id") String user_id,
                                                @Query("uid") String uid,
                                                @Query("goods_id") String goods_id);

    /**
     * GoodsGL - 商品管理-列表（冀）
     */
    @GET("goodsChangeStatus")
    Observable<ResponseBean> goodsChangeStatus(@Query("token") String token,
                                               @Query("user_id") String user_id,
                                               @Query("uid") String uid,
                                               @Query("goods_id") String goods_id);

    /**
     * User - 座机设置(syx)
     *
     * @return
     */
    @FormUrlEncoded
    @POST("phoneSetting")
    Observable<ResponseBean> phoneSetting(@Field("token") String token,
                                          @Field("uid") String uid,
                                          @Field("user_id") String user_id,
                                          @Field("telephone") String telephone);


    //CaseNote - 案例管理--案例展示
    @GET("caseList")
    Observable<ResponseBean<PageBean<CaseManageListBean>>> caseManagement(@Query("token") String token,
                                                                          @Query("uid") String uid,
                                                                          @Query("user_id") String user_id,
                                                                          @Query("clk_id") String clk_id,
                                                                          @Query("page") String page,
                                                                          @Query("limit") String limit);

    //CaseNote - 案例管理--案例添加
    @FormUrlEncoded
    @POST("caseCreate")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> caseCreate(@Field("uid") String user_id,
                                        @Field("create_time") String create_time,
                                        @Field("cate_id1") String cate_id1,
                                        @Field("cate_id2") String cate_id2,
                                        @Field("cate_id3") String cate_id3,
                                        @Field("contents") String contents,
                                        @Field("cases_img_url[]") List<String> cases_img_url);

    //CaseNote - 案例管理--美丽日记添加
    @FormUrlEncoded
    @POST("caseNoteCreate")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> caseNoteCreate(@Field("uid") String uid,
                                            @Field("cases_id") String cases_id,
                                            @Field("contents") String contents,
                                            @Field("image_url[]") List<String> image_url,
                                            @Field("create_time") String create_time);

    /**
     * 案例点赞
     *
     * @param casesId 【必填】案例Id
     * @param uid     【必填】用户ID
     * @return
     */
    @FormUrlEncoded
    @POST("caseClickLike")
    Observable<ResponseBean> caseThumbUp(@Field("cases_id") String casesId,
                                         @Field("user_id") String user_id,
                                         @Field("uid") String uid);


    //CaseNote - 案例管理--案例评论列表
    @GET("caseCommentList")
    Observable<ResponseBean<PageBean<CommentBean>>> caseCommentList(@Query("cases_id") String cases_id);

//    //CaseNote - 案例管理--案例详情
//    @FormUrlEncoded
//    @POST("casesDetail")
//    Observable<ResponseBean> casesDetail(@Field("case_note_id") String case_note_id);

    //CaseNote - 案例管理--美丽日记(案例详情-变美过程)
    @GET("caseNoteList")
    Observable<ResponseBean<PageBean<CaseNoteListBean>>> caseNoteList(@Query("token") String token,
                                                                      @Query("uid") String uid,
                                                                      @Query("cases_id") String cases_id,
                                                                      @Query("page") String page,
                                                                      @Query("limit") String limit);

    //CaseNote - 案例管理--评论添加
    @FormUrlEncoded
    @POST("caseComment")
    Observable<ResponseBean> caseComment(@Field("cases_id") String cases_id,
                                         @Field("from_uid") String from_uid,
                                         @Field("to_uid") String to_uid,
                                         @Field("pid") String pid,
                                         @Field("content") String content);

    //CaseNote - 案例管理--评论删除
    @FormUrlEncoded
    @POST("casesCommentDelete")
    Observable<ResponseBean> casesCommentDelete(@Field("uid") String uid,
                                                @Field("comment_id") String comment_id);

    /**
     * 我要推荐
     */
    //Promotion - 我的推广--主页(syx)
    @FormUrlEncoded
    @POST("promotionInfo")
    Observable<ResponseBean<PromotionInfoBean>> promotionInfo(@Field("token") String token,
                                                              @Field("uid") String uid,
                                                              @Field("user_id") String user_id);

    //Promotion - 我的搜索推广--医生列表(syx)
    @FormUrlEncoded
    @POST("doctorPromotion")
    Observable<ResponseBean<PageBean<DoctorPromotionBean>>> doctorPromotion(@Field("token") String token,
                                                                            @Field("uid") String uid,
                                                                            @Field("user_id") String user_id,
                                                                            @Field("page") String page,
                                                                            @Field("limit") String limit);

    //Promotion - 我的搜索推广--咨询师列表(syx)
    @FormUrlEncoded
    @POST("consultantPromotion")
    Observable<ResponseBean<PageBean<DoctorPromotionBean>>> consultantPromotion(@Field("token") String token,
                                                                                @Field("uid") String uid,
                                                                                @Field("user_id") String user_id,
                                                                                @Field("page") String page,
                                                                                @Field("limit") String limit);


    //Promotion - 我的推广--获取推广规则内容(syx)
    @GET("promotionAgreement")
    Observable<ResponseBean<PromotionAgreementBean>> promotionAgreement(@Query("token") String token,
                                                                        @Query("uid") String uid,
                                                                        @Query("user_id") String user_id);

    //Promotion - 我的推广--搜索推广-医院推广，查看医院信息(syx)（黄）
    @FormUrlEncoded
    @POST("hospitalPromotion")
    Observable<ResponseBean<HospitalPromotionBean>> hospitalPromotion(@Field("token") String token,
                                                                      @Field("uid") String uid,
                                                                      @Field("user_id") String user_id);

    //Promotion - 我的推广-设置搜索推广限额---(包括商品、医院等所有的)（黄）
    // ① 医院-> 开启推广(设置限额)
    @FormUrlEncoded
    @POST("setSearchPromotionAmount")
    Observable<ResponseBean> setSearchPromotionAmount(@Field("token") String token,
                                                      @Field("uid") String uid,
                                                      @Field("user_id") String user_id,
                                                      @Field("id") String id,
                                                      @Field("type") String type,
                                                      @Field("amount") String amount);

    //Promotion - 我的推广-添加搜索推广(包括商品、医院等所有的搜索推广)（黄）
    // ① 医院-> 开启推广
    @FormUrlEncoded
    @POST("addSearchPromotionItem")
    Observable<ResponseBean> addSearchPromotionItem(@Field("token") String token,
                                                    @Field("uid") String uid,
                                                    @Field("user_id") String user_id,
                                                    @Field("id") String id,          //要设置的对象的id，比如医生ID、商品ID
                                                    @Field("type") String type);//,      //类型 1-商品 2-医生 3-咨询师 4-医院 5-案例
    //  @Field("amount") String amount); //设置的限额数，不填则默认为0，不限额

    //Promotion - 我的推广-删除搜索推广(包括商品、医院等所有的搜索推广)（黄）
    // ① 医院-> 关闭推广
    @FormUrlEncoded
    @POST("removeSearchPromotionItem")
    Observable<ResponseBean> removeSearchPromotionItem(@Field("token") String token,
                                                       @Field("uid") String uid,
                                                       @Field("user_id") String user_id,
                                                       @Field("id") String id,          //要设置的对象的id，比如医生ID、商品ID
                                                       @Field("type") String type);     //类型 1-商品 2-医生 3-咨询师 4-医院 5-案例

    // Promotion - (10.16.9)我要推广 - 删除推广位（黄） 用于 我的 - 我要推广中，删除专区推广和Banner广告位推广
    @FormUrlEncoded
    @POST("removePromotion")
    Observable<ResponseBean> removePromotion(@Field("token") String token,
                                             @Field("uid") String uid,
                                             @Field("user_id") String user_id,
                                             @Field("id") String id);

    //Promotion - 我的推广--商品分类列表(syx)
    // ①商品推广  ②案例推广  ③广告推广(普通/VIP)  ④专区推广(普通/VIP)
    @FormUrlEncoded
    @POST("promotionSelectCategory")
    Observable<ResponseBean<List<PromotionSelectCategoryBean>>> promotionSelectCategory(@Field("token") String token,
                                                                                        @Field("uid") String uid,
                                                                                        @Field("user_id") String user_id,
                                                                                        @Field("zone_type") String zone_type);     //专区类型 （1.普通;2.VIP）


    //Promotion - 我的推广--商品搜索推广-我的商品列表页(syx)
    @FormUrlEncoded
    @POST("productPromotionProduct")
    Observable<ResponseBean<PageBean<ProductPromotionProductBean>>> productPromotionProduct(@Field("token") String token,
                                                                                            @Field("uid") String uid,
                                                                                            @Field("user_id") String user_id,
                                                                                            @Field("cate_id") String cate_id,
                                                                                            @Field("page") String page,
                                                                                            @Field("limit") String limit);

    //Promotion - 我的推广--商品搜索推广-我的商品列表页(syx)
    @FormUrlEncoded
    @POST("promotionSelectProduct")
    Observable<ResponseBean<List<PromotionSelectProductBean>>> promotionSelectProduct(@Field("token") String token,
                                                                                      @Field("uid") String uid,
                                                                                      @Field("user_id") String user_id,
                                                                                      @Field("cate_id") String cate_id,
                                                                                      @Field("pid") String pid,
                                                                                      @Field("page") String page,
                                                                                      @Field("limit") String limit);


    //Promotion - 我的搜索推广--案例列表(syx)
    // ①商品推广  ②案例推广  ③广告推广(普通/VIP)  ④专区推广(普通/VIP)
    @FormUrlEncoded
    @POST("casesPromotion")
    Observable<ResponseBean<PageBean<CaseManageListBean>>> casesPromotion(@Field("token") String token,
                                                                          @Field("uid") String uid,
                                                                          @Field("user_id") String user_id,
                                                                          @Field("cate_id") String cate_id,
                                                                          @Field("page") String page,
                                                                          @Field("limit") String limit);

    //Promotion - 我的推广-广告位推广分类列表(黄)
    @FormUrlEncoded
    @POST("bannerPromotionCategory")
    Observable<ResponseBean<List<PromotionSelectCategoryBean>>> bannerPromotionCategory(@Field("zone_type") String zone_type);

    //Promotion - 我的推广-广告位推广-广告位列表(黄)
    @FormUrlEncoded
    @POST("bannerPromotionList")
    Observable<ResponseBean<BannerPromotionListBean>> bannerPromotionList(@Field("token") String token,
                                                                          @Field("uid") String uid,
                                                                          @Field("user_id") String user_id,
                                                                          @Field("zone_type") String zone_type,
                                                                          @Field("cate_id") String cate_id);

    //Promotion - 我要推广 - 新增加推广（黄） 用于 我的 - 我要推广中，专区推广和Banner广告位推广
    //① 广告推广
    //② 专区推广
    @FormUrlEncoded
    @POST("addPromotion")
    Observable<ResponseBean> addPromotion(@Field("token") String token,
                                          @Field("uid") String uid,
                                          @Field("user_id") String user_id,
                                          @Field("c_type") String c_type,      // 类型 1-banner推广 2-商品推广
                                          @Field("cate_id") String cate_id,    // 专区（分类）ID
                                          @Field("zone_type") String zone_type,// 专区类型 （1.普通;2.VIP）
                                          @Field("model_id") String model_id,  // 模式ID，商品推广时是商品ID，banner推广时是医院ID
                                          @Field("amount") String amount,      // 预支付金额
                                          @Field("banner_img") String banner_img); // 封面图，当类型为 Banner推广时，该项必填

    @FormUrlEncoded
    @POST("addPromotion")
    Observable<ResponseBean> addPromotion(@Field("token") String token,
                                          @Field("uid") String uid,
                                          @Field("user_id") String user_id,
                                          @Field("c_type") String c_type,      // 类型 1-banner推广 2-商品推广
                                          @Field("cate_id") String cate_id,    // 专区（分类）ID
                                          @Field("zone_type") String zone_type,// 专区类型 （1.普通;2.VIP）
                                          @Field("model_id") String model_id,  // 模式ID，商品推广时是商品ID，banner推广时是医院ID
                                          @Field("banner_img") String banner_img); // 封面图，当类型为 Banner推广时，该项必填

    //Promotion - 专区推广-
    @FormUrlEncoded
    @POST("zonePromotion")
    Observable<ResponseBean<ZonePromotionBean>> zonePromotion(@Field("token") String token,
                                                              @Field("uid") String uid,
                                                              @Field("user_id") String user_id,
                                                              @Field("zone_type") String zone_type);// 专区类型 （1.普通;2.VIP）

    //Promotion - 我的推广--支付(syx)
    @FormUrlEncoded
    @POST("promotionCreateOrder")
    Observable<ResponseBean> promotionPayment(@Field("token") String token,
                                              @Field("uid") String uid,
                                              @Field("user_id") String user_id,
                                              @Field("money") String money,
                                              @Field("pay_mode") String pay_mode,   // 支付方式 1-支付宝 2-微信
                                              @Field("pay_type") String pay_type,   // 支付类型 1-VIP 2-推广费用 3-订单 4-美人筹
                                              @Field("type") String type);          // 4医院

    // Promotion - (10.16.1)我的推广-创建充值支付订单(黄)
    @FormUrlEncoded
    @POST("promotionCreateOrder")
    Observable<ResponseBean<SpreadsOrderBean>> promotionCreateOrder(@Field("token") String token,
                                                                    @Field("uid") String uid,
                                                                    @Field("user_id") String user_id,
                                                                    @Field("price") String money);

    //秒杀-->分类( 首页App导航 VIP专区分类、普通分类(小芸))
//    @POST("categoryList")
//    Observable<ResponseBean<NavigatorListBean>> categoryList();

    //秒杀-->列表
    @GET("killHospitalList")
    Observable<ResponseBean<PageBean<KillHospitalListBean>>> killHospitalList(@Query("token") String token,
                                                                              @Query("hospital_id") String hospital_id,
                                                                              @Query("category_id") String category_id,
                                                                              @Query("limit") String limit,
                                                                              @Query("page") String page);

    //预约管理(Appointment - 医生预约10.18-10.181)
    @GET("hospitalDoctorAppointment")
    Observable<ResponseBean<PageBean<HospitalDoctorAppointmentBean>>> hospitalDoctorAppointment(@Query("token") String token,
                                                                                                @Query("user_id") String user_id,
                                                                                                @Query("type") String type,
                                                                                                @Query("select") String select,
                                                                                                @Query("page") String page,
                                                                                                @Query("limit") String limit);

    //kill - 医院秒杀确认参与10.21.1（阿布）
    @FormUrlEncoded
    @POST("killAdd")
    Observable<ResponseBean> killAdd(@Field("token") String token,
                                     @Field("user_id") String user_id,
                                     @Field("category_id") String category_id,
                                     @Field("goods_id") String goods_id,
                                     @Field("time") String time,
                                     @Field("price") String price,
                                     @Field("num") String num);

    //kill - 取消秒杀
    @GET("killCancel")
    Observable<ResponseBean> killCancel(@Query("token") String token,
                                        @Query("user_id") String user_id,
                                        @Query("goods_id") String goods_id);

    //Look - lookOrder 查看下单(syx)
    @FormUrlEncoded
    @POST("lookHospitalOrder")
    Observable<ResponseBean<PageBean<LookHospitalOrderBean>>> lookHospitalOrder(@Field("token") String token,
                                                                                @Field("user_id") String user_id,
                                                                                @Field("uid") String uid,
                                                                                @Field("user_type") String user_type, //2医生 3咨询师
                                                                                @Field("type") String type, //2医生 3咨询师
                                                                                @Field("page") String page,
                                                                                @Field("limit") String limit);

    //Look - lookReview 查看评论(syx)
    @FormUrlEncoded
    @POST("lookHospitalComment")
    Observable<ResponseBean<PageBean<LookReviewBean>>> lookHospitalComment(@Field("token") String token,
                                                                           @Field("user_id") String user_id,
                                                                           @Field("uid") String uid,
                                                                           @Field("user_type") String user_type, //2医生 3咨询师
                                                                           @Field("type") String type, //2医生 3咨询师
                                                                           @Field("page") String page,
                                                                           @Field("limit") String limit);

    //Look - lookOrder 查看投诉(syx)
    @FormUrlEncoded
    @POST("lookComplaint")
    Observable<ResponseBean<PageBean<LookComplaintBean>>> lookComplaint(@Field("token") String token,
                                                                        @Field("user_id") String user_id,
                                                                        @Field("uid") String uid,
                                                                        @Field("user_type") String user_type, //2医生 3咨询师
                                                                        @Field("type") String type, //2医生 3咨询师
                                                                        @Field("page") String page,
                                                                        @Field("limit") String limit);

    //Look - 查看咨询量10.19.3 (小芸）
    @GET("lookSheetNum")
    Observable<ResponseBean<PageBean<LookSheetNumBean>>> lookSheetNum(@Query("token") String token,
                                                                      @Query("user_id") String user_id,
                                                                      @Query("user_type") String user_type, //2医生 3咨询师
                                                                      @Query("page") String page,
                                                                      @Query("limit") String limit);

    //HospitalDataAnalysis - (10.15)医院数据分析(syx)
    @GET("DataAnalysis")
    Observable<ResponseBean<DataAnalysisBean>> dataAnalysis(@Query("token") String token,
                                                            @Query("user_id") String user_id);

    //User - (10.22)数据竞对分析(syx)
    @FormUrlEncoded
    @POST("competitiveAnalysis")
    Observable<ResponseBean<CompetitiveAnalysisBean>> competitiveAnalysis(@Field("token") String token,
                                                                          @Field("uid") String uid,
                                                                          @Field("user_id") String user_id,
                                                                          @Field("type") String type,
                                                                          @Field("page") String page,
                                                                          @Field("limit") String limit);
}