package com.chengzi.app.ui.goods.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.base.BaseFragment;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.databinding.FragmentHotGoodsBinding;
import com.chengzi.app.dialog.DialDialog;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.home.bean.GoodBean;
import com.chengzi.app.ui.home.bean.HospitalBean;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class HotGoodsFragment extends BaseFragment<FragmentHotGoodsBinding, GoodsDetailViewModel>
        implements ClickEventHandler<HospitalBean> {

    private MainGoodsAdapter hotGoodsAdapter;

    public HotGoodsFragment() {
    }

    public static HotGoodsFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(HotGoodsFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new HotGoodsFragment();
            fragment.setArguments(args);
        }
        return (HotGoodsFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hot_goods;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setListener(this);

        initHotGoods();

        viewModel.goodDetailLive.observe(this, goodDetailBean -> {

            List<GoodBean> hot_data = goodDetailBean.getHot_data();
            hotGoodsAdapter.setNewData(hot_data);
            if (hotGoodsAdapter.getData().isEmpty()) {
                binding.tvHotGoodHint.setVisibility(View.GONE);
            } else {
                binding.tvHotGoodHint.setVisibility(View.VISIBLE);
            }

            HospitalBean hospital_data = goodDetailBean.getHospital_data();
            binding.setBean(hospital_data);

        });

    }

    private void initHotGoods() {
        hotGoodsAdapter = new MainGoodsAdapter();
        hotGoodsAdapter.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerHotGoods.setAdapter(hotGoodsAdapter);

    }

    @Override
    public void handleClick(View view, HospitalBean bean) {
        if (!ClickEvent.shouldClick(view)) {
            return;
        }
        switch (view.getId()) {
            case R.id.layout_org_homepage:
                HospitalHomePageActivity.start(getActivity(), bean == null ? "" : bean.getId());
                break;
            case R.id.image_dial:
                HotGoodsFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.requestCallPermission.get()) {
            viewModel.requestCallPermission.set(false);
            HotGoodsFragmentPermissionsDispatcher.showDailDialogWithPermissionCheck(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HotGoodsFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(value = {Manifest.permission.CALL_PHONE})
    void showDailDialog() {
        GoodDetailBean goodDetailBean = viewModel.goodDetailLive.getValue();
        if (goodDetailBean == null || goodDetailBean.getHospital_data() == null) {
            return;
        }
        HospitalBean hospital_data = goodDetailBean.getHospital_data();
        String mobile = hospital_data.getMobile();
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
