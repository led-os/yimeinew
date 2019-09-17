package com.chengzi.app.ui.search.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.search.bean.SearchBean;
import com.chengzi.app.ui.search.model.SearchService;

import java.util.List;

public class SearchViewModel extends BaseViewModel {

    public final ObservableField<String> keyword = new ObservableField<>();

    public final MutableLiveData<List<SearchBean>> searchHistoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<SearchBean>> searchHotLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> delHistoryLive = new MutableLiveData<>();

    private final SearchService searchService;

    public SearchViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    //  加载历史搜索记录
    public void searchHistory() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        String userId = AccountHelper.getUserId();
        String token = AccountHelper.getToken();
        searchService.searchHistory(userId, token)
                .subscribe(new SimpleObserver<ResponseBean<List<SearchBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<SearchBean>> listResponseBean) {
                        searchHistoryLive.postValue(listResponseBean.getData());
                    }
                });
    }

    //  保存历史搜索记录
    public void saveHistory() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        String userId = AccountHelper.getUserId();
        String token = AccountHelper.getToken();
        searchService.searchKey(userId,  keyword.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }

    //  删除所有历史搜索记录
    public void delAllHistory() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        String userId = AccountHelper.getUserId();
        String token = AccountHelper.getToken();
        searchService.delHistorySearch(userId,token)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        delHistoryLive.postValue(true);
                    }
                });
    }

    public void searchHot() {
        searchService.hotSearch()
                .subscribe(new SimpleObserver<ResponseBean<List<SearchBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<SearchBean>> listResponseBean) {
                        searchHotLive.postValue(listResponseBean.getData());
                    }
                });
    }
}
