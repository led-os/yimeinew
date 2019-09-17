package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.BindHistoryBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定的历史记录
 *
 * @ClassName:BindingManageViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/10 0010   16:40
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class BindHistoryRecordViewModel extends BaseViewModel implements IRequest {

    public final MutableLiveData<List<BindHistoryBean>> bindHistoryLiveData = new MutableLiveData<>();

    private final MineDoctorOrCounselorService mineService;

    public BindHistoryRecordViewModel() {
        mineService = Api.getApiService(MineDoctorOrCounselorService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.bindHistory(AccountHelper.getToken(), AccountHelper.getUserId(),
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<BindHistoryBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<BindHistoryBean>> pageBeanResponseBean) {
                        bindHistoryLiveData.postValue(pageBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
                        super.onError(code, errorMsg);
                        bindHistoryLiveData.postValue(new ArrayList<>());
                    }
                });
    }
}