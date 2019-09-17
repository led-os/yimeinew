package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.bean.ChooseAppointmentBean;
import com.chengzi.app.ui.homepage.model.DoctorHomeService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentViewModel extends BaseViewModel {

    public final MutableLiveData<ResponseBean<ChooseAppointmentBean>> postAppointmentLive = new MutableLiveData<>();

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> mobile = new ObservableField<>();
    public final ObservableField<String> supplement = new ObservableField<>();

    private long appointmentTime;

    private String targetId;

    //  提交预约信息
    public void postAppointment() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Params params = Params.newParams()
                .put("user_id", AccountHelper.getUserId())
                .put("to_id", targetId)
                .put("date", sdf.format(new Date(appointmentTime)))
                .put("mobile", mobile.get())
                .put("name", name.get())
                .put("content", supplement.get());

        Api.getApiService(DoctorHomeService.class)
                .postAppointment(params.params())
                .subscribe(new SimpleObserver<ResponseBean<ChooseAppointmentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<ChooseAppointmentBean> responseBean) {
                        postAppointmentLive.postValue(responseBean);
                    }
                });
    }

    public long getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(long appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
