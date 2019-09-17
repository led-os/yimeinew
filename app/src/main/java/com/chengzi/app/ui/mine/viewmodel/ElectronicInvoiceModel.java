package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.ElectronicInvoiceBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

/**
 * 电子发票的model
 * 电子发票的列表
 *
 * @ClassName:ElectronicInvoiceModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/28 002817:06
 * @Site:http://www.handongkeji.com
 * @author: wanghongfu
 * @Copyrights 2019/4/28 0028 handongkeji All rights reserved.
 */
public class ElectronicInvoiceModel extends BaseViewModel implements IRequest {
    //可开发票列表
    public final MutableLiveData<List<ElectronicInvoiceBean>> electronicLive = new MutableLiveData<>();
    //开发票
    public final MutableLiveData<ResponseBean> invoiceAddLiveData = new MutableLiveData<>();

    //发票抬头 税号   电子邮箱
    public final ObservableField<String> rise = new ObservableField<>();
    public final ObservableField<String> identify_number = new ObservableField<>();
    public final ObservableField<String> email = new ObservableField<>();

    public final MineService mineService;

    public ElectronicInvoiceModel() {
        mineService = Api.getApiService(MineService.class);
    }

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.invoiceList(AccountHelper.getToken(),  AccountHelper.getUserId(), String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ElectronicInvoiceBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ElectronicInvoiceBean>> responseBean) {
                        electronicLive.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        electronicLive.postValue(new ArrayList<>());
                    }
                });
    }

    /*开发票*/
    public void invoiceAdd(List<String> payment_ids, String rise_type, String invoice_content) {
        mineService.invoiceAdd(AccountHelper.getToken(), AccountHelper.getUserId(), payment_ids, rise_type,
                rise.get(), identify_number.get(),
                invoice_content, email.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        invoiceAddLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                    }
                });
    }
}
