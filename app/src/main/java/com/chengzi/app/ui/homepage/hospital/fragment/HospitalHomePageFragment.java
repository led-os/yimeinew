package com.chengzi.app.ui.homepage.hospital.fragment;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.netease.nim.uikit.api.yimei.ConsultantBean;
import com.netease.nim.uikit.api.yimei.ConsultantViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentHospitalHomePageBinding;
import com.chengzi.app.dialog.DialDialog;
import com.chengzi.app.ui.homepage.OnRefreshListener;
import com.chengzi.app.ui.homepage.RefreshListenerRegistry;
import com.chengzi.app.ui.homepage.activity.AppointmentActivity;
import com.chengzi.app.ui.homepage.bean.BelongOrgBean;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;
import com.chengzi.app.ui.homepage.bean.HospitalInfoBean;
import com.chengzi.app.ui.homepage.fragment.HisGoodsFragment;
import com.chengzi.app.ui.homepage.fragment.SubEstimateFragment;
import com.chengzi.app.ui.homepage.fragment.SubPeopleSayFragment;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;
import com.chengzi.app.utils.NimUtils;

import java.util.ArrayList;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 医院主页
 *
 * @ClassName:HospitalHomePageFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   10:54
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
@RuntimePermissions
public class HospitalHomePageFragment extends BaseFragment<FragmentHospitalHomePageBinding,
        HospitalHomePageViewModel> implements RefreshListenerRegistry, View.OnClickListener {

    private List<OnRefreshListener> listeners = new ArrayList<>();

    public HospitalHomePageFragment() {
    }

    public static HospitalHomePageFragment newInstance() {
        android.os.Bundle args = new Bundle();
        HospitalHomePageFragment fragment = new HospitalHomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_home_page;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setListener(this);

        binding.bottomLayout.setVisibility((viewModel.isSelf() || AccountHelper.getIdentity() > 1) ? View.GONE : View.VISIBLE);

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

        viewModel.promotionCut();

        //id	Number	  【必填】当前访问的对象ID（医生ID/咨询师ID/医院ID/商品ID等）
        //type	Number	  【必填】访问对象类型，1/咨询师；2/医生；3/机构；4/商品。
        viewModel.addVisit(viewModel.getHospitalId(), "3");
    }

    private void addFragments() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, HospitalInfoFragment.newInstance(getChildFragmentManager())
                        , HospitalInfoFragment.class.getSimpleName())
                .add(R.id.container, FeatureGoodsFragment.newInstance(getChildFragmentManager())
                        , FeatureGoodsFragment.class.getSimpleName())
                .add(R.id.container, HisGoodsFragment.newInstance(getChildFragmentManager(),
                        viewModel.getHospitalId(), 4, false)
                        , HisGoodsFragment.class.getSimpleName())
                .add(R.id.container, SubDoctorsFragment.newInstance(getChildFragmentManager(),
                        viewModel.getHospitalId(), 2, viewModel.isSelf())
                        , SubDoctorsFragment.class.getSimpleName() + "2")
                .add(R.id.container, SubDoctorsFragment.newInstance(getChildFragmentManager(),
                        viewModel.getHospitalId(), 3, viewModel.isSelf())
                        , SubDoctorsFragment.class.getSimpleName() + "3")
                .add(R.id.container, SubCaseFragment.newInstance(getChildFragmentManager(), viewModel.getHospitalId(), viewModel.isSelf())
                        , SubCaseFragment.class.getSimpleName())
                .add(R.id.container, SubEstimateFragment.newInstance(getChildFragmentManager(),
                        viewModel.getHospitalId(), 4),
                        SubEstimateFragment.class.getSimpleName())
                .add(R.id.container, SubPeopleSayFragment.newInstance(getChildFragmentManager(),
                        viewModel.getHospitalId(), 4),
                        SubPeopleSayFragment.class.getSimpleName())
                .add(R.id.container, HospitalLocationFragment.newInstance(getChildFragmentManager()),
                        HospitalLocationFragment.class.getSimpleName())
                .add(R.id.container, OtherHospitalFragment.newInstance(getChildFragmentManager()),
                        OtherHospitalFragment.class.getSimpleName())
                .commitAllowingStateLoss();
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
    public void onClick(View v) {
        if (!ClickEvent.shouldClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_online_services:  //  在线咨询
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (AccountHelper.getIdentity() != 1) {
                    ToastUtils.showShort("只有普通用户可以咨询");
                    return;
                }
                ConsultantViewModel consultantViewModel = ViewModelProviders.of(getActivity()).get(ConsultantViewModel.class);
                consultantViewModel.consultantLive.observe(this, new Observer<ConsultantBean>() {
                    @Override
                    public void onChanged(@Nullable ConsultantBean consultantBean) {
                        consultantViewModel.consultantLive.removeObserver(this);
                        if (consultantBean == null) {
                            ToastUtils.showShort("没有可咨询的咨询师了");
                            return;
                        }
                        consultantViewModel.consultantLive.setValue(null);
                        ConsultantBean.ConsultantsAccidBean consultants_accid = consultantBean.getConsultants_accid();
                        if (consultants_accid == null || TextUtils.isEmpty(consultants_accid.getYunxin_accid())
                                || TextUtils.isEmpty(consultants_accid.getId())) {
                            ToastUtils.showShort("没有可咨询的咨询师了");
                            return;
                        }
                        String yunxin_accid = consultants_accid.getYunxin_accid();
                        String consultantId = consultants_accid.getId();
                        if (!TextUtils.isEmpty(yunxin_accid) && !TextUtils.isEmpty(consultantId)) {
                            NimUtils.startP2PSession(getActivity(), yunxin_accid, 2, viewModel.getHospitalId(), consultantId);
                        }
                    }
                });
                consultantViewModel.setQueryId(viewModel.getHospitalId());
                consultantViewModel.setGetConsultantsType("service_organization");
                consultantViewModel.getConsultant();
                break;
            case R.id.btn_appointment_doctor:   //  预约医院 / 机构
                if (AccountHelper.shouldLogin(getActivity())) {
                    return;
                }
                if (viewModel.hospitalDetailLive.getValue() == null) {
                    return;
                }
                HospitalInfoBean info = viewModel.hospitalDetailLive.getValue().getInfo();
                BelongOrgBean belongOrg1 = new BelongOrgBean(info.getId(), info.getName(), info.getHeadimg());
//                startActivity(new Intent(getActivity(), AppointmentActivity.class)
//                        .putExtra("bean", belongOrg1)
//                );
                AppointmentActivity.start(getContext(), belongOrg1);
                break;
            case R.id.btn_dial:     //  拨打电话
                HospitalHomePageFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.requestCallPermissionHospital.get()) {
            viewModel.requestCallPermissionHospital.set(false);
            HospitalHomePageFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HospitalHomePageFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(value = {Manifest.permission.CALL_PHONE})
    void showDailDialog() {
        HospitalHomeInfoBean hospitalHomeInfoBean = viewModel.hospitalDetailLive.getValue();
        if (hospitalHomeInfoBean == null || hospitalHomeInfoBean.getInfo() == null) {
            return;
        }

        String mobile = hospitalHomeInfoBean.getInfo().getTelephone();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShort(String.format("该%s未设置服务电话，或服务电话错误", "医院"));
            return;
        }
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
                    viewModel.requestCallPermissionHospital.set(true);
                    AppUtils.launchAppDetailsSettings(getActivity().getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
