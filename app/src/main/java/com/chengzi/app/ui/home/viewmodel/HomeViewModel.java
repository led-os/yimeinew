package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.common.bean.CityBean;
import com.chengzi.app.ui.common.bean.ProvinceBean;
import com.chengzi.app.ui.common.model.CommonModel;
import com.chengzi.app.ui.home.bean.AdvListBean;
import com.chengzi.app.ui.home.bean.AdvertisingBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.bean.LocationBean;
import com.chengzi.app.ui.home.model.HomeService;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

public class HomeViewModel extends BaseViewModel {

    public final ObservableBoolean isRecommand = new ObservableBoolean(true);

    public final MutableLiveData<List<CategoryItem>> homeCategoryLive = new MutableLiveData<>();
    public final MutableLiveData<AdvertisingBean.AdvertisingHolderBean> advertisingLive = new MutableLiveData<>();
    public final MutableLiveData<List<AdvListBean>> positionListLive = new MutableLiveData<>();

    //用户切换城市->切换成功后 刷新首页/VIP/发现模块
    public final MutableLiveData<Boolean> changeCityLive = new MutableLiveData<>();

    public final boolean[] cityAndCategoryStatus = new boolean[2];
    public final MutableLiveData<Boolean> cityAndCategoryInitLive = new MutableLiveData<>();

    public final ObservableField<String> cityName = new ObservableField<>();

    public final ObservableField<List<ProvinceBean>> cities = new ObservableField<>();
    public final ObservableField<LocationBean> location = new ObservableField<>();

    private boolean launchAppDetail;
    private CategoryItem selectedCategory;
    private boolean refreshing;

    private HomeService homeService;
    private final CommonModel commonModel = new CommonModel();

    public HomeViewModel() {
        homeService = Api.getApiService(HomeService.class);
        cities.addOnPropertyChangedCallback(callback);
        location.addOnPropertyChangedCallback(callback);
    }

    private Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (cities.get() == null || location.get() == null) {
                return;
            }
            LocationBean locationBean = location.get();
            //  定位失败 或 用户拒绝了定位
            if (TextUtils.isEmpty(locationBean.getProvince())) {
                String cityId = PreferenceManager.getPreference(PreferenceManager.CITY_ID);
                if (TextUtils.isEmpty(cityId)) {
                    //  用户未选择过城市，设置默认北京
                    PreferenceManager.setPreference(PreferenceManager.CITY_ID, "110100");
                    PreferenceManager.setPreference(PreferenceManager.CITY_NAME, "北京市"); //  设置默认值
                    cityAndCategoryStatus[0] = true;
                    cityAndCategoryInitLive.postValue(true);
                }
            } else {
                boolean find = false;
                for (ProvinceBean provinceBean : cities.get()) {
                    if (TextUtils.equals(provinceBean.getProvince_name(), "直辖市") || TextUtils.equals(provinceBean.getProvince_name(), locationBean.getProvince())) {
                        for (CityBean cityBean : provinceBean.getChild()) {
                            if (TextUtils.equals(cityBean.getCity_name(), locationBean.getCityName()) ||
                                    locationBean.getCityName().startsWith(cityBean.getCity_name())) {
                                PreferenceManager.setPreference(PreferenceManager.CITY_ID, cityBean.getCity_id());
                                PreferenceManager.setPreference(PreferenceManager.CITY_NAME, locationBean.getCityName());
                                find = true;
                                cityAndCategoryStatus[0] = true;
                                cityAndCategoryInitLive.postValue(true);
                                break;
                            }
                        }
                    }
                    if (find) {
                        break;
                    }
                }
            }

            cityName.set(PreferenceManager.getPreference(PreferenceManager.CITY_NAME));

        }
    };

    public void homeCategory() {
        homeService.homeCategoryList()
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> listResponseBean) {
                        homeCategoryLive.postValue(listResponseBean.getData());
                    }
                });
    }

    // Advertisement - 广告(小芸)
    public void advertisement() {
        homeService.advertisement("")
                .subscribe(new SimpleObserver<ResponseBean<AdvertisingBean.AdvertisingHolderBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<AdvertisingBean.AdvertisingHolderBean> advertisingHolderBeanResponseBean) {
                        advertisingLive.postValue(advertisingHolderBeanResponseBean.getData());
                    }
                });
    }

    //首页- 广告banner图
    public void positionList() {
        if (selectedCategory == null) {
            ToastUtils.showShort("类别为空");
            return;
        }
        String city_id = PreferenceManager.getPreference(PreferenceManager.CITY_ID);
        homeService.positionList(city_id, selectedCategory.getId())
                .subscribe(new SimpleObserver<ResponseBean<List<AdvListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<AdvListBean>> advListBean) {
                        positionListLive.postValue(advListBean.getData());
                    }
                });
    }

    public void cityList() {
        commonModel.getCityList()
                .subscribe(new SimpleObserver<ResponseBean<List<ProvinceBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<ProvinceBean>> listResponseBean) {
                        cities.set(listResponseBean.getData());
                    }
                });
    }

    public void changeCity(String cityId) {
        changeCity(cityId, true);
    }

    public void changeCity(String cityId, boolean update) {
        Api.getApiService(HomeService.class)
                .changeCity(AccountHelper.getToken(),
                        AccountHelper.getUserId(),
                        cityId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        if (update) {
                            changeCityLive.postValue(true);
                        }
                    }
                });
    }


    public boolean isLaunchAppDetail() {
        return launchAppDetail;
    }

    public void setLaunchAppDetail(boolean launchAppDetail) {
        this.launchAppDetail = launchAppDetail;
    }

    //推广广告
    public void promotionCutBanner(String promotionId) {
        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "0" : AccountHelper.getUserId();
        homeService.promotionCutBanner(promotionId, userId , "1")
                .subscribe();
    }

    public CategoryItem getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(CategoryItem selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public boolean isRefreshing() {
        return refreshing;
    }

    public void setRefreshing(boolean refreshing) {
        this.refreshing = refreshing;
    }
}
