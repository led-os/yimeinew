package com.chengzi.app.ui.cases.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import android.util.Pair;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.event.CaseThumbUpEvent;
import com.chengzi.app.ui.cases.model.CaseService;
import com.chengzi.app.ui.home.bean.CaseBean;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;
import com.chengzi.app.utils.AlwaysAliveObservable;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CaseBaseViewModel extends BaseViewModel {

    public final AlwaysAliveObservable thumbUpObservable = new AlwaysAliveObservable();     //  点赞
    public final AlwaysAliveObservable commentObservable = new AlwaysAliveObservable();     //  评论
    public final AlwaysAliveObservable viewObservable = new AlwaysAliveObservable();        //  浏览量

    public final MutableLiveData<List<CaseNoteListBean>> caseDetailLive = new MutableLiveData<>();

    public final MutableLiveData<Pair<CaseBean, Integer>> caseThumbUpLive = new MutableLiveData<>();

    public void setLifecycleOwner(LifecycleOwner owner) {
        caseThumbUpLive.observe(owner, pair -> {
            CaseBean caseBean = pair.first;
            Integer integer = pair.second;
//            caseBean.setLike(integer == 1);
//            caseBean.setLike_number(caseBean.getLike_number() + (integer == 1 ? 1 : -1));
            thumbUpObservable.update();
            EventBus.getDefault().post(new CaseThumbUpEvent(caseBean.getId(), integer == 1));
        });
    }

    public void caseClickLike(CaseBean caseBean) {
        boolean thumbUp = caseBean.isLike();
        Api.getApiService(CaseService.class)
                .caseThumbUp(caseBean.getId(), AccountHelper.getUserId(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        caseThumbUpLive.postValue(new Pair<>(caseBean, thumbUp ? 0 : 1));
                    }
                });
    }

    public final MutableLiveData<ResponseBean<String>> caseThumbUpLiveData = new MutableLiveData<>();

    public void caseClickLike1(CaseBean caseBean) {
        Api.getApiService(CaseService.class)
                .caseThumbUp(caseBean.getId(), AccountHelper.getUserId(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<String>>() {
                    @Override
                    public void onSuccess(ResponseBean<String> responseBean) {
                        caseThumbUpLiveData.postValue(responseBean);
                    }
                });
    }

    /*变美过程---美丽日记列表*/
    public void getCaseDetail(String caseId) {
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "0" : AccountHelper.getUserId();
        Api.getApiService(MineHospitalService.class).caseNoteList(AccountHelper.getToken(), userId
                , caseId, "1", "1")
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseNoteListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CaseNoteListBean>> responseBean) {
                        caseDetailLive.postValue(responseBean.getData().getData());
                    }
                });
    }


}
