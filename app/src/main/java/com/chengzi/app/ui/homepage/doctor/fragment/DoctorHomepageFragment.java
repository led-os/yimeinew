package com.chengzi.app.ui.homepage.doctor.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentDoctorHomepageBinding;
import com.chengzi.app.dialog.DialDialog;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshListenerRegistry;
import com.chengzi.app.ui.homepage.activity.AppointmentActivity;
import com.chengzi.app.ui.homepage.bean.BelongOrgBean;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.doctor.viewmodel.DoctorHomePageViewModel;
import com.chengzi.app.ui.homepage.fragment.BelongOrgFragment;
import com.chengzi.app.ui.homepage.fragment.HisGoodsFragment;
import com.chengzi.app.ui.homepage.fragment.SubEstimateFragment;
import com.chengzi.app.ui.homepage.fragment.SubPeopleSayFragment;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.utils.NimUtils;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 医生/咨询师/医院 主页
 *
 * @ClassName:DoctorHomepageFragment
 * @PackageName:com.yimei.app.ui.doctorhomepage.fragment
 * @Create On 2019/4/18 0018   09:53
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
@RuntimePermissions
public class DoctorHomepageFragment extends BaseFragment<FragmentDoctorHomepageBinding,
        DoctorHomePageViewModel> implements RefreshListenerRegistry, View.OnClickListener {

    private List<OnRefreshListener> listeners = new ArrayList<>();

    public DoctorHomepageFragment() {
    }

    public static DoctorHomepageFragment newInstance() {
        android.os.Bundle args = new Bundle();
        DoctorHomepageFragment fragment = new DoctorHomepageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_doctor_homepage;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setListener(this);

        binding.bottomLayout.setVisibility(viewModel.isSelf() ? View.GONE : View.VISIBLE);

        if (AccountHelper.getIdentity() > 1) {
            binding.btnOnlineServices.setVisibility(View.INVISIBLE);
            binding.btnOnlineServices.setEnabled(false);
            binding.btnAppointmentDoctor.setVisibility(View.INVISIBLE);
            binding.btnAppointmentDoctor.setEnabled(false);
            binding.btnDial.setVisibility(View.INVISIBLE);
            binding.btnDial.setEnabled(false);
        }

        binding.swipeRefreshView.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefreshView.setOnRefreshListener(() -> {
            for (OnRefreshListener listener : listeners) {
                listener.onRefresh();
            }
        });

        if (savedInstanceState == null) {
            addFragments();
        }

        binding.swipeRefreshView.postDelayed(() -> {
            binding.swipeRefreshView.setRefreshing(true);
            for (OnRefreshListener listener : listeners) {
                listener.onRefresh();
            }
        }, 20);


        //  扣除推广费用
        viewModel.promotionCut();

        //id	Number	  【必填】当前访问的对象ID（医生ID/咨询师ID/医院ID/商品ID等）
        //type	Number	  【必填】访问对象类型，1/咨询师；2/医生；3/机构；4/商品。
        //2医生 3咨询师->1
        String type = viewModel.getUserType() == 2 ? "2" : "1";
        viewModel.addVisit(viewModel.getDoctorId(), type);

    }

    private void addFragments() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction
                .add(R.id.container, InfoFragment.newInstance(getChildFragmentManager()),
                        InfoFragment.class.getSimpleName());

        //  医生有预约，咨询师没有预约
        if (viewModel.getUserType() == 2) {
            transaction.add(R.id.container, AppointmentFragment.newInstance(getChildFragmentManager()),
                    AppointmentFragment.class.getSimpleName());
            //预约医生
            binding.btnAppointmentDoctor.setText(R.string.make_an_appointment_with_doctor);
        } else {
            //咨询师预约医院
            binding.btnAppointmentDoctor.setText(R.string.make_an_appointment_with_hospital);
        }

        transaction.add(R.id.container, BelongOrgFragment.newInstance(getChildFragmentManager()),
                BelongOrgFragment.class.getSimpleName())
                .add(R.id.container, SubEstimateFragment.newInstance(getChildFragmentManager(),
                        viewModel.getDoctorId(), viewModel.getUserType()),
                        SubEstimateFragment.class.getSimpleName())
                .add(R.id.container, SubPeopleSayFragment.newInstance(getChildFragmentManager(),
                        viewModel.getDoctorId(), viewModel.getUserType()),
                        SubPeopleSayFragment.class.getSimpleName())
                .add(R.id.container, HisGoodsFragment.newInstance(getChildFragmentManager(),
                        viewModel.getDoctorId(), viewModel.getUserType(), true),
                        HisGoodsFragment.class.getSimpleName())
                .add(R.id.container, RecommendOthersFragment.newInstance(getChildFragmentManager(),
                        viewModel.getUserType()),
                        RecommendOthersFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public void finishRefresh() {
        boolean finished = true;
        for (OnRefreshListener listener : listeners) {
            finished &= listener.isRefreshFinished();
        }
        if (finished) {
            binding.swipeRefreshView.setRefreshing(false);
        }
    }

    @Override
    public void register(OnRefreshListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void unRegister(OnRefreshListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_org_homepage:
                DoctorHomeInfoBean infoBean = viewModel.doctorHomeLive.getValue();
                if (infoBean == null || infoBean.getBelongOrg() == null) {
                    ToastUtils.showShort(String.format("该%1$s未绑定机构",viewModel.getUserType() == 2?"医生":"咨询师"));
                    return;
                }
                BelongOrgBean belongOrg = infoBean.getBelongOrg();
                String belongOrgId = belongOrg.getId();
                HospitalHomePageActivity.start(getContext(), belongOrgId);
                break;
            case R.id.btn_online_services:  //  在线咨询
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (AccountHelper.getIdentity() != 1) {
                    ToastUtils.showShort("只有普通用户可以咨询");
                    return;
                }

                DoctorHomeInfoBean bean = viewModel.doctorHomeLive.getValue();
                if (bean == null) {
                    return;
                }

                if (!TextUtils.equals(bean.getOnline_state(),"1")) {
                    ToastUtils.showShort("对方不在线");
                    return;
                }

                if (!TextUtils.isEmpty(bean.getYunxin_accid())) {
                    String yunxin_accid = bean.getYunxin_accid();
                    NimUtils.startP2PSession(getActivity(), yunxin_accid);
                } else {
                    ToastUtils.showShort("对方的云信id不存在，让他重新登录");
                }

                break;
            case R.id.btn_appointment_doctor:   //  预约医生  /预约机构

                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }

                int identity = AccountHelper.getIdentity();
                if (identity != 1) {
                    ToastUtils.showShort("只有普通用户可以预约");
                    return;
                }

                if (viewModel.getUserType() == 2) {
                    //预约医生
                    DoctorHomeInfoBean homeInfoBean = viewModel.doctorHomeLive.getValue();
//                    EventBus.getDefault().postSticky(homeInfoBean);
//                    startActivity(new Intent(getActivity(), AppointmentActivity.class));
                    AppointmentActivity.start(getContext(), homeInfoBean);
                } else {
                    //咨询师 预约医院 / 机构
                    BelongOrgBean belongOrg1 = viewModel.doctorHomeLive.getValue().getBelongOrg();
//                    startActivity(new Intent(getActivity(), AppointmentActivity.class)
//                            .putExtra("bean", belongOrg1)
//                    );
                    AppointmentActivity.start(getContext(), belongOrg1);
                }
                break;
            case R.id.btn_dial:
                DoctorHomepageFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.requestCallPermissionDoc.get()) {
            viewModel.requestCallPermissionDoc.set(false);
            DoctorHomepageFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        DoctorHomepageFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(value = {Manifest.permission.CALL_PHONE})
    void showDailDialog() {
        DoctorHomeInfoBean doctorHomeInfoBean = viewModel.doctorHomeLive.getValue();
        if (doctorHomeInfoBean == null) {
            return;
        }
        if (doctorHomeInfoBean.getBelongOrg() == null || TextUtils.isEmpty(doctorHomeInfoBean.getBelongOrg().getTelephone())) {
            ToastUtils.showShort(String.format("该%s未绑定医院，或所属医院未设置服务电话", viewModel.getUserType() == 2 ? "医生" : "咨询师"));
            return;
        }
        String mobile = doctorHomeInfoBean.getBelongOrg().getTelephone();
        DialDialog dialDialog = new DialDialog();
        dialDialog.setPhoneNumber(mobile);
        dialDialog.show(getChildFragmentManager(), DialDialog.class.getSimpleName());
    }

    @OnPermissionDenied(value = {Manifest.permission.CALL_PHONE})
    void onDialDenied() {
        showDeniedDialog("您拒绝了拨打电话权限，使该功能无法使用，是否现在去开启？");
    }

    @OnNeverAskAgain(value = {Manifest.permission.CALL_PHONE})
    void onDialNeverAsk() {
        showDeniedDialog("您禁止了拨打电话权限，是否现在去开启？");
    }

    private void showDeniedDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    viewModel.requestCallPermissionDoc.set(true);
                    AppUtils.launchAppDetailsSettings(getActivity().getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

}
