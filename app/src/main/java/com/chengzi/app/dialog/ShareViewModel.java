package com.chengzi.app.dialog;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.discover.model.DiscoverService;

public class ShareViewModel extends BaseViewModel {

    public final MutableLiveData<Boolean> shareLive = new MutableLiveData<>();

    private String type;    //  1 发现，2 案例
    private String targetId;    //   被转发的id

    public void shareStatistics() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        Api.getApiService(DiscoverService.class)
                .shareStatistics(AccountHelper.getToken(), AccountHelper.getUserId(),
                        type, targetId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        shareLive.postValue(true);
                    }
                });
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
