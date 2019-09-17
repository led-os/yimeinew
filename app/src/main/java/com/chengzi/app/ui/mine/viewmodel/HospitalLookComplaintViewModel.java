package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.LookComplaintBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看 -->投诉
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class HospitalLookComplaintViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<LookComplaintBean>> lookComplaintLiveData = new MutableLiveData<>();

    private MineHospitalService mineService;

    public HospitalLookComplaintViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    public ObservableField<String> user_type = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.lookComplaint(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(),
                user_type.get(), user_type.get(),
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<LookComplaintBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<LookComplaintBean>> responseBean) {
                        lookComplaintLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        lookComplaintLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}