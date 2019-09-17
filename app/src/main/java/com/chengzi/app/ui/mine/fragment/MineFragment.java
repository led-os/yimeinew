package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentMineBindingImpl;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.activity.FeedBackActivity;
import com.chengzi.app.ui.mine.activity.HelpActivity;
import com.chengzi.app.ui.mine.activity.MyBookingActivity;
import com.chengzi.app.ui.mine.activity.MyShopCarActivity;
import com.chengzi.app.ui.mine.activity.OrangeCreditActivity;
import com.chengzi.app.ui.mine.activity.SettingActivity;
import com.chengzi.app.ui.mine.activity.beautifulprofile.BeautifulProfileActivity;
import com.chengzi.app.ui.mine.activity.electronicinvoice.ElectronicInvoiceActivity;
import com.chengzi.app.ui.mine.activity.myorder.MyOrderActivity;
import com.chengzi.app.ui.mine.activity.mywallet.MyWalletActivity;
import com.chengzi.app.ui.mine.activity.onlinecases.OnlineCasesActivity;
import com.chengzi.app.ui.mine.activity.order_beauty_raise.UserBeautyRaiseActivity;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.ui.mine.activity.vip.MyVipActivity;
import com.chengzi.app.ui.mine.viewmodel.MineViewModel;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferTypeActivity;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * 我的  普通用户 个人中心
 *
 * @ClassName:MineFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/3/25 0025   16:11
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/25 0025 handongkeji All rights reserved.
 */

public class MineFragment extends BaseFragment<FragmentMineBindingImpl, MineViewModel> implements View.OnClickListener {

    public MineFragment() {
        // Required empty public constructor
    }

    //橙子信用分
    private String orange_create = "0";
    private String is_sign;

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this);

        resultData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountHelper.isLogin()) {
            viewModel.isSign();
            viewModel.userInfo();

        } else { //未登录状态
//            ImageLoader.loadImage(binding.ivHead, R.drawable.morentouxiang);
            binding.setUrl("");
            binding.setBean(new UserInfoBean());
        }
        //TODO:2019-6-24 隐藏普通用户->我的->签到
//        binding.tvSignIn.setVisibility(AccountHelper.isLogin() ? View.VISIBLE : View.GONE);
        binding.tvOrangeCredit.setVisibility(AccountHelper.isLogin() ? View.VISIBLE : View.GONE);
    }

    private void resultData() {
        viewModel.userInfoLiveData.observe(this, userInfo -> {
            dismissLoading();
            if (userInfo != null ) {
                UserInfoBean userInfoBean = userInfo;
                orange_create = TextUtils.isEmpty(userInfoBean.getOrange_create()) ? "0" : userInfoBean.getOrange_create();
                binding.setBean(userInfoBean);
                binding.setUrl(userInfoBean.getImage());
            }
        });
        viewModel.isSignLiveData.observe(this, isSignBean -> {
            dismissLoading();
            if (isSignBean != null && isSignBean.getData() != null) {
                //是否签到 getIs_sign  是否已签到，0-未签到 1-已签到
                is_sign = isSignBean.getData().getIs_sign();
//                binding.tvSignIn.setBackgroundResource(is_sign.equals("1") ? R.drawable.rect_btn_gray_background : R.drawable.rect_btn_red_background);
                binding.tvSignIn.setText(is_sign.equals("1") ? "已签到" : "签到");
            }
        });
        viewModel.responseBeanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
//                binding.tvSignIn.setBackgroundResource(R.drawable.rect_btn_gray_background);
//                binding.tvSignIn.setText("已签到");
                is_sign = "1";
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (AccountHelper.shouldLogin(getContext())) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_sign_in:        //签到
                //判断用户是否可以签到
                if (is_sign.equals("0")) {
                    showLoading(Sys.LOADING);
                    viewModel.userSign();
                }
                break;
            case R.id.tv_orange_credit:        //橙子信用
                startActivity(new Intent(getContext(), OrangeCreditActivity.class)
                        .putExtra("orange_create", orange_create)
                );
                break;
            case R.id.cl_user:
            case R.id.tv_personal_homepage:        //个人主页
                if (AccountHelper.isLogin()) {
                    UserHomePageActivity.startSelf(getContext());
                }
                break;
            case R.id.tv_wallet:        //钱包
                goActivity(MyWalletActivity.class);
                break;
            case R.id.tv_message:       //消息
                EventBus.getDefault().post(new SwitchMainTabEvent(1));
//                RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 1);
                break;
            case R.id.tv_my_vip:        //我的vip
                goActivity(MyVipActivity.class);
                break;
            case R.id.tv_my_order:      //我的订单
                goActivity(MyOrderActivity.class);
                break;
            case R.id.tv_shopping_cart: //购物车
                goActivity(MyShopCarActivity.class);
                break;
            case R.id.tv_my_reservation://我的预约
                goActivity(MyBookingActivity.class);
                break;
            case R.id.tv_consult:       //私享咨询
                PrivateReferActivity.start(getActivity());
                break;
            case R.id.tv_online_consultation:       //在线面诊
                PrivateReferTypeActivity.start(getActivity(), Sys.TYPE_DIAGNOSE_ONLINE);
                break;
            case R.id.tv_beauty_raise:   //用户美人筹
                goActivity(UserBeautyRaiseActivity.class);
                break;
            case R.id.tv_beauty_raise1: //美人筹规则
                LookVipIntroduceActivity.start(getContext(), "13");
                break;
            case R.id.tv_beauty_profile: //美丽档案
                goActivity(BeautifulProfileActivity.class);
                break;
            case R.id.tv_online_cases:   //在线病例
                OnlineCasesActivity.start(getActivity(), AccountHelper.getUserId());
                break;
            case R.id.tv_electronic_invoice:         //电子发票
                goActivity(ElectronicInvoiceActivity.class);
                break;
            case R.id.tv_setting:       //设置
                goActivity(SettingActivity.class);
                break;
            case R.id.tv_help:          //规则和帮助
                goActivity(HelpActivity.class);
                break;
            case R.id.tv_feedback:      //反馈和意见
                goActivity(FeedBackActivity.class);
                break;
        }
    }
}