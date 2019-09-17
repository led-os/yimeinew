package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.mine.bean.RecordListBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

/**
 * 我的vip
 *
 * @ClassName:MyVipViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/24 0024   14:04
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/24 0024 handongkeji All rights reserved.
 */
public class BeautifulProfileViewModel extends BaseViewModel implements IRequest {

    //美丽档案列表
    public final MutableLiveData<List<RecordListBean>> recordListLiveData = new MutableLiveData<>();

    private final MineService mineService;

    public BeautifulProfileViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.recordList(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<RecordListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<RecordListBean>> bean) {
                        recordListLiveData.postValue(bean.getData().getData());
                    }
                });
    }
}