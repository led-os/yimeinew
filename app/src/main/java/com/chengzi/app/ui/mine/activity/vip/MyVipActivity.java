package com.chengzi.app.ui.mine.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.lzy.imagepicker.util.StatusBarUtil;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMyVipBindingImpl;
import com.chengzi.app.ui.mine.bean.UserVipInfoBean;
import com.chengzi.app.ui.mine.viewmodel.MyVipViewModel;

/**
 * 我的vip
 *
 * @ClassName:MyVipActivity
 * @PackageName:com.yimei.app.ui.mine.activity.vip
 * @Create On 2019/4/1 0001   17:48
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class MyVipActivity extends BaseActivity<ActivityMyVipBindingImpl, MyVipViewModel> implements View.OnClickListener {


    private boolean is_vip;
    private UserVipInfoBean data;
    private String is_sign;
    private boolean can_buy_vip;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyVipActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_vip;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.statusBarLightMode(this);
        //沉浸式状态栏
        mBinding.setListener(this);
        //普通用户显示 转增 优惠券 邀请好友
        mBinding.cdView.setVisibility(AccountHelper.getIdentity() == Sys.TYPE_USER ? View.VISIBLE : View.GONE);

        //用户信息
        showLoading(Sys.LOADING);
        mViewModel.hosipationalVipInfoLiveData.observe(this, hosipationalVipInfoBean -> {
            dismissLoading();
            if (hosipationalVipInfoBean != null && hosipationalVipInfoBean.getData() != null) {
                data = hosipationalVipInfoBean.getData();
                //用户信息
                mBinding.setUrl(data.getImage());
                mBinding.setBean(data);

                //是否显示VIP图标 是否显示VIP到期时间 (普通用户)未开通和签到/(其他用户)是否显示开通
                is_vip = data.isIs_vip();
                can_buy_vip = data.getCan_buy_vip();

                //是VIP显示VIP有效时间
                mBinding.tvVipTime.setVisibility(is_vip ? View.VISIBLE : View.GONE);

                if (is_vip) {
                    if (AccountHelper.getIdentity() == Sys.TYPE_USER) {  ///普通用户查看签到是否签到状态
                        mViewModel.isSign();
                    } else {            //其他用户都隐藏开通按钮
                        mBinding.tvKtVip.setVisibility(View.GONE);
                    }
                    //VIP有限时间tv_vip_time
                    String vip_endtime = data.getVip_endtime();
                    mBinding.tvVipTime.setText("VIP有效期至" + vip_endtime);
                } else {
                    mBinding.tvKtVip.setText("开通VIP");
                }
                mBinding.ivIsVip.setVisibility(is_vip ? View.VISIBLE : View.GONE);
                //是否可购买 VIP
                mBinding.tvKtVip.setBackgroundResource(!can_buy_vip ? R.drawable.rect_btn_gray_background : R.drawable.rect_btn_red_background);
            }
        });

        mViewModel.isSignLiveData.observe(this, isSignBean -> {
            dismissLoading();
            if (isSignBean != null && isSignBean.getData() != null) {
                //是否签到 getIs_sign  是否已签到，0-未签到 1-已签到
                is_sign = isSignBean.getData().getIs_sign();
                mBinding.tvKtVip.setBackgroundResource(is_sign.equals("1") ? R.drawable.rect_btn_gray_background : R.drawable.rect_btn_red_background);
                mBinding.tvKtVip.setText(is_sign.equals("1") ? "已签到" : "签到");
            }
        });
        mViewModel.responseBeanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mBinding.tvKtVip.setText("已签到");
                mBinding.tvKtVip.setBackgroundResource(R.drawable.rect_btn_gray_background);
                is_sign = "1";
                //刷新数据
                mViewModel.hosipationalVipInfo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.hosipationalVipInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_kt_vip:   //开通vip->（普通用户开通后显示签到）
                if (!is_vip && can_buy_vip) {      //false时 可开通
                    goActivity(OpenVipActivity.class);
                }
                if (is_vip && is_sign.equals("0") && AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    showLoading(Sys.LOADING);
                    mViewModel.userSign();
                }
                break;
            case R.id.tv_see_vip:   //查看VIP特权介绍
//                goActivity(LookVipIntroduceActivity.class);
                //vip规则介绍：8-用户 9-咨询师 10-医生 11-医院
                String t_id = "";
                if (AccountHelper.getIdentity() == Sys.TYPE_USER) {
                    t_id = "8";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
                    t_id = "9";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                    t_id = "10";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                    t_id = "11";
                }
                LookVipIntroduceActivity.start(this, t_id);
                break;
            case R.id.tv_vip_zhuanzeng:   //转增VIP       --->只有普通用户可以
                if (!is_vip)//未开通
                    toast("需要开通VIP才可以转赠");
                else
                    goActivity(SendVipActivity.class);
                break;
            case R.id.tv_vip_youhuiquan:   //我的优惠券   --->只有普通用户可以
                goActivity(MyCouponActivity.class);
                break;
            case R.id.tv_vip_yaoqinghaoyou:   //邀请好友  --->只有普通用户可以
                goActivity(InviteFriendsActivity.class);
                break;
        }
    }
}