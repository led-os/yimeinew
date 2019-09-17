package com.netease.nim.uikit.api.yimei;

import android.arch.lifecycle.MutableLiveData;
import android.os.CountDownTimer;
import android.util.Log;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;

public class ConsultantViewModel extends BaseViewModel {

    public final MutableLiveData<ConsultantBean> consultantLive = new MutableLiveData<>();

    private int keepTime;
    private String queryId;     //  商品或者医院的ID
    private String getConsultantsType;  //   1 商品  goods，2 医院 service_organization
    private String consultantId;    //  被标记的咨询师id
    private boolean firstConsultant;

    private final ConsultantService consultantService;

    public ConsultantViewModel() {
        consultantService = Api.getApiService(ConsultantService.class);
    }

    // 获取在线咨询师
    public void getConsultant() {
        consultantService.getConsultant(AccountHelper.getUserId(), queryId, getConsultantsType)
                .subscribe(new SimpleObserver<ResponseBean<ConsultantBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<ConsultantBean> responseBean) {
                        consultantLive.postValue(responseBean.getData());
                    }
                });
    }

    //  标记当前未回复的咨询师 不在线
    public void markConsultant() {
        consultantService.markConsultant(AccountHelper.getUserId(), queryId, getConsultantsType, consultantId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }

    private CountDownTimer timer;

    public void start() {
        if (keepTime <= 0) {
            return;
        }
        long millisInFuture = firstConsultant ? 3 * 60 * 1000 : 30 * 1000;
//        long millisInFuture = firstConsultant ? 10 * 1000 : 10 * 1000;
        timer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("aaa", getClass().getSimpleName() + "  onTick: " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                Log.d("aaa", getClass().getSimpleName() + "  onFinish: ");
                getConsultant();
                markConsultant();
            }
        };
        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getGetConsultantsType() {
        return getConsultantsType;
    }

    public void setGetConsultantsType(String getConsultantsType) {
        this.getConsultantsType = getConsultantsType;
    }

    public String getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(String consultantId) {
        this.consultantId = consultantId;
    }

    public int getKeepTime() {
        return keepTime;
    }

    public void setKeepTime(int keepTime) {
        this.keepTime = keepTime;
    }

    public boolean isFirstConsultant() {
        return firstConsultant;
    }

    public void setFirstConsultant(boolean firstConsultant) {
        this.firstConsultant = firstConsultant;
    }
}
