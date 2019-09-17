package com.chengzi.app.ui.mine.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.ui.mine.bean.AgressmentBean;
import com.chengzi.app.ui.mine.bean.CouponListBean;
import com.chengzi.app.ui.mine.bean.GetDownloadUrlQrcodeBean;
import com.chengzi.app.ui.mine.bean.IsSignBean;
import com.chengzi.app.ui.mine.bean.OpenVipBean;
import com.chengzi.app.ui.mine.bean.UserVipInfoBean;
import com.chengzi.app.ui.mine.bean.VipGivingListBean;
import com.chengzi.app.ui.mine.model.MineService;

import java.util.List;

/**
 * 我的vip
 *
 * @ClassName:MyVipViewModel
 * @PackageName:com.yimei.app.ui.mine.viewmodel
 * @Create On 2019/4/24 0024   14:04
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/24 0024 handongkeji All rights reserved.
 */
public class MyVipViewModel extends BaseViewModel {

    //VIP转赠列表
    public final MutableLiveData<ResponseBean<List<VipGivingListBean>>> vipGivingListLiveData = new MutableLiveData<>();
    //VIP转赠
    public final MutableLiveData<ResponseBean> vipGivingLiveData = new MutableLiveData<>();
    //VIP信息展示
    public final MutableLiveData<ResponseBean<UserVipInfoBean>> hosipationalVipInfoLiveData = new MutableLiveData<>();
    //VIP开通介绍
    public final MutableLiveData<AgressmentBean> vipAgreeenmentLiveData = new MutableLiveData<>();
    //我的优惠券列表
    public final MutableLiveData<ResponseBean<List<CouponListBean>>> couponListLiveData = new MutableLiveData<>();
    //获取VIP价格信息展示
    public final MutableLiveData<ResponseBean<List<OpenVipBean>>> getVipPriceLiveData = new MutableLiveData<>();

    //下载二维码
    public final MutableLiveData<ResponseBean<GetDownloadUrlQrcodeBean>> getDownloadUrlQrcodeLiveData = new MutableLiveData<>();

    public final ObservableField<String> mobile = new ObservableField<>();

    private final MineService mineService;

    public MyVipViewModel() {
        mineService = Api.getApiService(MineService.class);
    }

    //VIP转赠列表
    public void vipGivingList() {
        mineService.vipGivingList(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean<List<VipGivingListBean>>>() {
                    @Override
                    public void onSuccess(ResponseBean<List<VipGivingListBean>> bean) {
                        vipGivingListLiveData.postValue(bean);
                    }
                });
    }

    //VIP转赠
    public void vipGiving() {
        mineService.vipGiving(AccountHelper.getToken(), AccountHelper.getUserId(), mobile.get())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        vipGivingLiveData.postValue(bean);
                    }

                });
    }

    //VIP信息展示
    public void hosipationalVipInfo() {
        mineService.userVipInfo(AccountHelper.getToken(), AccountHelper.getUserId(), String.valueOf(AccountHelper.getIdentity()))
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        hosipationalVipInfoLiveData.postValue(bean);
                    }

                });
    }

    //VIP信息展示
    public void vipAgreeenment() {
        //user_type://用户类型 必传 类型 1-用户 2-医生 3-咨询师 4-机构
        //info_type://消息类型 必传 消息类型(1.协议;2.VIP;3.推广）-->写死
        mineService.vipAgreeenmentList(AccountHelper.getToken(), String.valueOf(AccountHelper.getIdentity()), "2")
                .subscribe(new SimpleObserver<ResponseBean<AgressmentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<AgressmentBean> bean) {
                        vipAgreeenmentLiveData.postValue(bean.getData());
                    }

                });
    }

    //Texts - 文本中心（阿布）
    //t_id	  1 关于我们 2 隐私政策 3 用户协议 4 联系我们 5 意见反馈 6 排行榜规则 7 帮助中心 8 用户vip规则介绍 9 咨询师vip规则介绍 10医生vip规则介绍 11医院vip规则介绍 12推广规则
    public void getTexts(String t_id) {
        //user_type://用户类型 必传 类型 1-用户 2-医生 3-咨询师 4-机构
        //info_type://消息类型 必传 消息类型(1.协议;2.VIP;3.推广）-->写死
        mineService.getTexts(t_id)
                .subscribe(new SimpleObserver<ResponseBean<AgressmentBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<AgressmentBean> bean) {
                        vipAgreeenmentLiveData.postValue(bean.getData());
                    }

                });
    }

    //我的优惠券列表
    public void couponList() {
        mineService.couponList(AccountHelper.getToken(), AccountHelper.getUserId())
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        couponListLiveData.postValue(bean);
                    }

                });
    }

    //获取VIP价格信息展示
    public void getVipPrice() {
        //用户类型 必传 1.普通用户 2.其他
        String type = AccountHelper.getIdentity() == 1 ? "1" : "2";
        mineService.getVipPrice(AccountHelper.getToken(), AccountHelper.getUserId(), type)
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        getVipPriceLiveData.postValue(bean);
                    }

                });
    }
   /* //生成订单
    public final MutableLiveData<ResponseBean<GetVipOrderBean>> getOrderLiveData = new MutableLiveData<>();
    //生成订单
    public void getOrder(String vip_id) {
        mineService.getVipPreOrder(AccountHelper.getToken(), AccountHelper.getUserId(), vip_id)
                .subscribe(new SimpleObserver<ResponseBean<GetVipOrderBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<GetVipOrderBean> bean) {
                        getOrderLiveData.postValue(bean);
                    }
                });
    }*/

    //下载二维码
    public void getDownloadUrlQrcode() {
        mineService.getDownloadUrlQrcode()
                .subscribe(new SimpleObserver<ResponseBean<GetDownloadUrlQrcodeBean>>() {
                    @Override
                    public void onSuccess(ResponseBean<GetDownloadUrlQrcodeBean> bean) {
                        getDownloadUrlQrcodeLiveData.postValue(bean);
                    }

                    @Override
                    public void onError(int code, String errorMsg) {
//                        super.onError(code, errorMsg);
                        getDownloadUrlQrcodeLiveData.postValue(null);
                    }
                });
    }

    //User - 获取用户签到状态（冀）
    public final MutableLiveData<ResponseBean<IsSignBean>> isSignLiveData = new MutableLiveData<>();
    //用户签到(普通用户)
    public final MutableLiveData<ResponseBean> responseBeanLiveData = new MutableLiveData<>();

    //用户签到
    public void isSign() {
        Api.getApiService(MineService.class).isSign(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean<IsSignBean>>() {
            @Override
            public void onSuccess(ResponseBean<IsSignBean> responseBean) {
                isSignLiveData.postValue(responseBean);
            }
        });
    }

    //用户签到
    public void userSign() {
        Api.getApiService(MineService.class).userSign(AccountHelper.getToken(), AccountHelper.getUserId()).subscribe(new SimpleObserver<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean responseBean) {
                responseBeanLiveData.postValue(responseBean);
            }
        });
    }
}