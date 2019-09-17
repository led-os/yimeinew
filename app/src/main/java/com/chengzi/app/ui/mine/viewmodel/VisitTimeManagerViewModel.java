package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.AppointmentTimeManageBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;

import java.util.List;

public class VisitTimeManagerViewModel extends BaseViewModel {

    public final MutableLiveData<List<AppointmentTimeManageBean>> appointmentTimeManageLiveData = new MutableLiveData<>();
//    public final MutableLiveData<ResponseBean> setAppointmentTimeManageLiveData = new MutableLiveData<>();

    private String targetUserId;

    private final MineDoctorOrCounselorService mineService;

    public VisitTimeManagerViewModel() {
        mineService = Api.getApiService(MineDoctorOrCounselorService.class);
    }

    /**
     * 坐诊时间管理展示
     */
    public void appointmentTimeManage(String startTime,String endTime) {
        mineService.AppointmentTimeManage(targetUserId,startTime,endTime)
                .subscribe(new SimpleObserver<ResponseBean<List<AppointmentTimeManageBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<AppointmentTimeManageBean>> listResponseBean) {
                        appointmentTimeManageLiveData.postValue(listResponseBean.getData());
                    }
                });
    }


//    /**
//     * 坐诊时间管理 修改
//     *
//     * @param date 2019-04-29
//     * @param day  pm|am pm下午 am上午
//     */
//    public void appointmentTimeManage(String date, String day) {
//        mineService.AppointmentTimeManage(AccountHelper.getUserId(), AccountHelper.getUserId(), date, day)
//                .subscribe(new SimpleObserver<ResponseBean>() {
//                    @Override
//                    public void onSuccess(ResponseBean listResponseBean) {
//                        setAppointmentTimeManageLiveData.postValue(listResponseBean);
//                    }
//                });
//    }


    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }
}
