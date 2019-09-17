package com.chengzi.app.ui.discover.viewmodel;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableInt;
import android.text.TextUtils;
import android.util.Pair;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.event.AttentionEvent;
import com.chengzi.app.event.ForumCollectEvent;
import com.chengzi.app.event.ForumThumbUpEvent;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.discover.bean.ForumBean;
import com.chengzi.app.ui.discover.model.DiscoverService;
import com.chengzi.app.ui.mine.model.MineService;
import com.chengzi.app.utils.AlwaysAliveObservable;

import org.greenrobot.eventbus.EventBus;

public class BaseForumViewModel extends BaseViewModel {

    public final MutableLiveData<Pair<ForumBean, Integer>> attentionLive = new MutableLiveData<>();
    public final MutableLiveData<Pair<ForumBean, Integer>> forumCollectLive = new MutableLiveData<>();   //  帖子收藏
    public final MutableLiveData<Pair<ForumBean, Integer>> forumThumbUpLive = new MutableLiveData<>();   //  帖子点赞

    public final MutableLiveData<ForumBean> forumDetailLive = new MutableLiveData<>();


    public final AlwaysAliveObservable attentionObservable = new AlwaysAliveObservable();   //  关注
    public final AlwaysAliveObservable thumbUpObservable = new AlwaysAliveObservable();     //  点赞
    public final AlwaysAliveObservable collectObservable = new AlwaysAliveObservable();     //  收藏
    public final AlwaysAliveObservable commentObservable = new AlwaysAliveObservable();     //  收藏

    // 当前登录的医生/咨询师/医院 的认证状态  0待审核 1审核通过 2已拒绝
    public final ObservableInt curUserAuthStatus = new ObservableInt(-1);

    protected LifecycleOwner lifecycleOwner;

    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;

        attentionLive.observe(lifecycleOwner, pair -> {

            ForumBean forumBean = pair.first;
            int integer = pair.second;

            ToastUtils.showShort(integer == 0 ? "已取消关注" : "关注成功");
            if (forumBean == null) {
                return;
            }
            forumBean.setIs_follow(integer);
            attentionObservable.update();
            EventBus.getDefault().post(new AttentionEvent(forumBean.getUser_id(), integer == 1));
        });

        forumCollectLive.observe(lifecycleOwner, pair -> {

            ForumBean forumBean = pair.first;
            int integer = pair.second;

            ToastUtils.showShort(integer == 0 ? "已取消收藏" : "收藏成功");

            if (forumBean == null) {
                return;
            }
            forumBean.setIs_collect(integer);
            String collection_num = forumBean.getCollection_num();
            forumBean.setCollection_num(String.valueOf(Integer.valueOf(collection_num) + (integer == 0 ? -1 : 1)));
            collectObservable.update();
            EventBus.getDefault().post(new ForumCollectEvent(this, forumBean.getId(), forumBean.getUser_type(), integer == 1));
        });

        forumThumbUpLive.observe(lifecycleOwner, pair -> {

            ForumBean forumBean = pair.first;
            int integer = pair.second;

            if (forumBean == null) {
                return;
            }

            ToastUtils.showShort(integer == 0 ? "已取消点赞" : "点赞成功");
            forumBean.setIs_like(integer);
            String likes_num = forumBean.getLikes_num();
            forumBean.setLikes_num(String.valueOf(Integer.valueOf(likes_num) + (integer == 0 ? -1 : 1)));
            thumbUpObservable.update();
            EventBus.getDefault().post(new ForumThumbUpEvent(this, forumBean.getId(), forumBean.getUser_type(), integer == 1));
        });

        userInfoLiveData.observe(lifecycleOwner,userInfoBean -> {
            if (userInfoBean == null) {
                return;
            }
            String auth_status = userInfoBean.getAuth_status();
            curUserAuthStatus.set(Integer.valueOf(auth_status));
        });

    }

    //  帖子关注/取消关注
    public void attention(ForumBean forumBean, boolean attention) {
        Api.getApiService(DiscoverService.class)
                .forumAttention(AccountHelper.getToken(), AccountHelper.getUserId(),
                        forumBean.getUser_id())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        attentionLive.postValue(new Pair<>(forumBean, attention ? 0 : 1));
                    }
                });
    }

    //  帖子收藏/取消收藏
    public void forumCollect(ForumBean forumBean, boolean collected) {
        Api.getApiService(DiscoverService.class)
                .forumCollect(AccountHelper.getToken(), AccountHelper.getUserId(), forumBean.getId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        forumCollectLive.postValue(new Pair<>(forumBean, collected ? 0 : 1));
                    }
                });
    }

    //  帖子点赞/取消点赞
    public void forumThumbUp(ForumBean forumBean, boolean thumbUp) {
        Api.getApiService(DiscoverService.class)
                .forumThumbup(AccountHelper.getToken(), AccountHelper.getUserId(), forumBean.getId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        forumThumbUpLive.postValue(new Pair<>(forumBean, thumbUp ? 0 : 1));
                    }
                });
    }

    //  帖子详情
    public void getForumDetail(String forumId) {
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "" : AccountHelper.getUserId();
        Api.getApiService(DiscoverService.class)
                .forumDetail(forumId, userId)
                .subscribe(new SimpleObserver<ResponseBean<ForumBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<ForumBean> forumBeanResponseBean) {
                        forumDetailLive.postValue(forumBeanResponseBean.getData());
                    }
                });
    }

    //用户信息
    public final MutableLiveData<UserInfoBean> userInfoLiveData = new MutableLiveData<>();

    //用户信息
    public void userInfo() {
        if (!AccountHelper.isLogin()) {
            return;
        }
        Api.getApiService(MineService.class).userInfoShow(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        userInfoLiveData.postValue(userInfoBeanResponseBean.getData());
                    }
                });
    }

}
