package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.ExtensionRankBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

/**
 * 咨询师推广排行
 *
 * @ClassName:PromoteRankViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/5/8 0008   16:01
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/8 0008 handongkeji All rights reserved.
 */

public class PromoteRankViewModel extends BaseViewModel {

    public final MutableLiveData<ResponseBean<List<ExtensionRankBean>>> extensionRankLiveData = new MutableLiveData<>();

    private MineDoctorOrCounselorService mineService;

    public PromoteRankViewModel() {
        mineService = Api.getApiService(MineDoctorOrCounselorService.class);
    }

    //城市id
    private String city_id = PreferenceManager.getPreference(PreferenceManager.CITY_ID);


    //咨询师推广排行
    public void extensionRank(String city_id) {
        mineService.extensionRank(AccountHelper.getToken(), AccountHelper.getUserId(), city_id)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        extensionRankLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }
}