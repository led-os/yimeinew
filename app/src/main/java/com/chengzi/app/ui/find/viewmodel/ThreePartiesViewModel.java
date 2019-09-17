package com.chengzi.app.ui.find.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.find.bean.ArticleManageBean;
import com.chengzi.app.ui.find.model.FindService;

import java.util.ArrayList;
import java.util.List;

/**
 * 普通用户 ->我的预约
 *
 * @ClassName:MyBookingModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/8 0008   14:30
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class ThreePartiesViewModel extends BaseViewModel implements IRequest {

    //选中的position
//    public final ObservableInt selectedProvincePos = new ObservableInt();

    public final MutableLiveData<List<ArticleManageBean>> articleManageLive = new MutableLiveData<>();

    public ObservableField<String> type = new ObservableField<>();

    @Override
    public void onRequest(int currentPage, int pageSize) {
        Api.getApiService(FindService.class)
                .articleManage(String.valueOf(currentPage), String.valueOf(pageSize), type.get())
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ArticleManageBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ArticleManageBean>> responseBeanResponseBean) {
                        articleManageLive.postValue(responseBeanResponseBean.getData().getData());
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        articleManageLive.postValue(new ArrayList<>());
                    }
                });
    }
}