package com.chengzi.app.ui.discover.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.FollowBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

public class AtOtherUserViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<FollowBean>> followListLive = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        //关注/粉丝列表
        Api.getApiService(MineService.class)
                .followToList(AccountHelper.getToken(), AccountHelper.getUserId(),
                        AccountHelper.getUserId(), "1", String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<FollowBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<FollowBean>> responseBean) {
                        followListLive.postValue(responseBean.getData().getData());
                    }
                });
    }

}
