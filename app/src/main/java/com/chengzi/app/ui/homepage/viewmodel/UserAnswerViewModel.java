package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.homepage.bean.GetAnswersBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

public class UserAnswerViewModel extends BaseViewModel implements IRequest {
    public final MutableLiveData<List<GetAnswersBean>> userAnswersLiveData = new MutableLiveData<>();
    private final MineService mineService;

    public UserAnswerViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    public ObservableField<String> click_id = new ObservableField<>();
    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.getAnswers(AccountHelper.getToken(), AccountHelper.getUserId(), click_id.get(),
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<GetAnswersBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<GetAnswersBean>> responseBean) {
                        userAnswersLiveData.postValue(responseBean.getData().getData());
                    }

                });
    }
}
