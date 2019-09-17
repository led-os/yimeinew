package com.chengzi.app.ui.privaterefer.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.os.CountDownTimer;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.privaterefer.bean.ReferChoseBean;
import com.chengzi.app.ui.privaterefer.model.ReferService;

public class WaitForMatchViewModel extends BaseViewModel {

    public final MutableLiveData<ReferChoseBean> choseLive = new MutableLiveData<>();
    public final MutableLiveData<Long> timerLive = new MutableLiveData<>();
    public final MutableLiveData<Boolean> timerFinishLive = new MutableLiveData<>();
    public final MutableLiveData<DoctorBean> chooseSheetUserLive = new MutableLiveData<>();

    private int type;   // 0 私享咨询  1 在线诊断
    private String referId;     //  私享咨询 或 在线诊断的 id
    //    private long startTime;
    private boolean cancel = true;     //  是否取消

    private String selectedAccid;   //  被选择的医生/咨询师的accid
    public final ObservableBoolean requestCallPermissionDoc = new ObservableBoolean();
    private final ReferService referService;
    private CountDownTimer timer;
    private boolean countDowning;   //  true 正在倒计时

    public WaitForMatchViewModel() {
        referService = Api.getApiService(ReferService.class);
    }

    //  查询是否有医生/咨询师 抢单
    public void sheetChoseList() {
        String userId = AccountHelper.getUserId();
        referService.sheetChoseList(userId, referId)
                .subscribe(new SimpleObserver<ResponseBean<ReferChoseBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<ReferChoseBean> responseBean) {
                        choseLive.postValue(responseBean.getData());
                    }
                });
    }

    //  开启60秒倒计时
    public void startTimer() {
        if (timer != null) {
            return;
        }
        countDowning = true;
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerLive.postValue(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                timerFinishLive.postValue(true);
            }
        };
        timer.start();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            countDowning = false;
        }
    }

    //  取消 私享咨询/ 在线面诊
    public void cancelSheet() {
        if (!cancel) {
            return;
        }
        String userId = AccountHelper.getUserId();
        referService.sheetCancel(userId, referId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {

                    }
                });
    }

    //  选择抢单的医生/咨询师
    public void chooseSheetUsers(DoctorBean doctorBean) {
        String targetUserId = doctorBean.getId();
        String targetUserType = doctorBean.getType() + "";
        referService.chooseSheetUsers(AccountHelper.getToken(), AccountHelper.getUserId(), targetUserId, targetUserType
                , referId, type + "")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        chooseSheetUserLive.postValue(doctorBean);
                    }
                });
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getReferId() {
        return referId;
    }

    public void setReferId(String referId) {
        this.referId = referId;
    }

//    public long getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(long startTime) {
//        this.startTime = startTime;
//    }

    public boolean isCancel() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    public String getSelectedAccid() {
        return selectedAccid;
    }

    public void setSelectedAccid(String selectedAccid) {
        this.selectedAccid = selectedAccid;
    }

    public boolean isCountDowning() {
        return countDowning;
    }

    public void setCountDowning(boolean countDowning) {
        this.countDowning = countDowning;
    }
}
