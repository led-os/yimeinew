package com.chengzi.app.ui.homepage.hospital.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentHospitalLocationBinding;
import com.chengzi.app.dialog.DialDialog;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;
import com.chengzi.app.ui.homepage.bean.HospitalInfoBean;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class HospitalLocationFragment extends RefreshableFragment<FragmentHospitalLocationBinding,
        HospitalHomePageViewModel> implements ClickEventHandler<HospitalInfoBean> {

    public HospitalLocationFragment() {
    }

    public static HospitalLocationFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(HospitalLocationFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new HospitalLocationFragment();
            fragment.setArguments(args);
        }
        return (HospitalLocationFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_location;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this);
        //  只有普通用户才能拨打电话
        binding.imageDial.setVisibility(AccountHelper.getIdentity() > 1 ? View.INVISIBLE : View.VISIBLE);
        binding.imageDial.setEnabled(AccountHelper.getIdentity() == 1);
        viewModel.hospitalDetailLive.observe(this,bean -> {
            binding.setBean(bean.getInfo());
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean isRefreshFinished() {
        return true;
    }

    @Override
    public void handleClick(View view, HospitalInfoBean bean) {
        HospitalLocationFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.requestCallPermission.get()) {
            viewModel.requestCallPermission.set(false);
            HospitalLocationFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
        }
    }

        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HospitalLocationFragmentPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    @NeedsPermission(value = {Manifest.permission.CALL_PHONE})
    void showDailDialog() {

        HospitalHomeInfoBean value = viewModel.hospitalDetailLive.getValue();
        if (value == null || value.getInfo() == null) {
            return;
        }
        HospitalInfoBean info = value.getInfo();
        String mobile = info.getTelephone();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShort("该医院未设置服务电话，或服务电话错误");
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
                    viewModel.requestCallPermission.set(true);
                    AppUtils.launchAppDetailsSettings(getActivity().getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
