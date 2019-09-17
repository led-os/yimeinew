package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.RuleListBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

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

public class HelpViewModel extends BaseViewModel {

    //规则帮助列表
    public final MutableLiveData<List<RuleListBean>> ruleListLiveData = new MutableLiveData<>();

    private final MineService mineService;

    public HelpViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    //规则帮助列表
    public void ruleList() {
        int identity = AccountHelper.getIdentity();
        mineService.ruleList(AccountHelper.getToken(), AccountHelper.getUserId(),
                identity <= 0 ? "1" : String.valueOf(identity), String.valueOf(Integer.MAX_VALUE))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<RuleListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<RuleListBean>> bean) {
                        ruleListLiveData.postValue(bean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                    }
                });
    }
}