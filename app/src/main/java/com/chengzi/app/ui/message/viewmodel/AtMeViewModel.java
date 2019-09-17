package com.chengzi.app.ui.message.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.BaseForumViewModel;
import com.chengzi.app.ui.message.model.MessageService;

import java.util.List;

public class AtMeViewModel extends BaseForumViewModel implements IRequest {

    public final MutableLiveData<List<ForumBean>> forumListLive = new MutableLiveData<>();

    private final MessageService messageService;

    public AtMeViewModel() {
        messageService = Api.getApiService(MessageService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = AccountHelper.getUserId();
        messageService.getAtmeMsg(userId, String.valueOf(pageSize)
                , String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ForumBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ForumBean>> pageBeanResponseBean) {
                        forumListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }

}
