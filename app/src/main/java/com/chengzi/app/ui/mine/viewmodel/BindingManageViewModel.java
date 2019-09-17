package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.BindHistoryBean;
import com.chengzi.app.ui.mine.bean.BindSearchBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定管理
 *
 * @ClassName:BindingManageViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/10 0010   16:40
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class BindingManageViewModel extends BaseViewModel {

    //当前绑定管理
    public final MutableLiveData<List<BindHistoryBean>> bindHospitalLiveData = new MutableLiveData<>();
    //绑定新机构列表
    public final MutableLiveData<List<BindSearchBean>> bindSearchLiveData = new MutableLiveData<>();
    //绑定新机构
    public final MutableLiveData<ResponseBean> bindLiveData = new MutableLiveData<>();
    //解绑
    public final MutableLiveData<ResponseBean> bindClearLiveData = new MutableLiveData<>();
    public final ObservableField<String> searchContent = new ObservableField<>();

//    private String token = AccountHelper.getToken();
//    private String user_id = AccountHelper.getUserId();
//    private String user_type = String.valueOf(AccountHelper.getIdentity());

    private final MineDoctorOrCounselorService mineService;

    public BindingManageViewModel() {
        mineService = Api.getApiService(MineDoctorOrCounselorService.class);
    }

    public void bindSearch() {
        mineService.bindSearch(AccountHelper.getToken(), AccountHelper.getUserId(), searchContent.get())
                .subscribe(new SimpleObserver<ResponseBean<List<BindSearchBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<BindSearchBean>> pageBeanResponseBean) {
                        bindSearchLiveData.postValue(pageBeanResponseBean.getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        bindSearchLiveData.postValue(new ArrayList<>());
                    }
                });
    }

    //已绑定医院
    public void bindHospital() {
        mineService.bindHospital(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<BindHistoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<BindHistoryBean>> pageBeanResponseBean) {
                        bindHospitalLiveData.postValue(pageBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        bindHospitalLiveData.postValue(new ArrayList<>());
                    }
                });
    }

    //绑定
    public void bind(String bind_id) {
        mineService.bind(AccountHelper.getToken(), AccountHelper.getUserId(), bind_id, String.valueOf(AccountHelper.getIdentity()))
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        bindLiveData.postValue(responseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        bindLiveData.postValue(null);
                    }
                });
    }

    //解除绑定
    public void bindClear(String bind_id) {
        mineService.bindClear(AccountHelper.getToken(), AccountHelper.getUserId(), bind_id, String.valueOf(AccountHelper.getIdentity()))
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean pageBeanResponseBean) {
                        bindClearLiveData.postValue(pageBeanResponseBean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        bindClearLiveData.postValue(null);
                    }
                });
    }
}