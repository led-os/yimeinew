package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.model.MineService;

/**
 * 规则和帮助
 *
 * @ClassName:HelpViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/23 0023   13:42
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/23 0023 handongkeji All rights reserved.
 */

public class FeedBackViewModel extends BaseViewModel {

    //手机号  内容
    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> content = new ObservableField<>();


    //意见反馈添加
    public final MutableLiveData<ResponseBean<String>> feedbackLiveData = new MutableLiveData<>();

    private final MineService mineService;

    public FeedBackViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    //意见反馈添加
    public void feedback() {
        mineService.feedback(AccountHelper.getUserId(), mobile.get(), content.get())
                .subscribe(new SimpleObserver<ResponseBean<String>>() {
                    @Override
                    public void onSuccess(ResponseBean<String> bean) {
                        feedbackLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                    }
                });
    }
}