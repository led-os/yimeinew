package com.chengzi.app.ui.mine.activity.docotorroborder;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityRobOrderBindingImpl;
import com.chengzi.app.databinding.ItemRobOrderLayoutBindingImpl;
import com.chengzi.app.ui.mine.bean.SheetListBean;
import com.chengzi.app.ui.mine.viewmodel.RobOrderViewModel;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 私享咨询抢单(医生 咨询师)0 / 在线面诊抢单1
 *
 * @ClassName:RobOrderActivity
 * @PackageName:com.yimei.app.ui.mine.activity.docotorroborder
 * @Create On 2019/4/9 0009   10:26
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */
@RuntimePermissions
public class RobOrderActivity extends BaseActivity<ActivityRobOrderBindingImpl, RobOrderViewModel> {


    private RobOrderAdapter adapter;
    private PagingLoadHelper helper;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_rob_order;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        int sheet_type = getIntent().getIntExtra("type", 1);
        mBinding.topBar.setCenterText(sheet_type == 1 ? "私享咨询抢单" : "在线面诊抢单");
        mViewModel.setSheetType(sheet_type);

        adapter = new RobOrderAdapter();
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setPullupEnable(false);
        mBinding.swipeRefreshView.setLoadMoreViewGone(true);

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);

        observe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.start();
        if (mViewModel.requestCallPermissionDoc.get()) {
            mViewModel.requestCallPermissionDoc.set(false);
            RobOrderActivityPermissionsDispatcher.startVideoCallWithPermissionCheck(this);
        }
    }

    private void observe() {
        //列表

        mViewModel.sheetListLiveData.observe(this, data -> {
            if (data != null) {
                helper.onComplete(data);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });

        //抢单
        mViewModel.sheetJoinLiveData.observe(this, pair -> {
            dismissLoading();
            SheetListBean sheetBean = pair.first;
            String joinId = pair.second;
            RobOrderSucActivity.start(this, sheetBean.getId(), mViewModel.getSheetType(), sheetBean,joinId);
            helper.start();
        });
    }

    public class RobOrderAdapter extends BaseQuickAdapter<SheetListBean, BaseViewHolder> {
        public RobOrderAdapter() {
            super(R.layout.item_rob_order_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, SheetListBean item) {
            ItemRobOrderLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
            //tv_sex设置性别
            String gender = item.getGender();
            binding.tvSex.setBackgroundResource(gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);
        }
    }

    private ClickEventHandler<SheetListBean> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_rob_order:  //抢单
                showLoading(Sys.LOADING);
                if (mViewModel.getSheetType() == 2) {
                    mViewModel.setSheetListBean(bean);
                    RobOrderActivityPermissionsDispatcher.startVideoCallWithPermissionCheck(this);
                } else {
                    mViewModel.sheetJoin(bean, String.valueOf(mViewModel.getSheetType()));
                }
                break;
        }
    };

    @NeedsPermission(value = {Manifest.permission.CAMERA})
    public void startVideoCall() {
        mViewModel.sheetJoin(mViewModel.getSheetListBean(), String.valueOf(mViewModel.getSheetType()));
    }

    @OnPermissionDenied(value = {Manifest.permission.CAMERA})
    public void onCameraPermissionDemind() {
        showDeniedDialog("您拒绝了摄像头权限，使该功能无法使用，是否现在去开启？");
    }

    @OnNeverAskAgain(value = {Manifest.permission.CAMERA})
    void onCameraNeverAsk() {
        showDeniedDialog("您拒绝了摄像头权限，使该功能无法使用，是否现在去开启？");
    }

    private void showDeniedDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    mViewModel.requestCallPermissionDoc.set(true);
                    AppUtils.launchAppDetailsSettings(getPackageName());
                    dialog.dismiss();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RobOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}