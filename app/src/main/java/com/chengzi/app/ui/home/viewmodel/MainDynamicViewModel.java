package com.chengzi.app.ui.home.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.HomeArticleBean;
import com.chengzi.app.ui.home.bean.TradeListBean;
import com.chengzi.app.ui.home.model.HomeService;

import java.util.List;

public class MainDynamicViewModel extends BaseViewModel {
    //首页- 交易动态列表 学术动态列表
    public final MutableLiveData<List<TradeListBean>> tradeListLive = new MutableLiveData<>();
    public final MutableLiveData<List<HomeArticleBean>> homeArticleLive = new MutableLiveData<>();
    private HomeService homeService;

    public MainDynamicViewModel() {
        homeService = Api.getApiService(HomeService.class);
    }

    //  首页- 交易动态列表
    public void tradeList() {
        homeService.tradeList()
                .subscribe(new SimpleObserver<ResponseBean<List<TradeListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<TradeListBean>> listResponseBean) {
                        tradeListLive.postValue(listResponseBean.getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        tradeListLive.postValue(null);
                    }
                });

    }

    public void homeArticle() {
        homeService.homeArticle()
                .subscribe(new SimpleObserver<ResponseBean<List<HomeArticleBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<HomeArticleBean>> listResponseBean) {
                        homeArticleLive.postValue(listResponseBean.getData());
                    }
                });

    }
}