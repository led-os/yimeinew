package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentMineDoctorBinding;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.ui.account.activity.DoctorCertifiedActivity;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.activity.FeedBackActivity;
import com.chengzi.app.ui.mine.activity.HelpActivity;
import com.chengzi.app.ui.mine.activity.OrangeCreditActivity;
import com.chengzi.app.ui.mine.activity.SettingActivity;
import com.chengzi.app.ui.mine.activity.VisitTimeManagerActivity;
import com.chengzi.app.ui.mine.activity.bindingmanage.BindingManageActivity;
import com.chengzi.app.ui.mine.activity.checklook.CheckLookActivity;
import com.chengzi.app.ui.mine.activity.docotorroborder.RobOrderActivity;
import com.chengzi.app.ui.mine.activity.mywallet.MyWalletActivity;
import com.chengzi.app.ui.mine.activity.vip.MyVipActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.viewmodel.MineViewModel;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.ImageLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * 医生 -->个人中心
 *
 * @ClassName:MineDoctorFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/1 0001   11:41
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class MineDoctorFragment extends BaseFragment<FragmentMineDoctorBinding, MineViewModel> {

    private String orange_create;
    //认证状态  0待审核 1审核通过 2已拒绝
    private String auth_status = "3";

    public static MineDoctorFragment newInstance() {
        Bundle args = new Bundle();
        MineDoctorFragment fragment = new MineDoctorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_mine_doctor;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(clickEventHandler);
        resultData();
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
                binding.tvContent2.setText(TextUtils.isEmpty(userInfoBean.getGrade()) ? "0.0分" : userInfoBean.getGrade() + "分");//专业审美平分
                binding.tvContent4.setText(TextUtils.isEmpty(userInfoBean.getSkill_grade()) ? "0.0分" : userInfoBean.getSkill_grade() + "分");//技术平分
                binding.tvLevelType.setText(TextUtils.isEmpty(userInfoBean.getOccupation_class()) ? "" : userInfoBean.getOccupation_class());
                //头像
                ImageLoader.loadImage(binding.ivHead, userInfoBean.getImage(), 0, R.drawable.morentouxiang);
                auth_status = userInfoBean.getAuth_status();
                AccountHelper.setAuthStatus(userInfo.getAuth_status());
            }
        });

        viewModel.authenticationLiveData.observe(this, authenticationBean -> {
            dismissLoading();
            if (authenticationBean != null && authenticationBean.getData() != null) {
                AuthenticationBean data = authenticationBean.getData();
                DoctorCertifiedActivity.start(getContext(), data, data.getInfo_id());
            }
        });
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

    private ClickEventHandler<Object> clickEventHandler = (view, bean) -> {
        //未审核通过的医生点击个人中心的按钮会提示：您暂未通过认证审核（设置、规则和帮助、意见和反馈不需要通过审核即可点击）
        if (view.getId() != R.id.tv_message
                && view.getId() != R.id.tv_setting
                && view.getId() != R.id.tv_help
                && view.getId() != R.id.tv_feedback) {
            //认证状态  0待审核 1审核通过 2已拒绝
            if (auth_status.equals("0")) {
                toast("您暂未通过认证审核");
                return;
            } else if (auth_status.equals("3")) {//未认证 TO DO：去认证
                toast("您未认证身份，请认证");
                goActivity(DoctorCertifiedActivity.class);
                return;
            } else if (auth_status.equals("2")) {//已拒绝 TODO：重新认证
                toast("您的审核已拒绝，请重新认证");
//                goActivity(DoctorCertifiedActivity.class);
//                DoctorCertifiedActivity.start(this, data,data.getInfo_id());
                viewModel.authentication();
                return;
            }
        }
        switch (view.getId()) {
            case R.id.tv_orange_credit:        //橙子信用
                startActivity(new Intent(getContext(), OrangeCreditActivity.class)
                        .putExtra("orange_create", orange_create)
                );
                break;
            case R.id.cl_doctor:        //个人主页
            case R.id.tv_homepage:        //个人主页
                int identity = AccountHelper.getIdentity();
                if (identity == 2) {
                    DoctorHomePageActivity.startDoctorSelt(getActivity());
                } else if (identity == 3) {
                    DoctorHomePageActivity.startCounselorSelt(getActivity());
                }
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
                            .setSureListener(v -> MyVipActivity.start(getActivity()))
                            .show();
                }
                break;
            case R.id.tv_online_consultation:       //在线面诊抢单
                infoBean = viewModel.userInfoLiveData.getValue();
                if (infoBean == null) {
                    return;
                }
                if (infoBean.getIs_VIP() == 1) {

                    startActivity(new Intent(getContext(), RobOrderActivity.class).putExtra("type", 2));

                } else {
                    new MessageDialogBuilder(getActivity())
                            .setMessage("你还不是VIP，是否去购买？")
                            .setSureListener(v -> MyVipActivity.start(getActivity()))
                            .show();
                }
                break;
            case R.id.tv_sit_time_manager:   //坐诊时间管理
                VisitTimeManagerActivity.start(getContext(), AccountHelper.getUserId());
                break;
            case R.id.tv_select: //查看
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
    };
}