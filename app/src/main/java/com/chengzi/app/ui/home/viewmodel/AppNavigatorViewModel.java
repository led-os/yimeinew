package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.GetCustomerServiceStaffBean;
import com.chengzi.app.ui.home.model.HomeService;
import com.chengzi.app.ui.vip.model.VipService;

import java.util.List;

public class AppNavigatorViewModel extends BaseViewModel {

    public final MutableLiveData<List<CategoryItem>> homeCategoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<CategoryItem>> vipCategoryLive = new MutableLiveData<>();
    public final ObservableField<Pair<Integer, Integer>> status = new ObservableField<>();
    public final MutableLiveData<GetCustomerServiceStaffBean> customerServiceLive = new MutableLiveData<>();

    public void homeCategory() {
        Api.getApiService(HomeService.class).homeCategoryList()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> listResponseBean) {
                        homeCategoryLive.postValue(listResponseBean.getData());
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (status.get() != null) {
                                status.set(new Pair<>(1, status.get().second));
                            } else {
                                status.set(new Pair<>(1, 0));
                            }
                        });

                    }
                });
    }

    public void getVipCategory() {
        Api.getApiService(VipService.class).getVipCategory(AccountHelper.getToken())
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> bean) {

                        vipCategoryLive.postValue(bean.getData());
                        new Handler(Looper.getMainLooper()).post(() -> {

                            if (status.get() != null) {
                                status.set(new Pair<>(status.get().first, 1));
                            } else {
                                status.set(new Pair<>(0, 1));
                            }
                        });
                    }
                });
    }

    /**
     * 联系网站获取客服
     */
    public void getCustomerServiceStaff() {
        Api.getApiService(HomeService.class).getCustomerServiceStaff()
                .subscribe(new SimpleObserver<ResponseBean<GetCustomerServiceStaffBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<GetCustomerServiceStaffBean> getCustomerServiceStaffBeanResponseBean) {
                        customerServiceLive.postValue(getCustomerServiceStaffBeanResponseBean.getData());
                    }
                });
    }

}
