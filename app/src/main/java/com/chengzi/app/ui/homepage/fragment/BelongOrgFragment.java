package com.chengzi.app.ui.homepage.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentBelongOrgBinding;
import com.chengzi.app.dialog.DialDialog;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.bean.BelongOrgBean;
import com.chengzi.app.ui.homepage.bean.DoctorHomeInfoBean;
import com.chengzi.app.ui.homepage.doctor.viewmodel.DoctorHomePageViewModel;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 所属机构
 *
 * @ClassName:BelongOrgFragment
 * @PackageName:com.yimei.app.ui.doctorhomepage.fragment
 * @Create On 2019/4/18 0018   10:12
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
@RuntimePermissions
public class BelongOrgFragment extends RefreshableFragment<FragmentBelongOrgBinding, DoctorHomePageViewModel>
        implements ClickEventHandler<BelongOrgBean> {

    public BelongOrgFragment() {
    }

    public static BelongOrgFragment newInstance(FragmentManager fm) {
        BelongOrgFragment fragment = (BelongOrgFragment) fm.findFragmentByTag(BelongOrgFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new BelongOrgFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_belong_org;
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

        viewModel.doctorHomeLive.observe(this, doctorHomeInfoBean -> {
            BelongOrgBean belongOrg = doctorHomeInfoBean.getBelongOrg();
            if (belongOrg == null) {
                return;
            }
            binding.getRoot().setVisibility(View.VISIBLE);
            binding.setBean(belongOrg);
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
    public void handleClick(View view, BelongOrgBean bean) {
        switch (view.getId()) {
            case R.id.layout_org_homepage:
                HospitalHomePageActivity.start(getActivity(), bean.getId());
                break;
            case R.id.image_dial:
                BelongOrgFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.requestCallPermissionOrg.get()) {
            viewModel.requestCallPermissionOrg.set(false);
            BelongOrgFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BelongOrgFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(value = {Manifest.permission.CALL_PHONE})
    void showDailDialog() {
        DoctorHomeInfoBean doctorHomeInfoBean = viewModel.doctorHomeLive.getValue();
        if (doctorHomeInfoBean == null || doctorHomeInfoBean.getBelongOrg() == null) {
            return;
        }
        BelongOrgBean belongOrg = doctorHomeInfoBean.getBelongOrg();
        String mobile = belongOrg.getTelephone();
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
                    viewModel.requestCallPermissionOrg.set(true);
                    AppUtils.launchAppDetailsSettings(getActivity().getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }
}
