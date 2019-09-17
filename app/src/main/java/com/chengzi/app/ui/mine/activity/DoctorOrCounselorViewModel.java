package com.chengzi.app.ui.mine.activity;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.DoctorListBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.HashMap;
import java.util.List;

/**
 * @Desc:
 * @ClassName:DoctorOrCounselorViewModel
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/29 0029
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class DoctorOrCounselorViewModel extends BaseViewModel implements IRequest {

    public final ObservableInt type = new ObservableInt();
    //医生/咨询师列表
    public final MutableLiveData<List<DoctorListBean>> listMutableLiveData = new MutableLiveData<>();
    //审核 同意/拒绝
    public final MutableLiveData<ResponseBean> bindReviewLiveData = new MutableLiveData<>();
    //解除绑定
    public final MutableLiveData<DoctorListBean> bindClearLiveData = new MutableLiveData<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("uid", AccountHelper.getUserId())
                .put("user_id", AccountHelper.getUserId())
                .put("type", String.valueOf(type.get()))  //用户类型 2医生列表 3咨询师列表
                .put("page", currentPage + "")
                .put("limit", pageSize + "")
                .params();
        Api.getApiService(MineHospitalService.class).doctorList(params).subscribe(new SimpleObserver<ResponseBean<PageBean<DoctorListBean>>>() {
            @Override
            public void onSuccess(ResponseBean<PageBean<DoctorListBean>> pageBeanResponseBean) {
                if (pageBeanResponseBean != null && pageBeanResponseBean.getData() != null) {
                    listMutableLiveData.postValue(pageBeanResponseBean.getData().getData());
                }
            }

            @Override
            public void onError(int code, String errorMsg) {
//                super.onError(code, errorMsg);
                listMutableLiveData.postValue(null);
            }
        });
    }

    /**
     * 医生/咨询师 接收/拒绝绑定
     *
     * @param binding_id 医生/咨询师 id
     * @param status   1-已通过 2-已拒绝
     */
    public void bindReview(String binding_id, String status) { //type 2医生 3咨询师
        Api.getApiService(MineHospitalService.class)
                .bindReview(AccountHelper.getToken(), AccountHelper.getUserId(), String.valueOf(type.get()), binding_id, status).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                bindReviewLiveData.postValue(responseBean);
            }
        });
    }

    /**
     * 解除绑定
     *
     * @param //binding_id 医生/咨询师 id
     */
    public void bindClear(DoctorListBean user) {
        Api.getApiService(MineHospitalService.class).
                bindClear(AccountHelper.getToken(), AccountHelper.getUserId(), user.getUser_id(), String.valueOf(type.get())).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                bindClearLiveData.postValue(user);
            }
        });
    }
}
