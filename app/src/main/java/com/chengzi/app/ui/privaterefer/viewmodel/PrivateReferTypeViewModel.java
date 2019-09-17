package com.chengzi.app.ui.privaterefer.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.model.HomeService;
import com.chengzi.app.ui.privaterefer.model.ReferService;

import java.util.List;

public class PrivateReferTypeViewModel extends BaseViewModel {

    public final MutableLiveData<String> referLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> homeCategoryLive = new MutableLiveData<>();

    private final CommonModel commonModel = new CommonModel();
    private final ReferService referService;

    public PrivateReferTypeViewModel() {
        referService = Api.getApiService(ReferService.class);
    }

    public void homeCategory() {
        Api.getApiService(HomeService.class).homeCategoryList()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> listResponseBean) {
                        homeCategoryLive.postValue(listResponseBean.getData());
                    }
                });
    }

    //  发起 私享咨询/在线面诊
    public void referStart(String categoryId){
        String userId = AccountHelper.getUserId();
        referService.sheetStart(userId,categoryId,String.valueOf(type))
                .subscribe(new SimpleObserver<ResponseBean<String>>() {
                    @Override
                    public void onSuccess(ResponseBean<String> stringResponseBean) {
                        referLive.postValue(stringResponseBean.getData());
                    }
                });
    }

    private int type; // 0 私享咨询  1 在线诊断

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
