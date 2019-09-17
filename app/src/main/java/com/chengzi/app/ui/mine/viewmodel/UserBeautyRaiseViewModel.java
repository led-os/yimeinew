package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.UserPlanOrderDetailsBean;
import com.chengzi.app.ui.mine.bean.UserPlanOrderListBean;
import com.chengzi.app.ui.mine.bean.VerificationOrderBean;
import com.chengzi.app.ui.mine.model.BeauytRaiseOrderService;

import java.util.HashMap;
import java.util.List;

/**
 * 美人筹
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class UserBeautyRaiseViewModel extends BaseViewModel implements IRequest {

    //美人筹订单列表
    public final MutableLiveData<List<UserPlanOrderListBean>> userPlanOrderListLiveData = new MutableLiveData<>();
    //美人筹订单详情
    public final MutableLiveData<ResponseBean<UserPlanOrderDetailsBean>> userPlanOrderDetailsLiveData = new MutableLiveData<>();
    //普通用户美人筹取消订单
    public final MutableLiveData<ResponseBean> userPlanChoiceHospitalLiveData = new MutableLiveData<>();
    //普通发起人选择医院
    public final MutableLiveData<ResponseBean> userPlanOrderCancelLiveData = new MutableLiveData<>();
    //机构取消参与美人筹
    public final MutableLiveData<ResponseBean> cancelJoinToPlanLiveData = new MutableLiveData<>();
    //机构确认使用
    public final MutableLiveData<ResponseBean> orderConfirmUseLiveData = new MutableLiveData<>();
    //验证订单
    public final MutableLiveData<ResponseBean<VerificationOrderBean>> verificationOrderLiveData = new MutableLiveData<>();

    public final ObservableBoolean requestCallPermissionDoc = new ObservableBoolean();

    private final BeauytRaiseOrderService beauytRaiseOrderService;

    public UserBeautyRaiseViewModel() {
        beauytRaiseOrderService = Api.getApiService(BeauytRaiseOrderService.class);
    }

    //-1全部 0待付款 1未达成 2待使用 3已完成 4已取消
    public int status = -1;
    // 1-进行中，2-待使用，3-已完成
    public int h_status = -1;

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();

    @Override  //列表
    public void onRequest(int currentPage, int pageSize) {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", AccountHelper.getToken());
        map.put("user_id", AccountHelper.getUserId());
        map.put("limit", String.valueOf(pageSize));
        map.put("page", String.valueOf(currentPage));
        if (status != -1)
            map.put("status", String.valueOf(status));
        if (h_status != -1)
            map.put("h_status", String.valueOf(h_status));
        beauytRaiseOrderService.userPlanOrderList(map)
                .subscribe(new SimpleObserver<ResponseBean<PageBean<UserPlanOrderListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<UserPlanOrderListBean>> bean) {
                        userPlanOrderListLiveData.postValue(bean.getData().getData());
                    }
                });
    }

    //详情
    public void userPlanOrderDetails(String id) {
        beauytRaiseOrderService.userPlanOrderDetails(AccountHelper.getToken(), AccountHelper.getUserId(), id)
                .subscribe(new SimpleObserver<ResponseBean<UserPlanOrderDetailsBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserPlanOrderDetailsBean> bean) {
                        userPlanOrderDetailsLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        userPlanOrderDetailsLiveData.postValue(null);
                    }
                });
    }

    //普通用户取消订单
    public void userPlanOrderCancel(String id) {
        beauytRaiseOrderService.userPlanOrderCancel(AccountHelper.getToken(), AccountHelper.getUserId(), id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        userPlanOrderCancelLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        userPlanOrderCancelLiveData.postValue(null);
                    }
                });
    }


    //发起人选择医院
    public void userPlanChoiceHospital(String id, String hospital_id) {
        beauytRaiseOrderService.userPlanChoiceHospital(AccountHelper.getToken(), AccountHelper.getUserId(), id, hospital_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        userPlanChoiceHospitalLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        userPlanChoiceHospitalLiveData.postValue(null);
                    }
                });
    }

    //机构取消参与美人筹
    public void cancelJoinToPlan(String plan_order_id) {
        beauytRaiseOrderService.cancelJoinToPlan(AccountHelper.getToken(), AccountHelper.getUserId(), plan_order_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        cancelJoinToPlanLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        cancelJoinToPlanLiveData.postValue(null);
                    }
                });
    }

    //机构取消参与美人筹 //
    //订单类型，1-普通，拼购，秒杀 2-美人筹（必须）
    public void orderConfirmUse(String id, String order_type) {
        beauytRaiseOrderService.orderConfirmUse(AccountHelper.getToken(), AccountHelper.getUserId(), id, order_type, order_type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        orderConfirmUseLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        orderConfirmUseLiveData.postValue(null);
                    }
                });
    }

    //验证订单
    public void verificationOrder(String order_code) {
        beauytRaiseOrderService.verificationOrder(AccountHelper.getToken(), AccountHelper.getUserId(), order_code)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        verificationOrderLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        verificationOrderLiveData.postValue(null);
                    }
                });
    }
}