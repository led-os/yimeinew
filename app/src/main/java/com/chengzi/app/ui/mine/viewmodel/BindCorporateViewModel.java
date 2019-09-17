package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.chengzi.app.ui.mine.bean.BankBean;
import com.chengzi.app.ui.mine.model.BindCorporateService;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;

import java.util.List;

public class BindCorporateViewModel extends BaseViewModel {

    public final MutableLiveData<List<BankBean>> bankLive = new MutableLiveData<>();

    public void getBanks(){
        Api.getApiService(BindCorporateService.class)
                .getBanks()
                .subscribe(new SimpleObserver<ResponseBean<List<BankBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<BankBean>> listResponseBean) {
                        bankLive.postValue(listResponseBean.getData());
                    }
                });
    }

}
