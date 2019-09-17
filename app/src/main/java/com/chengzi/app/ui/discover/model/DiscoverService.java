package com.chengzi.app.ui.discover.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.discover.bean.ForumBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DiscoverService {

    /**
     * 发现圈列表
     *
     * @param type  身份类型 类型 1-用户 2-医生 3-咨询师 4-机构 (必）
     * @param limit 分页参数（必）
     * @param page  当前页数
     * @return
     */
    @GET("findList")
    Observable<ResponseBean<PageBean<ForumBean>>> forumList(@Query("user_id") String userId,
                                                            @Query("type") String type,
                                                            @Query("limit") String limit,
                                                            @Query("page") String page);

    /**
     * 发现圈发布动态（阿布）
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("findAdd")
    Observable<ResponseBean> postForum(@FieldMap Map<String, String> params, @Field("at_user_id[]") List<String> atUserIds);

    @GET("findDetails")
    Observable<ResponseBean<ForumBean>> forumDetail(@Query("details_id") String forumId,
                                                    @Query("user_id") String userId);

    /**
     * 发现圈关注/取消关注（阿布）
     *
     * @param token
     * @param userId
     * @param attentionId 被关注者id
     * @return
     */
    @GET("findFollow")
    Observable<ResponseBean> forumAttention(@Query("token") String token,
                                            @Query("user_id") String userId,
                                            @Query("cover_id") String attentionId);

    /**
     * 发现圈收藏（阿布）
     *
     * @param token
     * @param userId  用户id （必）
     * @param forumId 发现圈id
     * @return
     */
    @GET("findCollect")
    Observable<ResponseBean> forumCollect(@Query("token") String token,
                                          @Query("user_id") String userId,
                                          @Query("find_id") String forumId);

    /**
     * 发现圈点赞（阿布）
     *
     * @param token
     * @param userId
     * @param forumId 发现圈id
     * @return
     */
    @GET("findClick")
    Observable<ResponseBean> forumThumbup(@Query("token") String token,
                                          @Query("user_id") String userId,
                                          @Query("find_id") String forumId);

    /**
     * 帖子发表评论
     *
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("findCommentAdd")
    Observable<ResponseBean> forumCommentAdd(@FieldMap Map<String, String> params);

    /**
     * 删除详情（阿布）
     */
    @GET("delDetails")
    Observable<ResponseBean> delForum(@Query("token") String token,
                                      @Query("user_id") String userId,
                                      @Query("type") String type,
                                      @Query("details_id") String detailsId);

    /**
     * 分享统计
     *
     * @param token
     * @param userId
     * @param type   1 发现，2 案例
     * @param id     被转发的id
     * @return
     */
    @GET("clickNum")
    Observable<ResponseBean> shareStatistics(@Query("token") String token,
                                             @Query("user_id") String userId,
                                             @Query("type") String type,
                                             @Query("id") String id);
}
