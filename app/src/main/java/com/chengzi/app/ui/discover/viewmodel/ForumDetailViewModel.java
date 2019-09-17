package com.chengzi.app.ui.discover.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.model.DiscoverService;


public class ForumDetailViewModel extends BaseForumViewModel {

    public final MutableLiveData<Boolean> delForumLive = new MutableLiveData<>();

    private String forumId;
    private ForumBean forumBean;

    //  帖子详情
//    public void getForumDetail() {
//        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "" : AccountHelper.getUserId();
//        Api.getApiService(DiscoverService.class)
//                .forumDetail(forumId, userId)
//                .subscribe(new SimpleObserver<ResponseBean<ForumBean>>() {
//                    @Override
//                    public void onSuccess(ResponseBean<ForumBean> forumBeanResponseBean) {
//                        forum.postValue(forumBeanResponseBean.getData());
//                    }
//                });
//    }

    //  删除帖子
    public void deleteForum() {
        Api.getApiService(DiscoverService.class)
                .delForum(AccountHelper.getToken(),
                        AccountHelper.getUserId(), "1", forumId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        delForumLive.postValue(true);
                    }
                });
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public ForumBean getForumBean() {
        return forumBean;
    }

    public void setForumBean(ForumBean forumBean) {
        this.forumBean = forumBean;
    }
}
