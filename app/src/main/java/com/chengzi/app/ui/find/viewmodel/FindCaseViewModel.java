package com.chengzi.app.ui.find.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.cases.viewmodel.CaseBaseViewModel;
import com.chengzi.app.ui.find.bean.FindBaseBean;
import com.chengzi.app.ui.find.model.FindService;
import com.chengzi.app.ui.home.bean.CaseBean;

import java.util.List;

public class FindCaseViewModel extends CaseBaseViewModel implements IRequest {

    public final MutableLiveData<List<FindBaseBean<CaseBean>>> casesLive = new MutableLiveData<>();

    private final FindService findService;

    public FindCaseViewModel() {
        findService = Api.getApiService(FindService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "" : AccountHelper.getUserId();
        findService.findCase("history_cases", userId)
                .subscribe(new SimpleObserver<ResponseBean<List<FindBaseBean<CaseBean>>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<FindBaseBean<CaseBean>>> findBaseBeanResponseBean) {
                        casesLive.postValue(findBaseBeanResponseBean.getData());
                    }
                });
    }
}
