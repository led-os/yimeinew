package com.chengzi.app.ui.onlinequestionandanswer.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.OnlineQAbean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QAbean;
import com.chengzi.app.ui.onlinequestionandanswer.bean.QuestionDetailBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface OnlineQAService {

    /**
     * Question_____ - 问答-搜索历史/热搜(小芸)
     *
     * @param userId 用户id 不传 则只返回热门搜索
     */
    @GET("questionHotAndHisory")
    Observable<ResponseBean<OnlineQAbean>> questionSearchKey(@Query("user_id") String userId);

    /**
     * 问题列表App(小芸) 3.11
     */
    @GET("questionlist")
    Observable<ResponseBean<QAbean.QAbeanWraper>> questionList();

    /**
     * 问题详情--后台(小芸)
     *
     * @param questionId
     */
    @GET("questionDetail")
    Observable<ResponseBean<QuestionDetailBean>> questionDetail(@Query("question_id") String questionId);

    /**
     * 问题回答 3.11.7App(小芸)
     *
     * @param userId     用户id （必传）
     * @param questionId 问题id （必传）
     * @param content    回答内容（必传）
     */
    @FormUrlEncoded
    @POST("questionsAnswer")
    Observable<ResponseBean> answerQuestion(@Field("user_id") String userId,
                                            @Field("question_id") String questionId,
                                            @Field("content") String content);

    /**
     * 在线问答添加(小芸) 3.11.3 App
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("questionsAdd")
    Observable<ResponseBean> addQuestion(@FieldMap Map<String, String> params);

    /**
     * 问答搜索 App(小芸) 3.11.4/2/5
     */
    @FormUrlEncoded
    @POST("questionSearch")
    Observable<ResponseBean<PageBean<QAbean>>> questionSearch(@FieldMap Map<String, String> params);

    /**
     * 在线问答已解决/未解决列表
     *
     * @param status 1-已解决  2-未解决
     */
    @GET("questionStatus")
    Observable<ResponseBean<PageBean<QAbean>>> questionStatus(@Query("status") String status,
                                                              @Query("limit") String limit,
                                                              @Query("page") String page);

    /**
     * 医生、咨询师随机6个(小芸) 3.11
     *
     * @param userType
     * @param categoryId
     */
    @GET("questionPeo")
    Observable<ResponseBean<List<DoctorBean>>> questionPeo(@Query("user_type") String userType,
                                                           @Query("cate_id") String categoryId);

    /**
     * @param questionId 问题主键id （必传）
     * @param answerId   回答主键id （必传）
     * @return
     */
    @FormUrlEncoded
    @POST("questionAdopt")
    Observable<ResponseBean> acceptAnswer(@Field("question_id") String questionId,
                                          @Field("answer_id") String answerId);


    /**
     * 删除在线提问(只能提问者删除自己的提问)
     * @param questionId    问题id
     * @param userId        用户id
     * @return
     */
    @FormUrlEncoded
    @POST("questionDelete")
    Observable<ResponseBean> deleteQuestion(@Field("question_id") String questionId,
                                            @Field("user_id") String userId);
}
