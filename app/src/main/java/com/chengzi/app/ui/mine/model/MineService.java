package com.chengzi.app.ui.mine.model;

import com.google.gson.JsonObject;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.homepage.bean.ClickLikeListBean;
import com.chengzi.app.ui.homepage.bean.GetAnswersBean;
import com.chengzi.app.ui.homepage.bean.UserQuestionsBean;
import com.chengzi.app.ui.mine.bean.AccountFlowBean;
import com.chengzi.app.ui.mine.bean.AgressmentBean;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.bean.BankCityBean;
import com.chengzi.app.ui.mine.bean.CaseInfoBean;
import com.chengzi.app.ui.mine.bean.ComplaintListBean;
import com.chengzi.app.ui.mine.bean.CouponListBean;
import com.chengzi.app.ui.mine.bean.ElectronicInvoiceBean;
import com.chengzi.app.ui.mine.bean.FollowBean;
import com.chengzi.app.ui.mine.bean.GetDownloadUrlQrcodeBean;
import com.chengzi.app.ui.mine.bean.GoodAtBean;
import com.chengzi.app.ui.mine.bean.HistoryInvoiceBean;
import com.chengzi.app.ui.mine.bean.IsSignBean;
import com.chengzi.app.ui.mine.bean.MyAppointmentBean;
import com.chengzi.app.ui.mine.bean.OpenVipBean;
import com.chengzi.app.ui.mine.bean.RecordListBean;
import com.chengzi.app.ui.mine.bean.RuleListBean;
import com.chengzi.app.ui.mine.bean.SeeOrderBean;
import com.chengzi.app.ui.mine.bean.UserVipInfoBean;
import com.chengzi.app.ui.mine.bean.VipGivingListBean;
import com.chengzi.app.ui.mine.bean.WalletBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;

import java.util.HashMap;
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

public interface MineService {

    //Rule - 规则帮助列表(小芸)
    @FormUrlEncoded
    @POST("ruleList")
    Observable<ResponseBean<PageBean<RuleListBean>>> ruleList(@Field("token") String token,
                                                              @Field("user_id") String user_id,
                                                              @Field("type") String type,
                                                              @Field("limit") String limit);

    //Feedback - 意见反馈添加(小芸)
    @FormUrlEncoded
    @POST("feedback")
    Observable<ResponseBean<String>> feedback(@Field("user_id") String user_id,
                                              @Field("phone") String phone,
                                              @Field("opinion") String opinion);

    //CaseOnline - 获取线上病例详情（冀）
    @GET("caseInfo")
    Observable<ResponseBean<CaseInfoBean>> caseInfo(@Query("token") String token,
                                                    @Query("user_id") String user_id,
                                                    @Query("case_uid") String case_uid);

    //CaseOnline - 增加、编辑线上病例（冀）
    @FormUrlEncoded
    @POST("addCase")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> addCase(@FieldMap Map<String, String> params,
                                     @Field("image[]") List<String> image);

    @FormUrlEncoded
    @POST("addCase")
    Observable<ResponseBean> addCase(@FieldMap Map<String, String> params);

    //VIP - VIP转赠列表（冀）
    @GET("vipGivingList")
    Observable<ResponseBean<List<VipGivingListBean>>> vipGivingList(@Query("token") String token,
                                                                    @Query("user_id") String user_id);

    //VIP - VIP转赠（冀）
    @FormUrlEncoded
    @POST("vipGiving")
    Observable<ResponseBean> vipGiving(@Field("token") String token,
                                       @Field("user_id") String user_id,
                                       @Field("mobile") String mobile);

//    //VIP - VIP信息展示
//    @GET("vipInfo")
//    Observable<ResponseBean<HosipationalVipInfoBean>> vipInfo(@Query("token") String token,
//                                                              @Query("user_id") String user_id,
//                                                              @Query("type") String type); // 1-用户 2-医生 3-咨询师 4-机构


    //VIP - VIP信息展示-->医院
    @GET("userVipInfo")
    Observable<ResponseBean<UserVipInfoBean>> userVipInfo(@Query("token") String token,
                                                          @Query("user_id") String user_id,
                                                          @Query("type") String type); // 1-用户 2-医生 3-咨询师 4-机构

    //VIP - VIP开通介绍
    @FormUrlEncoded
    @POST("vipAgreeenmentList")
    Observable<ResponseBean<AgressmentBean>> vipAgreeenmentList(@Field("token") String token,
                                                                @Field("user_type") String user_type,
                                                                @Field("info_type") String info_type);

    //Texts - 文本中心
    //文本id 1 关于我们 2 隐私政策 3 用户协议 4 联系我们 5 意见反馈 6 排行榜规则 7 帮助中心
    // 8 用户vip规则介绍 9 咨询师vip规则介绍 10医生vip规则介绍 11医院vip规则介绍 12推广规则
    @GET("getTexts")
    Observable<ResponseBean<AgressmentBean>> getTexts(@Query("t_id") String t_id);

