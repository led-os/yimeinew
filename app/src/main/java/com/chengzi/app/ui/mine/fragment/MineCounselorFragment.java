package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentMineCounselorBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.ui.account.activity.CounselorCertifiedActivity;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.activity.FeedBackActivity;
import com.chengzi.app.ui.mine.activity.HelpActivity;
import com.chengzi.app.ui.mine.activity.IWantRecommendActivity;
import com.chengzi.app.ui.mine.activity.OrangeCreditActivity;
import com.chengzi.app.ui.mine.activity.SettingActivity;
import com.chengzi.app.ui.mine.activity.bindingmanage.BindingManageActivity;
import com.chengzi.app.ui.mine.activity.checklook.CheckLookActivity;
import com.chengzi.app.ui.mine.activity.docotorroborder.RobOrderActivity;
import com.chengzi.app.ui.mine.activity.mywallet.MyWalletActivity;
import com.chengzi.app.ui.mine.activity.vip.MyVipActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.bean.PromoteRoutingBean;
import com.chengzi.app.ui.mine.viewmodel.MineViewModel;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.uits.ImageLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * 咨询师 个人中心
 *
 * @ClassName:MineCounselorFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/1 0001   11:54
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class MineCounselorFragment extends BaseFragment<FragmentMineCounselorBinding, MineViewModel>
        implements View.OnClickListener {

    private String orange_create;
    //认证状态  0待审核 1审核通过 2已拒绝
    private String auth_status = "0";

    public static MineCounselorFragment newInstance() {
        Bundle args = new Bundle();
        MineCounselorFragment fragment = new MineCounselorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_mine_counselor;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this::onClick);

        resultData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountHelper.isLogin() && !isHidden()) {
            viewModel.userInfo();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && AccountHelper.isLogin()) {
            viewModel.userInfo();
        }
    }

    private void resultData() {
        viewModel.userInfoLiveData.observe(this, userInfo -> {
            dismissLoading();

            if (userInfo != null) {
                UserInfoBean userInfoBean = userInfo;
                binding.tvNickName.setText(TextUtils.isEmpty(userInfoBean.getTrue_name()) ? userInfoBean.getName() : userInfoBean.getTrue_name());
//                binding.tvNickName.setText(userInfoBean.getName());
                orange_create = userInfoBean.getOrange_create();
                binding.tvOrangeCredit.setText("橙子信用：" + (TextUtils.isEmpty(orange_create) ? "0" : userInfoBean.getOrange_create()) + "分");
                binding.tvContent2.setText((TextUtils.isEmpty(userInfoBean.getGrade()) ? "0.0" : userInfoBean.getGrade()) + "分");//专业审美平分
                binding.tvContent4.setText((TextUtils.isEmpty(userInfoBean.getSkill_grade()) ? "0.0" : userInfoBean.getSkill_grade()) + "分");//服务平分
                binding.tvLevelType.setText(TextUtils.isEmpty(userInfoBean.getRank()) ? "" : userInfoBean.getRank());
                //头像
                ImageLoader.loadImage(binding.ivHead, userInfoBean.getImage(), R.drawable.morentouxiang);

                auth_status = userInfoBean.getAuth_status();
                AccountHelper.setAuthStatus(userInfo.getAuth_status());
            }
        });


        //我要推荐-->如果没有绑定医院 不可进
        viewModel.promoteRoutingLiveData.observe(this, promoteRoutingBean -> {
            dismissLoading();
            if (promoteRoutingBean != null && promoteRoutingBean.getStatus() == Sys.SUCCESS_STATUS && promoteRoutingBean.getData() != null) {
                PromoteRoutingBean data = promoteRoutingBean.getData();
                startActivity(new Intent(getContext(), IWantRecommendActivity.class)
                        .putExtra("data", data)
                );
            }
        });

        viewModel.authenticationLiveData.observe(this, authenticationBean -> {
            dismissLoading();
            if (authenticationBean != null && authenticationBean.getData() != null) {
                AuthenticationBean data = authenticationBean.getData();
                CounselorCertifiedActivity.start(getContext(), data, data.getInfo_id());
            }
        });
    }


    @Override
    public void onClick(View v) {
        //未审核通过的医生点击个人中心的按钮会提示：您暂未通过认证审核（设置、规则和帮助、意见和反馈不需要通过审核即可点击）
        if (v.getId() != R.id.tv_message
                && v.getId() != R.id.tv_setting
                && v.getId() != R.id.tv_help
                && v.getId() != R.id.tv_feedback) {
            //认证状态  0待审核 1审核通过 2已拒绝
            if (auth_status.equals("0")) {
                toast("您暂未通过审核");
                return;
            } else if (auth_status.equals("3")) {//未认证 TO DO：去认证
                toast("您未认证身份，请认证");
                goActivity(CounselorCertifiedActivity.class);
                return;
            } else if (auth_status.equals("2")) {//已拒绝 TODO：重新认证
                toast("您的审核已拒绝，请重新认证");
//                goActivity(CounselorCertifiedActivity.class);
//                CounselorCertifiedActivity.start(this, data, data.getInfo_id());
                viewModel.authentication();
                return;
            }
        }
        switch (v.getId()) {
            case R.id.cl_counselor:        //咨询师主页
            case R.id.tv_homepage:
                DoctorHomePageActivity.startCounselorSelt(getContext());
                break;
            case R.id.tv_orange_credit:        //橙子信用
                startActivity(new Intent(getContext(), OrangeCreditActivity.class)
                        .putExtra("orange_create", orange_create)
                );
                break;
            case R.id.tv_wallet:        //钱包
                goActivity(MyWalletActivity.class);
                break;
            case R.id.tv_message:       //消息
//                GlobalParms.sChangeFragment.changge(1);
                EventBus.getDefault().post(new SwitchMainTabEvent(1));
//                RxBus.get().post(RxBusTag.TAG_SWITCH_MAIN_TAB, 1);
                break;
            case R.id.tv_my_vip:        //我的vip
                goActivity(MyVipActivity.class);
                break;
            case R.id.tv_consult:       //私享咨询抢单
                UserInfoBean infoBean = viewModel.userInfoLiveData.getValue();
                if (infoBean == null) {
                    return;
                }
                if (infoBean.getIs_VIP() == 1) {
                    goActivity(RobOrderActivity.class);
                } else {
                    new MessageDialogBuilder(getActivity())
                            .setMessage("你还不是VIP，是否去购买？")
                            .setSureListener(view -> MyVipActivity.start(getActivity()))
                            .show();
                }
                break;
            case R.id.tv_want_promote:  //我要推荐客户 -->//绑定医院了才可以推荐
                showLoading(Sys.LOADING);
                viewModel.promoteRouting();
                break;
            case R.id.tv_select:       //查看
                goActivity(CheckLookActivity.class);
                break;
            case R.id.tv_binding_manager:         //绑定管理
                goActivity(BindingManageActivity.class);
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