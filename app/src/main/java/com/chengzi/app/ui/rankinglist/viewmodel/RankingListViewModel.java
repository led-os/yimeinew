package com.chengzi.app.ui.rankinglist.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.CategoryItem;

import java.util.List;

public class RankingListViewModel extends BaseViewModel {

    public final MutableLiveData<CategoryItem> categoryIdLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> categoryLive = new MutableLiveData<>();
    private final CommonModel commonModel = new CommonModel();

    public void categoryList() {
        commonModel.categoryList1()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> navigatorListBeanResponseBean) {
                        List<CategoryItem> categoryItems = navigatorListBeanResponseBean.getData();
                        categoryLive.postValue(categoryItems);
                    }
                });
    }

}
