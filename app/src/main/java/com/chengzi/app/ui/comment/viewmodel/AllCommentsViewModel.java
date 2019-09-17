package com.chengzi.app.ui.comment.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.comment.bean.CommentBean;
import com.chengzi.app.ui.peopleraise.model.RaiseService;

import java.util.List;

public class AllCommentsViewModel extends BaseCommentViewModel implements IRequest {

    public final MutableLiveData<CommentBean> commentDetailLive = new MutableLiveData<>();
    public final MutableLiveData<List<CommentBean>> singleCommentLive = new MutableLiveData<>();
    public final MutableLiveData<List<CommentBean>> commentListLive = new MutableLiveData<>();

    private final RaiseService raiseService;

    public AllCommentsViewModel() {
        raiseService = Api.getApiService(RaiseService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        raiseService.planCommentList(targetId, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> pageBeanResponseBean) {
                        commentListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

    //
    public void fetchCommentDetail(String commentId) {
        commentModel.getChildComment(commentId, commentType)
                .subscribe(new SimpleObserver<ResponseBean<CommentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CommentBean> commentBeanResponseBean) {
                        commentDetailLive.postValue(commentBeanResponseBean.getData());
                    }
                });
    }

    public void fetchSingleItem(int page){
        raiseService.planCommentList(targetId, String.valueOf(1), String.valueOf(page))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentBean>> pageBeanResponseBean) {
                        singleCommentLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

}
