package com.chengzi.app.ui.discover.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.model.DiscoverService;

import java.util.List;

public class ForumViewModel extends BaseForumViewModel implements IRequest {

    public final MutableLiveData<List<ForumBean>> forumListLive = new MutableLiveData<>();

    private final DiscoverService discoverService;
    private String type;   //  身份类型 类型 1-用户 2-医生 3-咨询师 4-机构 (必）

    public ForumViewModel() {
        discoverService = Api.getApiService(DiscoverService.class);
    }

    @Override
    public void onRequest(int currentPage, int pageSize) {
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "" : AccountHelper.getUserId();
        discoverService.forumList(userId, type, String.valueOf(pageSize), String.valueOf(currentPage))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<ForumBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<ForumBean>> pageBeanResponseBean) {
                        forumListLive.postValue(pageBeanResponseBean.getData().getData());
                    }
                });
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
