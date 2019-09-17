package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.CompetitiveAnalysisBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

/**
 * 数据竞对分析
 *
 * @ClassName:UserBeautyRaiseViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/3 0003   09:59
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class CompetitiveAnalysisViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<CompetitiveAnalysisBean> competitiveAnalysisLiveData = new MutableLiveData<>();

    //2-医生 3-咨询师
    public ObservableField<String> user_type = new ObservableField<>();

    /// 数据竞对分析
    @Override
    public void onRequest(int currentPage, int pageSize) {
        Api.getApiService(MineHospitalService.class)
                .competitiveAnalysis(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(),
                        user_type.get(), String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<CompetitiveAnalysisBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<CompetitiveAnalysisBean> listResponseBean) {
                        competitiveAnalysisLiveData.postValue(listResponseBean.getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        competitiveAnalysisLiveData.postValue(null);
                    }
                });
    }
}