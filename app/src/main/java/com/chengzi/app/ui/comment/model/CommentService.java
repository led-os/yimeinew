package com.chengzi.app.ui.comment.model;

import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.comment.bean.CommentBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommentService {

    /**
     * 删除评论（阿布）
     *
     * @param userId    用户id （必须）
     * @param commentId 评论id （必须）
     * @param type      1 美人筹  2  发现圈 3 案例
     * @return
     */
    @GET("delComment")
    Observable<ResponseBean> deleteComment(@Query("token") String token,
                                           @Query("user_id") String userId,
                                           @Query("comment_id") String commentId,
                                           @Query("type") String type);

    /**
     * 案例/发现圈/美人筹评论详情（单条评论）（黄）
     */
    @GET("commentSubDetail")
    Observable<ResponseBean<CommentBean>> getChildComment(@Query("comment_id") String commentId,
                                                          @Query("type") String commentType);

    @GET("findComment")
    Observable<ResponseBean<PageBean<CommentBean>>> forumComment(@Query("details_id") String forumId,
                                                    @Query("limit") String pageSize,
                                                    @Query("page") String page);

}
