package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.BaseForumViewModel;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

public class UserCollectViewModel extends BaseForumViewModel implements IRequest {

    public final MutableLiveData<List<ForumBean>> userCollectLiveData = new MutableLiveData<>();

    private String targetId;
    private final MineService mineService;

    public UserCollectViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.userCollect(AccountHelper.getToken(), AccountHelper.getUserId(), targetId,
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ForumBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ForumBean>> responseBean) {
                        userCollectLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        userCollectLiveData.postValue(new ArrayList<>());
                    }
                });
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
