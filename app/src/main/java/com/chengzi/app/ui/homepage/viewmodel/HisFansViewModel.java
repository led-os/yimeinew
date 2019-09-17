package com.chengzi.app.ui.homepage.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.util.Pair;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.PageBean;
import com.handong.framework.base.ResponseBean;
import com.nevermore.oceans.pagingload.IRequest;
import com.chengzi.app.ui.homepage.model.UserHomeService;
import com.chengzi.app.ui.mine.bean.FollowBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;


public class HisFansViewModel extends BaseViewModel implements IRequest {

    //普通用户 关注/粉丝列表
    public final MutableLiveData<List<FollowBean>> followListLiveData = new MutableLiveData<>();
    // Consultant - 关注/取消关注（冀）
    public final MutableLiveData<Pair<FollowBean, Integer>> findFollowLive = new MutableLiveData<>();

    private final MineService mineService;

    public HisFansViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    private String targetId;    //被查看的用户id
    private int type;   //  类型1-关注 2-粉丝

    @Override
    public void onRequest(int currentPage, int pageSize) {
        //关注/粉丝列表
        mineService.followToList(AccountHelper.getToken(), AccountHelper.getUserId(),
                targetId, type + "", String.valueOf(currentPage), String.valueOf(pageSize))
                .subscribe(new SimpleObserver<ResponseBean<PageBean<FollowBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<PageBean<FollowBean>> responseBean) {
                        followListLiveData.postValue(responseBean.getData().getData());
                    }

                });
    }

    //Consultant - 关注/取消关注（冀）
    public void findFollow(FollowBean bean, int position) {
        Api.getApiService(UserHomeService.class)
                .findFollow(AccountHelper.getToken(), AccountHelper.getUserId(), bean.getUser_id())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        bean.setIs_followed(bean.getIs_followed() == 1 ? 0 : 1);
                        findFollowLive.postValue(new Pair<>(bean, position));
                    }
                });
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
