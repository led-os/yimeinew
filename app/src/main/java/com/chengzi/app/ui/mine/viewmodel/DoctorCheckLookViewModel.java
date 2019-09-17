package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.LookHospitalOrderBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看 医院身份
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class DoctorCheckLookViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<LookHospitalOrderBean>> lookOrderLiveData = new MutableLiveData<>();

    private MineHospitalService mineService;

    public DoctorCheckLookViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    public ObservableField<String> user_type = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.lookHospitalOrder(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(),
                user_type.get(), user_type.get(),
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<LookHospitalOrderBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<LookHospitalOrderBean>> responseBean) {
                        lookOrderLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        lookOrderLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}