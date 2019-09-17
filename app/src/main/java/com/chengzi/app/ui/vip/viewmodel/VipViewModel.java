package com.chengzi.app.ui.vip.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.home.bean.CategoryItem;
import com.chengzi.app.ui.home.model.HomeService;
import com.chengzi.app.ui.vip.bean.VipBannerListBean;
import com.chengzi.app.ui.vip.bean.VipChannelHomePageBean;
import com.chengzi.app.ui.vip.bean.VipChannelIndexBean;
import com.chengzi.app.ui.vip.bean.VipNormalChannelBean;
import com.chengzi.app.ui.vip.model.VipService;
import com.chengzi.app.utils.AlwaysAliveObservable;
import com.chengzi.app.utils.PreferenceManager;

import java.util.List;

/**
 * VIP模块
 *
 * @ClassName:VipViewModel
 * @PackageName:com.yimei.app.ui.vip.viewmodel
 * @Create On 2019/5/29 0029   15:05
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/29 0029 handongkeji All rights reserved.
 */

public class VipViewModel extends BaseViewModel {

    public final MutableLiveData<List<CategoryItem>> vipCategoryLive = new MutableLiveData<>();
    public final MutableLiveData<List<VipBannerListBean>> getVipBannerLive = new MutableLiveData<>();

    public final MutableLiveData<VipChannelHomePageBean> vipChannelHomeLive = new MutableLiveData<>();
    public final MutableLiveData<VipChannelIndexBean> vipChannelIndexLive = new MutableLiveData<>();
    public final MutableLiveData<VipNormalChannelBean> vipNormalChannelLive = new MutableLiveData<>();

    public final MutableLiveData<CategoryItem> currentCategory = new MutableLiveData<>();

    public final AlwaysAliveObservable alwaysAliveObservable = new AlwaysAliveObservable();
    public final AlwaysAliveObservable refreshObservable = new AlwaysAliveObservable();

    private VipService vipService;

    public VipViewModel() {
        vipService = Api.getApiService(VipService.class);
    }

    public void getVipCategory() {
        vipService.getVipCategory(AccountHelper.getToken())
                .subscribe(new SimpleObserver<ResponseBean<List<CategoryItem>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<CategoryItem>> bean) {
                        vipCategoryLive.postValue(bean.getData());
                    }
                });
    }

    public void getVipBanner() {
        CategoryItem categoryItem = currentCategory.getValue();
        if (categoryItem == null) {
            return;
        }
        String city_id = PreferenceManager.getPreference(PreferenceManager.CITY_ID);
        vipService.getVipBanner(AccountHelper.getToken(), categoryItem.getId(), city_id)
                .subscribe(new SimpleObserver<ResponseBean<List<VipBannerListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<VipBannerListBean>> bean) {
                        getVipBannerLive.postValue(bean.getData());
                    }
                });
    }

    //VIP精选商品
    public void getVipChannelHomepage() {
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "0" : AccountHelper.getUserId();
        vipService.vipChannelHomepage(userId)
                .subscribe(new SimpleObserver<ResponseBean<VipChannelHomePageBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<VipChannelHomePageBean> vipChannelHomePageBeanResponseBean) {
                        vipChannelHomeLive.postValue(vipChannelHomePageBeanResponseBean.getData());
                    }
                });
    }

    //获取VIP频道首页基本信息
    public void vipChannelIndex() {
        String city_id = PreferenceManager.getPreference(PreferenceManager.CITY_ID);
        city_id = !TextUtils.isEmpty(city_id) ? city_id : "110100";
        vipService.vipChannelIndex(AccountHelper.getUserId(), city_id)
                .subscribe(new SimpleObserver<ResponseBean<VipChannelIndexBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<VipChannelIndexBean> indexBean) {
                        vipChannelIndexLive.postValue(indexBean.getData());
                    }
                });
    }

    //VIP其他分类商品
    public void getVipNormalChannel() {
        CategoryItem categoryItem = currentCategory.getValue();
        if (categoryItem == null) {
            return;
        }
        vipService.getVipNormalChannel(categoryItem.getId(), PreferenceManager.getPreference(PreferenceManager.CITY_ID))
                .subscribe(new SimpleObserver<ResponseBean<VipNormalChannelBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<VipNormalChannelBean> vipNormalChannelBeanResponseBean) {
                        vipNormalChannelLive.postValue(vipNormalChannelBeanResponseBean.getData());
                    }
                });
    }

    //推广广告
    public void promotionCutBanner(String promotionId) {
        if (TextUtils.isEmpty(promotionId) || TextUtils.equals(promotionId, "0") || (AccountHelper.isLogin() && AccountHelper.getIdentity() > 1)) {
            return;
        }
        String userId = TextUtils.isEmpty(AccountHelper.getUserId()) ? "0" : AccountHelper.getUserId();
        Api.getApiService(HomeService.class).promotionCutBanner(promotionId, userId, "1")
                .subscribe();
    }

}
