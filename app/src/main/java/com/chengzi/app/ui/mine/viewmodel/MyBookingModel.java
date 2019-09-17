package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.MyAppointmentBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通用户 ->我的预约
 *
 * @ClassName:MyBookingModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/8 0008   14:30
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class MyBookingModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<MyAppointmentBean>> myAppointmentLiveData = new MutableLiveData<>();
    public final MutableLiveData<ResponseBean> cancelAppointmentLiveData = new MutableLiveData<>();

    private final MineService mineService;

    public MyBookingModel() {
        mineService = Api.getApiService(MineService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        //类型 （必传 1普通用户-我的预约， 2医生-预约我的）
        mineService.myAppointment(AccountHelper.getToken(), AccountHelper.getUserId(),
                "1", pageSize, currentPage)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<MyAppointmentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<MyAppointmentBean>> pageBeanResponseBean) {
                        myAppointmentLiveData.postValue(pageBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        myAppointmentLiveData.postValue(new ArrayList<>());
                    }
                });
    }

    /**
     * 取消预约
     *
     * @param id 预约id
     */
    public void cancelAppointment(String id) {
        //类型 （必传 1普通用户-我的预约， 2医生-预约我的）
        mineService.cancelAppointment(AccountHelper.getToken(), AccountHelper.getUserId(), id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean pageBeanResponseBean) {
                        cancelAppointmentLiveData.postValue(pageBeanResponseBean);
                    }
                });
    }
}