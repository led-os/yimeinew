package com.chengzi.app.ui.comment.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.model.CommentModel;

public class CommentDetailViewModel extends BaseCommentViewModel {

    public final MutableLiveData<CommentBean> commentDetailLive = new MutableLiveData<>();

    private String commentId;   //  父评论id
    private CommentBean commentBean;    //  父评论
    private boolean newChildComment;

    private final CommentModel commentModel = new CommentModel();

    public void fetchCommentDetail(){
        commentModel.getChildComment(commentId,commentType)
                .subscribe(new SimpleObserver<ResponseBean<CommentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CommentBean> commentBeanResponseBean) {
                        commentDetailLive.postValue(commentBeanResponseBean.getData());
                    }
                });
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public CommentBean getCommentBean() {
        return commentBean;
    }

    public void setCommentBean(CommentBean commentBean) {
        this.commentBean = commentBean;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public boolean isNewChildComment() {
        return newChildComment;
    }

    public void setNewChildComment(boolean newChildComment) {
        this.newChildComment = newChildComment;
    }
}
