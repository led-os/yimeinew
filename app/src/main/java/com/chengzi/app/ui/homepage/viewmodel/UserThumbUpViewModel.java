package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.homepage.bean.ClickLikeListBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

public class UserThumbUpViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<ClickLikeListBean>> clickLikeListLiveData = new MutableLiveData<>();
    private final MineService mineService;

    public UserThumbUpViewModel() {
        mineService = Api.getApiService(MineService.class);
    }


    public ObservableField<String> click_id = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.clickLikeList(AccountHelper.getToken(), click_id.get(),
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ClickLikeListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ClickLikeListBean>> responseBean) {
                        clickLikeListLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        clickLikeListLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}
