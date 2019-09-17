package com.chengzi.app.ui.homepage.user.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.homepage.bean.UserHeaderInfoBean;
import com.chengzi.app.ui.homepage.model.UserHomeService;

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
public class UserHomePageViewModel extends BaseViewModel {


    //用户主页头部用户信息
    public final MutableLiveData<UserHeaderInfoBean> userInfoLiveData = new MutableLiveData<>();
    //  用户主页头部--更换自己的封面
    public final MutableLiveData<ResponseBean> changeCoverLiveData = new MutableLiveData<>();
    // Consultant - 关注/取消关注（冀）
    public final MutableLiveData<Integer> findFollowLiveData = new MutableLiveData<>();

    //  刷新
    public final MutableLiveData<Boolean> refreshLive = new MutableLiveData<>();

    //封面图
    public ObservableField<String> cover_image = new ObservableField<>();


    private String targetUserId;    //  用户id

    private final UserHomeService userHomeService;

    public UserHomePageViewModel() {
        userHomeService = Api.getApiService(UserHomeService.class);
    }

    //用户主页头部用户信息
    public void userInfo() {
        String token = AccountHelper.getToken();
        String userId = AccountHelper.getUserId();
        Params params = Params.newParams()
                .put("token", token)
                .put("user_id", userId)
                .put("click_id", targetUserId);
        userHomeService.userInfo(params.params())
                .subscribe(new SimpleObserver<ResponseBean<UserHeaderInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserHeaderInfoBean> responseBean) {
                        userInfoLiveData.postValue(responseBean.getData());
                    }
                });
    }

    //用户主页头部--更换自己的封面
    public void changeCover() {
        userHomeService.changeCover(AccountHelper.getToken(), AccountHelper.getUserId(), cover_image.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        changeCoverLiveData.postValue(responseBean);
                    }
                });
    }

    //Consultant - 关注/取消关注（冀）
    public void findFollow(boolean attention) {
        userHomeService.findFollow(AccountHelper.getToken(), AccountHelper.getUserId(), targetUserId)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        findFollowLiveData.postValue(attention?0:1);
                    }
                });
    }


    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }
}