package com.chengzi.app.ui.comment.model;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.comment.bean.CommentBean;

import io.reactivex.Observable;

public class CommentModel {

    private final CommentService commentService;

    public CommentModel() {
        commentService = Api.getApiService(CommentService.class);
    }

    /**
     * 删除评论（阿布）
     *
     * @param commentId 评论id （必须）
     * @param type      1 美人筹  2  发现圈 3 案例
     * @return
     */
    public Observable<ResponseBean> deleteComment(String commentId, String type) {
        String token = AccountHelper.getToken();
        String userId = AccountHelper.getUserId();
        return commentService.deleteComment(token, userId, commentId, type);
    }

    public Observable<ResponseBean<CommentBean>> getChildComment(String parentCommentId,
                                                                 int commentType) {
        return commentService.getChildComment(parentCommentId, String.valueOf(commentType));
    }

}
