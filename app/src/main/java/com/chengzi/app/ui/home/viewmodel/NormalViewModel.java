package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.NormalBean;
import com.chengzi.app.ui.home.model.HomeService;

public class NormalViewModel extends BaseViewModel {

    public final MutableLiveData<NormalBean> normalLive = new MutableLiveData<>();

    private String categoryName;
    private String categoryId;

    private final HomeService homeService;

    public NormalViewModel() {
        homeService = Api.getApiService(HomeService.class);
    }

    public void specialList() {
        if (TextUtils.isEmpty(categoryId)) {
            return;
        }
        Params params = Params.newParams()
                .put("visit_type", "2")
                .put("class_id", categoryId);
        homeService.specialList(params.params())
                .subscribe(new SimpleObserver<ResponseBean<NormalBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<NormalBean> normalBeanResponseBean) {
                        normalLive.postValue(normalBeanResponseBean.getData());
                    }
                });
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
