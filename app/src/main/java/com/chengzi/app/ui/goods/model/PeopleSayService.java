package com.chengzi.app.ui.goods.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.goods.bean.PeopleSayAnswerBean;
import com.chengzi.app.ui.goods.bean.PeopleSayQuestionBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PeopleSayService {


    /**
     * 大家说问题列表(李金泽)
     *
     * @param askType  必传（大家说类型:doctor|consultant|goods|serviceOrganization）
     * @param targetId 必传 (大家说类型下的ID，例如：医生doctor 的时候 to_id 为用户ID)
     * @param pageSize 每页数据数量
     * @param page     页数
     */
    @GET("AskOthersQuestionList")
    Observable<ResponseBean<PageBean<PeopleSayQuestionBean>>> askOthersQuestionList(
            @Query("ask_type") String askType,
            @Query("to_id") String targetId,
            @Query("page_size") String pageSize,
            @Query("page") String page);


    /**
     * 大家说问题回复列表(李金泽)
     *
     * @param questionId 必传(问题ID)
     * @param pageSize
     * @param page
     */
    @GET("askOthersAnswerList")
    Observable<ResponseBean<PageBean<PeopleSayAnswerBean>>> askOthersAnswerList(
            @Query("question_id") String questionId,
            @Query("page_size") String pageSize,
            @Query("page") String page);


    /**
     * 大家说发布问题(李金泽)
     *
     * @param userId   提问人id
     * @param targetId 必传(商品、医生、咨询师、机构他们的ID)
     * @param askType  必传(doctor|consultant|goods|serviceOrganization)
     * @param content  问题内容
     */
    @FormUrlEncoded
    @POST("askOthersPushlished")
    Observable<ResponseBean> askOthersPushlished(@Field("uid") String userId,
                                                 @Field("to_id") String targetId,
                                                 @Field("ask_type") String askType,
                                                 @Field("content") String content);

    /**
     * 大家说回复问题(李金泽)
     *
     * @param userId
     * @param questionId 必传(问题ID)
     * @param content
     */
    @FormUrlEncoded
    @POST("askOthersReply")
    Observable<ResponseBean> answerOthers(@Field("uid") String userId,
                                          @Field("to_question_id") String questionId,
                                          @Field("content") String content);

    /**
     * 大家说问题详情
     * @param questionId
     */
    @GET("askOthersDetail")
    Observable<ResponseBean<PeopleSayQuestionBean>> askOthersQuestionDetail(@Query("question_id") String questionId);

}
