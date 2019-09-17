package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.HospitalDoctorAppointmentBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

import java.util.ArrayList;
import java.util.List;

/**
 * 预约管理-->医院(医生预约 /医院预约)
 *
 * @ClassName:AccountDetailsViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class ReservationManageViewModel extends BaseViewModel implements IRequest {
    public final MutableLiveData<List<HospitalDoctorAppointmentBean>> hospitalDoctorAppointmentLiveData = new MutableLiveData<>();

    private final MineHospitalService mineService;

    public ReservationManageViewModel() {
        mineService = Api.getApiService(MineHospitalService.class);
    }

    //用户类型 2 医生 4 机构 （必传）
    public String type;
    //接口类型 1 医生预约 2 医院预约
    public String select;

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.hospitalDoctorAppointment(AccountHelper.getToken(), AccountHelper.getUserId(),
                type, select,
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<HospitalDoctorAppointmentBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<HospitalDoctorAppointmentBean>> responseBean) {
                        hospitalDoctorAppointmentLiveData.postValue(responseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        hospitalDoctorAppointmentLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}