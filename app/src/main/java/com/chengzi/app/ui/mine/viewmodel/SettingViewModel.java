package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.api.Params;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.HashMap;

/**
 * @Desc:
 * @ClassName:SettingViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/29 0029
 * @Site:http://www.handongkeji.com
 * @author:chenzhiguang
 * @Copyrights 2018/1/31 0031 handongkeji All rights reserved.
 */
public class SettingViewModel extends BaseViewModel {

    public static final int STATE_ONLINE = 1;

    public static final int STATE_OFFLINE = 2;

    public static final int STATE_BUSY = 3;

    public final MutableLiveData<ResponseBean> responseBeanLiveData = new MutableLiveData<>();

    public void changeOnlineState(int state) {
        //咨询师
        HashMap<String, String> params = Params.newParams()
                .put("token", AccountHelper.getToken())
                .put("user_id", AccountHelper.getUserId())
                .put("type", AccountHelper.getIdentity() + "")//用户类型
                .put("state", state + "")//状态( 1-在线 2-离线 3-忙碌)（必传）
                .params();
        Api.getApiService(MineService.class).changeOnlineState(params).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }
}
