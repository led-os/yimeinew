package com.chengzi.app.ui.message.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.message.bean.CommentMsgBean;
import com.chengzi.app.ui.message.model.MessageService;

import java.util.List;

public class CommentAndThumbupViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<CommentMsgBean>> liveData = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Api.getApiService(MessageService.class)
                .likesOrCommentNotify(AccountHelper.getUserId(),
                        String.valueOf(pageSize),
                        String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CommentMsgBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CommentMsgBean>> responseBean) {
                        liveData.postValue(responseBean.getData().getData());
                    }
                });
    }
}
