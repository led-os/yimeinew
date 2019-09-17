package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;

import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.bean.ProvinceBean;
import com.chengzi.app.ui.common.model.CommonModel;

import java.util.List;

public class SelectCityViewModel extends BaseViewModel {

    public final ObservableInt selectedProvincePos = new ObservableInt();
    public final MutableLiveData<List<ProvinceBean>> liveData = new MutableLiveData<>();
    private final CommonModel commonModel = new CommonModel();

    public void getCities() {
        commonModel.getCityList()
                .subscribe(new SimpleObserver<ResponseBean<List<ProvinceBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<ProvinceBean>> listResponseBean) {
                        liveData.postValue(listResponseBean.getData());
                    }
                });
    }

}
