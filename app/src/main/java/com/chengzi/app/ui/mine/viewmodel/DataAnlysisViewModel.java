package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.DataAnalysisBean;
import com.chengzi.app.ui.mine.model.MineHospitalService;

/**
 * 数据分析
 *
 * @ClassName:DataAnlysisViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/5/21 0021   09:43
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/21 0021 handongkeji All rights reserved.
 */
public class DataAnlysisViewModel extends BaseViewModel {
    public final MutableLiveData<ResponseBean<DataAnalysisBean>> responseBeanLiveData = new MutableLiveData<>();

    public void dataAnalysis() {
        Api.getApiService(MineHospitalService.class).dataAnalysis(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<DataAnalysisBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<DataAnalysisBean> dataAnalysisBeanResponseBean) {
                        responseBeanLiveData.postValue(dataAnalysisBeanResponseBean);
                    }
                });
    }
}
