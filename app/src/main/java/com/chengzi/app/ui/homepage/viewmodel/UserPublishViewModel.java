package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.viewmodel.BaseForumViewModel;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

/**
 * 发表
 *
 * @ClassName:UserPublishViewModel
 * @PackageName:com.yimei.app.ui.homepage.viewmodel
 * @Create On 2019/5/24 0024   14:50
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/24 0024 handongkeji All rights reserved.
 */
public class UserPublishViewModel extends BaseForumViewModel implements IRequest {

    public final MutableLiveData<List<ForumBean>> userIndexLiveData = new MutableLiveData<>();

    private String targetId;
    private final MineService mineService;

    public UserPublishViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        mineService.userIndex(AccountHelper.getToken(), AccountHelper.getUserId(), targetId,
                String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ForumBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ForumBean>> responseBean) {
                        userIndexLiveData.postValue(responseBean.getData().getData());
                    }
                });
    }


    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
}
