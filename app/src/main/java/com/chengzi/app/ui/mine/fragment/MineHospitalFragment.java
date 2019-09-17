package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentMineHospitalBinding;
import com.chengzi.app.event.SwitchMainTabEvent;
import com.chengzi.app.ui.account.activity.HospitalCertifiedActivity;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.mine.activity.CompetitiveAnalysisActivity;
import com.chengzi.app.ui.mine.activity.DataNalysisActivity;
import com.chengzi.app.ui.mine.activity.DoctorOrCounselorActivity;
import com.chengzi.app.ui.mine.activity.FeedBackActivity;
import com.chengzi.app.ui.mine.activity.GoodsManageActivity;
import com.chengzi.app.ui.mine.activity.HelpActivity;
import com.chengzi.app.ui.mine.activity.OrangeCreditActivity;
import com.chengzi.app.ui.mine.activity.ReservationManageActivity;
import com.chengzi.app.ui.mine.activity.SettingActivity;
import com.chengzi.app.ui.mine.activity.SettingPhoneActivity;
import com.chengzi.app.ui.mine.activity.checklook.HospitalCheckLookActivity;
import com.chengzi.app.ui.mine.activity.electronicinvoice.ElectronicInvoiceActivity;
import com.chengzi.app.ui.mine.activity.myorder.DoctorMyOrderActivity;
import com.chengzi.app.ui.mine.activity.mywallet.MyWalletActivity;
import com.chengzi.app.ui.mine.activity.order_beauty_raise.HospitalBeautyRaiseActivity;
import com.chengzi.app.ui.mine.activity.popularize.CaseSearchPromotionActivity;
import com.chengzi.app.ui.mine.activity.popularize.IWantPopularizeActivity;
import com.chengzi.app.ui.mine.activity.takepartseckill.TakePartSeckillActivity;
import com.chengzi.app.ui.mine.activity.verifyorder.VerifyOrderActivity;
import com.chengzi.app.ui.mine.activity.vip.MyVipActivity;
import com.chengzi.app.ui.mine.bean.AuthenticationBean;
import com.chengzi.app.ui.mine.viewmodel.MineViewModel;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.uits.ImageLoader;

import org.greenrobot.eventbus.EventBus;

/**
 * 医院 个人中心
 *
 * @ClassName:MineHospitalFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/1 0001   13:23
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class MineHospitalFragment extends BaseFragment<FragmentMineHospitalBinding, MineViewModel> implements View.OnClickListener {

    private String orange_create;
    //认证状态  0待审核 1审核通过 2已拒绝
    private String auth_status = "0";

    public static MineHospitalFragment newInstance() {
        Bundle args = new Bundle();
        MineHospitalFragment fragment = new MineHospitalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_mine_hospital;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this);
        binding.tvSettingPhone1.setText(Sys.SETTING_KEFU_PHONE);
        resultData();

        //  客户说app 端不要电子发票功能
        binding.tvElectronicInvoice.setVisibility(View.GONE);
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
                binding.tvContent2.setText((TextUtils.isEmpty(userInfoBean.getGrade()) ? "0.0" : userInfoBean.getGrade()) + "分");
                //头像
                ImageLoader.loadImage(binding.ivHead, userInfoBean.getImage(), R.drawable.morentouxiang);
                AccountHelper.setTelephone(userInfoBean.getTelephone());
                //设置的电话
                binding.tvSettingPhone1.setText(AccountHelper.getTelephone());

                auth_status = userInfoBean.getAuth_status();
                AccountHelper.setAuthStatus(userInfo.getAuth_status());
            }
        });
        viewModel.authenticationLiveData.observe(this, authenticationBean -> {
            dismissLoading();
            if (authenticationBean != null && authenticationBean.getData() != null) {
                AuthenticationBean data = authenticationBean.getData();
                HospitalCertifiedActivity.start(getContext(), data, data.getInfo_id());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountHelper.isLogin()) {
            //设置的电话
            binding.tvSettingPhone1.setText(AccountHelper.getTelephone());
        }

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

    @Override
    public void onClick(View v) {
        //未审核通过的医生点击个人中心的按钮会提示：您暂未通过认证审核（设置、规则和帮助、意见和反馈不需要通过审核即可点击）
        if (v.getId() != R.id.tv_message
                && v.getId() != R.id.tv_setting
                && v.getId() != R.id.tv_help
                && v.getId() != R.id.tv_feedback) {
            //认证状态  0待审核 1审核通过 2已拒绝
            if (auth_status.equals("0")) {
                toast("您暂未通过认证审核");
                return;
            } else if (auth_status.equals("3")) {//未认证 TO DO：去认证
                toast("您未认证身份，请认证");
                goActivity(HospitalCertifiedActivity.class);
                return;
            } else if (auth_status.equals("2")) {//已拒绝 TODO：重新认证
                toast("您的审核已拒绝，请重新认证");
//                goActivity(HospitalCertifiedActivity2.class);
//                HospitalCertifiedActivity.start(this, data,data.getInfo_id());
                viewModel.authentication();
//                HospitalCertifiedActivity2.start(getContext(),true);
                return;
            }
        }

        switch (v.getId()) {
            case R.id.cl_hospital:        //医院主页 -->HospitalHomePageFragment
            case R.id.tv_homepage:
                HospitalHomePageActivity.startSelf(getActivity());
                break;
            case R.id.tv_orange_credit:        //橙子信用
                startActivity(new Intent(getContext(), OrangeCreditActivity.class)
                        .putExtra("orange_create", orange_create)
                );
                break;
            case R.id.tv_verify_order:        //验证订单
                goActivity(VerifyOrderActivity.class);
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
            case R.id.tv_reservation_manage://预约管理
                goActivity(ReservationManageActivity.class);
                break;
            case R.id.tv_select://查看
                goActivity(HospitalCheckLookActivity.class);
                break;
            case R.id.tv_doctor_manage:       //医生管理
                DoctorOrCounselorActivity.start(getContext(), 2);
                break;
            case R.id.tv_counselor_manage:       //咨询师管理
                DoctorOrCounselorActivity.start(getContext(), 3);
                break;
            case R.id.tv_case_manage:       //案例管理
                startActivity(new Intent(getContext(), CaseSearchPromotionActivity.class)
                        .putExtra("form", 1)
                );
                break;
            case R.id.tv_goods_manage:       //商品管理
                goActivity(GoodsManageActivity.class);
                break;
            case R.id.tv_order_manage:       //订单管理
                goActivity(DoctorMyOrderActivity.class);
                break;
            case R.id.tv_beauty_raise:   //美人筹
                goActivity(HospitalBeautyRaiseActivity.class);
                break;
            case R.id.tv_data_nalysis: //数据分析
                goActivity(DataNalysisActivity.class);
                break;
            case R.id.tv_competitive_analysis:   //竞对分析
                goActivity(CompetitiveAnalysisActivity.class);
                break;
            case R.id.tv_seckill: //参与秒杀
                goActivity(TakePartSeckillActivity.class);
                break;
            case R.id.tv_want_promote:   //我要推广
                goActivity(IWantPopularizeActivity.class);
                break;
            case R.id.tv_electronic_invoice:         //电子发票
                goActivity(ElectronicInvoiceActivity.class);
                break;
            case R.id.tv_setting:       //设置
                goActivity(SettingActivity.class);
                break;
            case R.id.tv_setting_phone:       //设置电话
            case R.id.tv_setting_phone1:
                goActivity(SettingPhoneActivity.class);
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