    //VIP - 获取VIP价格信息展示
    @FormUrlEncoded
    @POST("getVipPrice")
    Observable<ResponseBean<List<OpenVipBean>>> getVipPrice(@Field("token") String token,
                                                            @Field("uid") String uid,
                                                            @Field("type") String type);    //1-普通用户 2-其他

    //VIP - 下载二维码（冀）
    @GET("getDownloadUrlQrcode")
    Observable<ResponseBean<GetDownloadUrlQrcodeBean>> getDownloadUrlQrcode();

    //Wallet - 我的优惠券列表（冀）
    @GET("couponList")
    Observable<ResponseBean<List<CouponListBean>>> couponList(@Query("token") String token,
                                                              @Query("user_id") String user_id);

    //Record - 美丽档案列表（冀）
    @GET("recordList")
    Observable<ResponseBean<PageBean<RecordListBean>>> recordList(@Query("token") String token,
                                                                  @Query("user_id") String user_id,
                                                                  @Query("uid") String uid,
                                                                  @Query("limit") String limit,
                                                                  @Query("page") String page);

    //Record - 美丽档案编辑（冀）
    @FormUrlEncoded
    @POST("recordEdit")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> recordEdit(@Field("token") String token,
                                        @Field("user_id") String user_id,
                                        @Field("order_id") String order_id,
                                        @Field("image[]") List<String> image);


    //Wallet - 用户我的钱包(冀)
    @GET("wallet")
    Observable<ResponseBean<WalletBean>> walletInfo(@QueryMap HashMap<String, String> params);

    //Wallet - 绑定提现账号（冀）
    @FormUrlEncoded
    @POST("bindAccount")
    Observable<ResponseBean> bindAccount(@FieldMap HashMap<String, String> params);

    //Wallet - 解绑提现账号（冀）
    @FormUrlEncoded
    @POST("unBindAccount")
    Observable<ResponseBean> unbindAccount(@FieldMap HashMap<String, String> params);

    //Wallet - 提现接口（冀）
    @FormUrlEncoded
    @POST("withdraw")
    Observable<ResponseBean> withdraw(@FieldMap HashMap<String, String> params);

    //获取对公账户的省市
    @GET("bankCity")
    Observable<ResponseBean<List<BankCityBean>>> bankCity();

    @GET("bankCity")
    Observable<ResponseBean<List<BankCityBean>>> bankCity(@Query("pid") String pid);

    //Wallet - 账户流水（冀）
    @GET("accountFlow")
    Observable<ResponseBean<AccountFlowBean>> accountFlow(@QueryMap HashMap<String, String> params);

    //Invoice - 可开发票列表（冀）
    @GET("invoiceList")
    Observable<ResponseBean<PageBean<ElectronicInvoiceBean>>> invoiceList(@Query("token") String token,
                                                                          @Query("user_id") String user_id,
                                                                          @Query("limit") String limit,
                                                                          @Query("page") String page);

    //Invoice - 开发票（冀）
    @FormUrlEncoded
    @POST("invoiceAdd")
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    Observable<ResponseBean> invoiceAdd(@Field("token") String token,
                                        @Field("user_id") String user_id,
                                        @Field("payment_id[]") List<String> payment_id,
                                        @Field("rise_type") String rise_type,
                                        @Field("rise") String rise,
                                        @Field("identify_number") String identify_number,
                                        @Field("invoice_content") String invoice_content,
                                        @Field("email") String email);

    //Invoice - 开票历史 （冀）
    @GET("invoiceHistory")
    Observable<ResponseBean<PageBean<HistoryInvoiceBean>>> invoiceHistory(@Query("token") String token,
                                                                          @Query("user_id") String user_id,
                                                                          @Query("limit") String limit,
                                                                          @Query("page") String page);

    //Complaint - 修改在线状态(小芸)
    @FormUrlEncoded
    @POST("changeOnlineState")
    Observable<ResponseBean> changeOnlineState(@FieldMap HashMap<String, String> params);

    //Setting - 修改手机号(小芸)
    @FormUrlEncoded
    @POST("changeMobile")
    Observable<ResponseBean> changeMobile(@FieldMap HashMap<String, String> params);

    //Complaint - 修改密码(小芸)
    @FormUrlEncoded
    @POST("changePwd")
    Observable<ResponseBean> changePwd(@FieldMap HashMap<String, String> params);

    //User - 我的预约/预约我的(冀)
    @FormUrlEncoded
    @POST("myAppointment")
    Observable<ResponseBean<PageBean<MyAppointmentBean>>> myAppointment(@Field("token") String token,
                                                                        @Field("user_id") String user_id,
                                                                        @Field("type") String type,
                                                                        @Field("limit") int limit,
                                                                        @Field("page") int page);

    //Complaint - 投诉列表(小芸)
    @FormUrlEncoded
    @POST("complaintList")
    Observable<ResponseBean<PageBean<ComplaintListBean>>> complaintList(@Field("token") String token,
                                                                        @Field("user_id") String user_id,
                                                                        @Field("type") String type,
                                                                        @Field("limit") int limit,
                                                                        @Field("page") int page);

