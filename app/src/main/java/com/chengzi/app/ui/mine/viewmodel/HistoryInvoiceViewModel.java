package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.HistoryInvoiceBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

/**
 * 开票历史
 *
 * @ClassName:HistoryInvoiceViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/8 0008   17:37
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */

public class HistoryInvoiceViewModel extends BaseViewModel implements IRequest {

    private MineService mineService;

    public final MutableLiveData<List<HistoryInvoiceBean>> historyInvoiceLive = new MutableLiveData<>();

    public HistoryInvoiceViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.invoiceHistory(AccountHelper.getToken(), AccountHelper.getUserId(),
                String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<HistoryInvoiceBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<HistoryInvoiceBean>> responseBean) {
                        historyInvoiceLive.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        historyInvoiceLive.postValue(new ArrayList<>());
                    }
                });
    }
}