package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HomeRecommandBean;
import com.chengzi.app.ui.home.model.HomeService;
import com.chengzi.app.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommandViewModel extends BaseViewModel {

    public final MutableLiveData<List<HomeRecommandBean<GoodBean>>> homeRecommandLive = new MutableLiveData<>();
    //  下拉刷新用
    public final MutableLiveData<Boolean> refreshLive = new MutableLiveData<>();

    public final List<HomeRecommandBean> items = new ArrayList<>();

    public final MutableLiveData<List<GoodBean>> likeGoodsLive = new MutableLiveData<>();

    private HomeService homeService;

    public RecommandViewModel() {
        homeService = Api.getApiService(HomeService.class);
    }

    //  首页推广
    public void homeRecommand() {
        HashMap<String, String> params = Params.newParams()
                .put("city_id", PreferenceManager.getPreference(PreferenceManager.CITY_ID))
                .params();
        homeService.homeRecommandList(params)
                .subscribe(new SimpleObserver<ResponseBean<List<HomeRecommandBean<GoodBean>>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<HomeRecommandBean<GoodBean>>> listResponseBean) {
                        homeRecommandLive.postValue(listResponseBean.getData());
                    }
                });

    }

    //  猜你喜欢
    public void likeGoodsList() {
        String token = TextUtils.isEmpty(AccountHelper.getToken()) ? null : AccountHelper.getToken();
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? null : AccountHelper.getUserId();
        Params params = Params.newParams()
                .put("token", token)
                .put("user_id", userId)
                .put("city_id", PreferenceManager.getPreference(PreferenceManager.CITY_ID));
        homeService.likeGoodsList(params.params())
                .subscribe(new SimpleObserver<ResponseBean<List<GoodBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<GoodBean>> listResponseBean) {
                        likeGoodsLive.postValue(listResponseBean.getData());
                    }
                });
    }

}
