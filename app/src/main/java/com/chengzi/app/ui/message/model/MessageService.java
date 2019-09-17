package com.chengzi.app.ui.message.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.message.bean.AttentionDoctorBean;
import com.chengzi.app.ui.message.bean.CommentMsgBean;
import com.chengzi.app.ui.message.bean.FansMsgBean;
import com.chengzi.app.ui.message.bean.MessageUnreadCountBean;
import com.chengzi.app.ui.message.bean.QuestionMsgBean;
import com.chengzi.app.ui.message.bean.SystemMsgBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageService {

    /**
     * 消息问答
     *
     * @param userId
     * @param pageSize
     * @param page
     * @return
     */
    @GET("notification-questionOnline")
    Observable<ResponseBean<PageBean<QuestionMsgBean>>> questionOnlineNotify(@Query("uid") String userId,
                                                                             @Query("page_size") String pageSize,
                                                                             @Query("page") String page);

    /**
     * 评论和赞
     *
     * @param userId
     * @param pageSize
     * @param page
     * @return
     */
    @GET("notification-lisksOrComment")
    Observable<ResponseBean<PageBean<CommentMsgBean>>> likesOrCommentNotify(@Query("uid") String userId,
                                                                            @Query("page_size") String pageSize,
                                                                            @Query("page") String page);

    /**
     * 系统消息
     *
     * @param userId
     */
    @GET("notification-system")
    Observable<ResponseBean<PageBean<SystemMsgBean>>> systemMsg(@Query("user_id") String userId,
                                                                @Query("page_size") String pageSize,
                                                                @Query("page") String page);

    /**
     * at 我的消息
     *
     * @param userId
     * @param pageSize
     * @param page
     * @return
     */
    @GET("notification-find-remind")
    Observable<ResponseBean<PageBean<ForumBean>>> getAtmeMsg(@Query("user_id") String userId,
                                                             @Query("page_size") String pageSize,
                                                             @Query("page") String page);

    /**
     * 粉丝消息
     *
     * @return
     */
    @GET("notification-fans")
    Observable<ResponseBean<PageBean<FansMsgBean>>> getFansMsg(@Query("user_id") String userId,
                                                               @Query("page_size") String pageSize,
                                                               @Query("page") String page);

    /**
     * 获取我关注的医生列表
     *
     * @param token
     * @param userId
     * @return
     */
    @GET("getAttention")
    Observable<ResponseBean<List<AttentionDoctorBean>>> getAttention(@Query("token") String token,
                                                                     @Query("user_id") String userId);

    /**
     * @param token
     * @param userId
     * @param caseUserId 病例所属用户id
     * @param toUserId   接受病例的用户id
     * @return
     */
    @FormUrlEncoded
    @POST("caseForward")
    Observable<ResponseBean> caseForward(@Field("token") String token,
                                         @Field("user_id") String userId,
                                         @Field("case_uid") String caseUserId,
                                         @Field("to_uid") String toUserId);

    /**
     * 获取未读消息数量
     * @param userId
     * @return
     */
    @GET("notification-count")
    Observable<ResponseBean<MessageUnreadCountBean>> notificationCount(@Query("user_id") String userId);

}
