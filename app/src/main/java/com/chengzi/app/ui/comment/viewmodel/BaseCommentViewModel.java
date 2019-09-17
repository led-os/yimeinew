package com.chengzi.app.ui.comment.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.cases.model.CaseService;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.model.CommentModel;
import com.chengzi.app.ui.discover.model.DiscoverService;
import com.chengzi.app.ui.peopleraise.model.RaiseService;
import com.chengzi.app.utils.URLUtils;

public class BaseCommentViewModel extends BaseViewModel {

    public final MutableLiveData<String> addCommentLive = new MutableLiveData<>();  //  添加评论
    public final MutableLiveData<String> deleteLive = new MutableLiveData<>();

    public final ObservableField<String> reply = new ObservableField<>();

    public final ObservableBoolean isComment = new ObservableBoolean(); //  评论输入框状态，true 显示， false 隐藏
    public final ObservableField<String> content = new ObservableField<>();  //  评论内容

    protected String targetId;      //  目标id ，美人筹id|案例id|帖子id
    protected int commentType;    //  1 案例 2 美人筹 3 发现圈

    protected String parentCommentId;   //  父评论id
    protected String replyUserId;       //  被评论人id

    protected final CommentModel commentModel = new CommentModel();
    protected final RaiseService raiseService;

    public BaseCommentViewModel() {
        raiseService = Api.getApiService(RaiseService.class);
        isComment.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!isComment.get()) {
                    // 软键盘收起时，重置这两个变量
                    parentCommentId = null;
                    replyUserId = null;
                }
            }
        });
    }

    //  删除评论
    public void deleteComment(String commentId) {
        String userId = AccountHelper.getUserId();
        // type 1 美人筹  2  发现圈 3 案例
        commentModel.deleteComment(commentId, commentType == 1 ? "3" : (commentType == 2 ? "1" : "2"))
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        deleteLive.postValue(commentId);
                    }
                });
    }

    // 一级评论
    public void topComment() {
        parentCommentId = null;
        replyUserId = null;
        reply.set("");
    }

    //  子评论
    public void childComment(CommentBean bean) {
        parentCommentId = bean.getId();
        replyUserId = null;
        reply.set(bean.getFrom_name());
    }

    //  三级评论
    public void grandComment(CommentBean bean) {
        parentCommentId = bean.getPid();  //  三级评论的父评论id 还是 一级评论的id，而不是 二级评论的id
        replyUserId = bean.getFrom_uid();
        reply.set(bean.getFrom_name());
    }

    //  添加评论
    public void addComment() {
        //  1 案例 2 美人筹 3 发现圈
        if (commentType == 1) {
            caseCommentAdd();
        } else if (commentType == 2) {
            planCommentAdd();
        } else {
            forumCommentAdd();
        }
        reset();
    }

    private void reset() {

        parentCommentId = null;
        replyUserId = null;
        content.set("");
        reply.set("");
    }

    //  添加案例评论
    private void caseCommentAdd() {
        String commentContent = null;
        if (!TextUtils.isEmpty(reply.get()) && content.get().startsWith("回复" + reply.get())) {
            commentContent = content.get().replace("回复" + reply.get(), "");
        } else {
            commentContent = content.get();
        }

        Params params = Params.newParams()
                .put("from_uid", AccountHelper.getUserId())
                .put("pid", TextUtils.isEmpty(parentCommentId) ? "0" : parentCommentId)
                .put("to_uid", TextUtils.isEmpty(replyUserId) ? "0" : replyUserId)
                .put("cases_id", targetId)
                .put("content", URLUtils.encode(commentContent));

        final boolean isTopComment = TextUtils.isEmpty(parentCommentId);  //  是否是一级评论
        final String parentId = parentCommentId;

        Api.getApiService(CaseService.class)
                .addCaseComment(params.params())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addCommentLive.postValue(isTopComment ? null : parentId);
                    }
                });
    }

    //  添加美人筹评论
    private void planCommentAdd() {

        String commentContent = null;
        if (!TextUtils.isEmpty(reply.get()) && content.get().startsWith("回复" + reply.get())) {
            commentContent = content.get().replace("回复" + reply.get(), "");
        } else {
            commentContent = content.get();
        }

        String userId = AccountHelper.getUserId();
        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", userId)
                .put("pid", parentCommentId)    //  被评论内容的id （非） 默认0 一级评论
                .put("to_user_id", replyUserId)            //  被评论人的id （非）默认0
                .put("plan_order_id", targetId)     //  美人筹订单 （必）
                .put("content", URLUtils.encode(commentContent));    //  评论内容 （必）

        final boolean isTopComment = TextUtils.isEmpty(parentCommentId);  //  是否是一级评论
        final String parentId = parentCommentId;

        raiseService.planCommentAdd(params.params())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addCommentLive.postValue(isTopComment ? null : parentId);
                    }
                });
    }

    //  添加帖子评论
    private void forumCommentAdd() {

        String commentContent = null;
        if (!TextUtils.isEmpty(reply.get()) && content.get().startsWith("回复" + reply.get())) {
            commentContent = content.get().replace("回复" + reply.get(), "");
        } else {
            commentContent = content.get();
        }

        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("pid", parentCommentId)
                .put("to_user_id", replyUserId)
                .put("find_id", targetId)
                .put("content", URLUtils.encode(commentContent));

        final boolean isTopComment = TextUtils.isEmpty(parentCommentId);  //  是否是一级评论
        final String parentId = parentCommentId;

        Api.getApiService(DiscoverService.class)
                .forumCommentAdd(params.params())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        addCommentLive.postValue(isTopComment ? null : parentId);
                    }
                });
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
