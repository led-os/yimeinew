package com.chengzi.app.ui.comment.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.cases.model.CaseService;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.comment.model.CommentService;

import java.util.List;

public class CommentContainerViewModel extends BaseCommentViewModel implements IRequest {

    public final MutableLiveData<CommentBean> commentDetailLive = new MutableLiveData<>();
    public final MutableLiveData<List<CommentBean>> commentListLive = new MutableLiveData<>();
    public final MutableLiveData<List<CommentBean>> singleCommentListLive = new MutableLiveData<>();


    private final CommentService commentService;

    public CommentContainerViewModel() {
        commentService = Api.getApiService(CommentService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        //  1 案例 2 美人筹 3 发现圈
        if (commentType == 1) {
            getCaseCommentList(currentPage, pageSize);
        } else if (commentType == 2) {

        } else {
            getForumComment(currentPage, pageSize);
        }
    }

    private void getForumComment(int currentPage, int pageSize) {
        commentService.forumComment(targetId, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> responseBean) {
                        commentListLive.postValue(responseBean.getData().getData());
                    }
                });
    }

    public void fetchCommentDetail(String commentId) {
        commentModel.getChildComment(commentId, commentType)
                .subscribe(new SimpleObserver<ResponseBean<CommentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CommentBean> commentBeanResponseBean) {
                        commentDetailLive.postValue(commentBeanResponseBean.getData());
                    }
                });
    }

    private void getCaseCommentList(int currentPage, int pageSize) {
        Api.getApiService(CaseService.class)
                .caseCommentList(targetId, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> pageBeanResponseBean) {
                        commentListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    public void fetchSingle(int page){
        //  1 案例 2 美人筹 3 发现圈
        if (commentType == 1) {
            getSingleCaseComment(page);
        } else {
            getSingleForumComment(page);
        }
    }

    private void getSingleCaseComment(int page){
        Api.getApiService(CaseService.class)
                .caseCommentList(targetId, String.valueOf(1), String.valueOf(page))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> pageBeanResponseBean) {
                        singleCommentListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    private void getSingleForumComment(int page){
        commentService.forumComment(targetId, String.valueOf(1), String.valueOf(page))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> responseBean) {
                        singleCommentListLive.postValue(responseBean.getData().getData());
                    }
                });
    }
}
