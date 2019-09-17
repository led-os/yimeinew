package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.SeeOrderBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

/**
 * 投诉我的(医生/咨询师)
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class CheckPlaceOrderModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<SeeOrderBean>> seeOrderLiveData = new MutableLiveData<>();

    private final MineService mineService;

    public CheckPlaceOrderModel() {
        mineService = Api.getApiService(MineService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.seeOrder(AccountHelper.getToken(), AccountHelper.getUserId(),
                String.valueOf(AccountHelper.getIdentity()), pageSize, currentPage)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<SeeOrderBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<SeeOrderBean>> pageBeanResponseBean) {
                        seeOrderLiveData.postValue(pageBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        seeOrderLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}