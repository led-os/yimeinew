package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.BankCityBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

public class SelectProvinceCitiesViewModel extends BaseViewModel {

    public final ObservableInt selectedProvincePos = new ObservableInt();
    public final MutableLiveData<List<BankCityBean>> bankProvinceLiveData = new MutableLiveData<>();
    public final MutableLiveData<List<BankCityBean>> bankCityLiveData = new MutableLiveData<>();

    /**
     * 提现
     *
     * @param pid 市必传
     */
    public void bankCity(String pid) {
        if (pid.equals("0")) {
            Api.getApiService(MineService.class).bankCity().subscribe(new SimpleObserver<ResponseBean<List<BankCityBean>>>() {
                @Override
                public void onSuccess(ResponseBean<List<BankCityBean>> responseBean) {
                    bankProvinceLiveData.postValue(responseBean.getData());
                }

                @Override
                public void onError(int code, String errorMsg) {
//                    super.onError(code, errorMsg);
                    bankProvinceLiveData.postValue(null);
                }
            });
        } else {
            Api.getApiService(MineService.class).bankCity(pid).subscribe(new SimpleObserver<ResponseBean<List<BankCityBean>>>() {
                @Override
                public void onSuccess(ResponseBean<List<BankCityBean>> responseBean) {
                    bankCityLiveData.postValue(responseBean.getData());
                }

                @Override
                public void onError(int code, String errorMsg) {
//                    super.onError(code, errorMsg);
                    bankCityLiveData.postValue(null);
                }
            });
        }
    }
}