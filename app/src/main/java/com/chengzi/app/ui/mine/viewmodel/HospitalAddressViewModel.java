package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.HashMap;

public class HospitalAddressViewModel extends BaseViewModel {

    public final ObservableField<String> keyword = new ObservableField<>();
    public final ObservableField<String> detailAddress = new ObservableField<>();

    //更新用户地址
    public final MutableLiveData<ResponseBean<UserInfoBean>> updateInfoLiveData = new MutableLiveData<>();

    public void updateInfo(HashMap<String, String> map) {
        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId());
        params.putAll(map);
        Api.getApiService(MineService.class).updateInfo(params.params())
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        updateInfoLiveData.postValue(userInfoBeanResponseBean);
                    }
                });
    }
}