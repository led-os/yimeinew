package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.bean.GoodAtBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.HashMap;
import java.util.List;

/**
 * 编辑信息
 *
 * @ClassName:EditInfoUserViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/5/21 0021   16:53
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/21 0021 handongkeji All rights reserved.
 */
public class EditInfoUserViewModel extends BaseViewModel {

    //用户信息
    public final MutableLiveData<ResponseBean<UserInfoBean>> userInfoLiveData = new MutableLiveData<>();
    //更新用户头像
    public final MutableLiveData<ResponseBean<UserInfoBean>> updateInfoLiveData = new MutableLiveData<>();
    //获取所有一级分类
    public final MutableLiveData<List<GoodAtBean>> parentLevelListLiveData = new MutableLiveData<>();
    //更新擅长
    public final MutableLiveData<ResponseBean> parentLevelUpdateLiveData = new MutableLiveData<>();
    //医院/咨询师/医生认证页面
    public final MutableLiveData<ResponseBean<AuthenticationBean>> authenticationLiveData = new MutableLiveData<>();

    public ObservableField<String> key_param = new ObservableField<>();

    //type=1修改 其他->
    public ObservableField<String> type = new ObservableField<>();

    private final MineService mineService;

    public EditInfoUserViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    //用户信息
    public void userInfo() {
        mineService.userInfoShow(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        userInfoLiveData.postValue(userInfoBeanResponseBean);
                    }
                });
    }

    //更新用户信息
    public void updateInfo(String value) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("token", AccountHelper.getToken());
        maps.put("user_id", AccountHelper.getUserId());
        maps.put(key_param.get(), value);
        mineService.updateInfo(maps)
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        updateInfoLiveData.postValue(userInfoBeanResponseBean);
                    }
                });
    }

    public void updateInfo(HashMap<String, String> map) {
        Params params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId());
        params.putAll(map);
        mineService.updateInfo(params.params())
                .subscribe(new SimpleObserver<ResponseBean<UserInfoBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<UserInfoBean> userInfoBeanResponseBean) {
                        updateInfoLiveData.postValue(userInfoBeanResponseBean);
                    }
                });
    }

    //获取所有一级分类
    public void parentLevelList() {
        mineService.parentLevelList(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<List<GoodAtBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<GoodAtBean>> goodAtBean) {
                        parentLevelListLiveData.postValue(goodAtBean.getData());
                    }
                });
    }

    //更新擅长
    public void parentLevelUpdate(String str) {
        mineService.parentLevelUpdate(AccountHelper.getToken(), AccountHelper.getUserId(), AccountHelper.getUserId(), str)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean responseBean) {
                        parentLevelUpdateLiveData.postValue(responseBean);
                    }
                });
    }

    //医院/咨询师/医生认证页面
    public void authentication() {
        mineService.authentication(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<AuthenticationBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<AuthenticationBean> responseBean) {
                        authenticationLiveData.postValue(responseBean);
                    }
                });
    }
}