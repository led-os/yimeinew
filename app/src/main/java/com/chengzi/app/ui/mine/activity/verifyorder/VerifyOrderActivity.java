package com.chengzi.app.ui.mine.activity.verifyorder;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.google.zxing.activity.CaptureActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityVerifyOrderBindingImpl;
import com.chengzi.app.ui.mine.bean.VerificationOrderBean;
import com.chengzi.app.ui.mine.viewmodel.UserBeautyRaiseViewModel;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * 验证订单-->医院
 *
 * @ClassName:VerifyOrderActivity
 * @PackageName:com.yimei.app.ui.mine.activity.verifyorder
 * @Create On 2019/4/12 0012   13:39
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */
@RuntimePermissions
public class VerifyOrderActivity extends BaseActivity<ActivityVerifyOrderBindingImpl, UserBeautyRaiseViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_verify_order;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        //验证订单
        mViewModel.verificationOrderLiveData.observe(this, verificationOrderBean -> {
            dismissLoading();
            if (verificationOrderBean != null && verificationOrderBean.getData() != null) {
                VerificationOrderBean data = verificationOrderBean.getData();
                startActivity(new Intent(this, VerifyResultActivity.class)
                        .putExtra("data", data)
                );
            } else {
//                toast("验证订单不存在或非待使用状态!");
            }
        });
    }

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_verify:  //验证
                String order_code = mBinding.etSeckillMoney.getText().toString().trim();
                if (!TextUtils.isEmpty(order_code))
                    observe(order_code);
                else {
                    toast("请输入订单验证码");
                }
                break;
            case R.id.ll_scan_qr_code_verifiy:  //扫描二维码验证
                VerifyOrderActivityPermissionsDispatcher.startCameraWithPermissionCheck(this);
                break;
        }
    };

    /**
     * 验证订单
     *
     * @param order_code
     */
    private void observe(String order_code) {
        showLoading(Sys.LOADING);
        mViewModel.verificationOrder(order_code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        //二维码扫描结果回调
        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        String scanResult = bundle.getString("qr_scan_result");
        //将扫描出的信息显示出来
        if (!TextUtils.isEmpty(scanResult)) {
            observe(scanResult);
        }
    }

    @NeedsPermission(value = {Manifest.permission.CAMERA})
    public void startCamera() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
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
        VerifyOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewModel.requestCallPermissionDoc.get()) {
            mViewModel.requestCallPermissionDoc.set(false);
            VerifyOrderActivityPermissionsDispatcher.startCameraWithPermissionCheck(this);
        }
    }

}
