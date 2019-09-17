package com.chengzi.app.ui.search.viewmodel;

import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.search.OnDisplayChangeObserver.Display;
import com.chengzi.app.ui.search.model.SearchService;

public class SearchResultViewModel extends BaseViewModel {

    public final ObservableField<Display> display = new ObservableField<>(Display.LIST);

    public final ObservableField<String> keyword = new ObservableField<>();

    private final SearchService searchService;

    public SearchResultViewModel() {
        searchService = Api.getApiService(SearchService.class);
    }

    //  保存历史搜索记录
    public void saveHistory() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        String userId = AccountHelper.getUserId();
        searchService.searchKey(userId, keyword.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }
}
