package com.chengzi.app.ui.cases.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.cases.model.CaseService;
import com.chengzi.app.ui.mine.bean.CaseNoteListBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;
import com.chengzi.app.utils.AlwaysAliveObservable;

import java.util.List;

public class CaseDetailViewModel extends BaseViewModel {

    public final MutableLiveData<List<CaseNoteListBean>> caseNoteListLive = new MutableLiveData<>();//案例详情
    public final MutableLiveData<Integer> caseThumbUpLive = new MutableLiveData<>();//点赞

    public final AlwaysAliveObservable thumbUpObservable = new AlwaysAliveObservable();
    public final ObservableField<String> likeNumber = new ObservableField<>("0");  //  点赞数量
    public final ObservableBoolean like = new ObservableBoolean();    //  是否点赞

    private String caseId;  //  案例id

    private String promotionId;     //  推广id

    /*变美过程---美丽日记列表*/
    public void caseNoteList() {
        String userId = TextUtils.isEmpty(AccountHelper.getUserId())?"0":AccountHelper.getUserId();
        Api.getApiService(MineHospitalService.class).caseNoteList(AccountHelper.getToken(),userId
                , caseId, "1",
                String.valueOf(Integer.MAX_VALUE))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<CaseNoteListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<CaseNoteListBean>> responseBean) {
                        caseNoteListLive.postValue(responseBean.getData().getData());
                    }
                });
    }

    public void caseClickLike() {
        boolean thumbUp = like.get();
        Api.getApiService(CaseService.class)
                .caseThumbUp(caseId, AccountHelper.getUserId(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        caseThumbUpLive.postValue(thumbUp ? 0 : 1);
                    }
                });
    }


    public void promotionCut() {

        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId())?"0":AccountHelper.getUserId();
        Api.getApiService(CaseService.class)
                .promotionCutCasesSearch(promotionId,userId ,"1")
                .subscribe();
    }


    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
}
