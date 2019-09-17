package com.chengzi.app.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.dialog.BaseDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogDialLayoutBinding;

public class DialDialog extends BaseDialog implements View.OnClickListener {

    private DialogDialLayoutBinding mBinding;

    private String phoneNumber;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_dial_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setListener(this);
        mBinding.tvDial.setText(getString(R.string.dial) + phoneNumber);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.showShort("号码错误");
                    return;
                }
                PhoneUtils.dial(phoneNumber);
                break;
        }
        dismissAllowingStateLoss();
    }
}