    //User________ - 查看下单到个人的（咨询师、医生可用）
    @FormUrlEncoded
    @POST("seeOrder")
    Observable<ResponseBean<PageBean<SeeOrderBean>>> seeOrder(@Field("token") String token,
                                                              @Field("user_id") String user_id,
                                                              @Field("type") String type,
                                                              @Field("limit") int limit,
                                                              @Field("page") int page);

    //User - 取消预约（冀）
    @GET("cancelAppointment")
    Observable<ResponseBean> cancelAppointment(@Query("token") String token,
                                               @Query("user_id") String user_id,
                                               @Query("id") String id);

    //User - 获取用户签到状态（冀）
    @GET("isSign")
    Observable<ResponseBean<IsSignBean>> isSign(@Query("token") String token,
                                                @Query("user_id") String user_id);


    //User__ - 用户签到（冀）
    @GET("userSign")
    Observable<ResponseBean> userSign(@Query("token") String token,
                                      @Query("user_id") String user_id);

    //User - 更新用户头像
    @FormUrlEncoded
    @POST("updateInfo")
    Observable<ResponseBean<UserInfoBean>> updateInfo(@FieldMap HashMap<String, String> params);

    //User - 获取所有一级分类
    @FormUrlEncoded
    @POST("ParentLevelList")
    Observable<ResponseBean<List<GoodAtBean>>> parentLevelList(@Field("token") String token,
                                                               @Field("user_id") String user_id);

    //Doctor - 更新擅长
    @FormUrlEncoded
    @POST("ParentLevelUpdate")
    Observable<ResponseBean> parentLevelUpdate(@Field("token") String token,
                                               @Field("user_id") String user_id,
                                               @Field("uid") String uid,
                                               @Field("str") String str);

    //Reuse - 医院/咨询师/医生认证页面
    @GET("Authentication")
    Observable<ResponseBean<AuthenticationBean>> authentication(@Query("token") String token,
                                                                @Query("user_id") String user_id);

    //用户信息展示
    @GET("userInfoShow")
    Observable<ResponseBean<UserInfoBean>> userInfoShow(@Query("token") String token,
                                                        @Query("user_id") String user_id);

    //Consultant - 关注列表/粉丝列表
    @GET("followToList")
    Observable<ResponseBean<PageBean<FollowBean>>> followToList(@Query("token") String token,
                                                                @Query("user_id") String user_id,
                                                                @Query("click_id") String click_id,
                                                                @Query("type") String type,
                                                                @Query("page") String page,
                                                                @Query("limit") String limit);

    //Consultant - 点赞列表-点赞我的（冀）
    @GET("clickLikeList")
    Observable<ResponseBean<PageBean<ClickLikeListBean>>> clickLikeList(@Query("token") String token,
                                                                        @Query("user_id") String click_id,
                                                                        @Query("page") String page,
                                                                        @Query("limit") String limit);

    //User - 用户发表3.17 (阿布）
    @GET("userIndex")
    Observable<ResponseBean<PageBean<ForumBean>>> userIndex(@Query("token") String token,
                                                            @Query("user_id") String user_id,
                                                            @Query("click_id") String click_id,
                                                            @Query("page") String page,
                                                            @Query("limit") String limit);

    //User - 用户收藏3.17（阿布）
    @GET("userCollect")
    Observable<ResponseBean<PageBean<ForumBean>>> userCollect(@Query("token") String token,
                                                              @Query("user_id") String user_id,
                                                              @Query("click_id") String click_id,
                                                              @Query("page") String page,
                                                              @Query("limit") String limit);

    //User - 用户提问3.17（阿布）
    @GET("userQuestions")
    Observable<ResponseBean<PageBean<UserQuestionsBean>>> userQuestions(@Query("token") String token,
                                                                        @Query("user_id") String user_id,
                                                                        @Query("click_id") String click_id,
                                                                        @Query("page") String page,
                                                                        @Query("limit") String limit);

    //User - 用户回答3.17（阿布）
    @GET("userAskOthersAnswers")
    Observable<ResponseBean<PageBean<GetAnswersBean>>> getAnswers(@Query("token") String token,
                                                                  @Query("user_id") String user_id,
                                                                  @Query("click_id") String click_id,
                                                                  @Query("page") String page,
                                                                  @Query("page_size") String limit);

    //Question - 问/答列表通用3.17.2/3.11.2/3.11.4/3.11.5/3.13.10/7.4.2（冀）
    @GET("allQuestionAnswerList")
    Observable<ResponseBean<PageBean<QAbean>>> allQuestionAnswerList(@Query("user_id") String user_id,  //当前用户id ，记录搜索历史用
                                                                     @Query("click_id") String click_id,//被查看者id ，不传返回全部数据
                                                                     @Query("status") String status,    // 问题状态 1-已解决 2-未解决，其他返全部数据 （非必须）
                                                                     @Query("page") String page,
                                                                     @Query("limit") String limit);

    /**
     * 获取授权参数
     *
     * @return
     */
    @GET("aliAccountRequestParam")
    Observable<ResponseBean<JsonObject>> aliAccountRequestParam(@Query("user_id") String userId);
}