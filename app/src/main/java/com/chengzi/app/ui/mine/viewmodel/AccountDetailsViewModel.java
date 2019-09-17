package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.AccountFlowBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.HashMap;
import java.util.List;

/**
 * 账户明细-->支付0/补给金1
 *
 * @ClassName:AccountDetailsViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class AccountDetailsViewModel extends BaseViewModel implements IRequest {

    public final ObservableField<String> type = new ObservableField<>();

    public final MutableLiveData<List<AccountFlowBean.DataBean>> accountLive = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", type.get())//1提现，2补给
                .put("page", currentPage + "")
                .put("limit", pageSize + "")
                .params();
        Api.getApiService(MineService.class).accountFlow(params).subscribe(new SimpleObserver<ResponseBean<AccountFlowBean>>() {
            @Override
            public void onSuccess(ResponseBean<AccountFlowBean> accountFlowBeanResponseBean) {
                if (accountFlowBeanResponseBean != null && accountFlowBeanResponseBean.getData() != null) {
                    accountLive.postValue(accountFlowBeanResponseBean.getData().getData());
                }
            }
        });
    }
}