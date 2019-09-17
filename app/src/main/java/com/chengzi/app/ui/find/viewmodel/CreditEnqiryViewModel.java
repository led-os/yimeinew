package com.chengzi.app.ui.find.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.find.bean.CreditSearchDetailBean;
import com.chengzi.app.ui.find.model.FindService;

import java.util.List;

/**
 * 信用查询
 *
 * @ClassName:CreditEnqiryViewModel
 * @PackageName:com.yimei.app.ui.find.viewmodel
 * @Create On 2019/4/19 0019   16:35
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class CreditEnqiryViewModel extends BaseViewModel {
    //信用列表 信用详情
    public final MutableLiveData<List<CreditSearchDetailBean>> creditSearchListLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean<CreditSearchDetailBean>> creditSearchDetailLiveData = new MutableLiveData<>();
    public final ObservableField<String> searchContent = new ObservableField<>();

    private final FindService findService;

    public CreditEnqiryViewModel() {
        findService = Api.getApiService(FindService.class);
    }

    public void creditSearchList() {
        findService.creditSearchList(AccountHelper.getToken(), searchContent.get())
                .subscribe(new SimpleObserver<ResponseBean<List<CreditSearchDetailBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CreditSearchDetailBean>> findBaseBeanResponseBean) {
                        creditSearchListLiveData.postValue(findBaseBeanResponseBean.getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        creditSearchDetailLiveData.postValue(null);
                    }
                });
    }

    public void creditSearchDetail(String search_id) {
        findService.creditSearchDetail(AccountHelper.getToken(), search_id)
                .subscribe(new SimpleObserver<ResponseBean<CreditSearchDetailBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CreditSearchDetailBean> searchDetailBean) {
                        creditSearchDetailLiveData.postValue(searchDetailBean);
                    }
                });
    }
}