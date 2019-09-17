package com.chengzi.app.ui.account.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityFindPwdBindingImpl;
import com.chengzi.app.ui.account.viewmodel.FindPwdViewModel;

/**
 * 找回密码
 *
 * @ClassName:FindPwdActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   15:45
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */

public class FindPwdActivity extends BaseActivity<ActivityFindPwdBindingImpl, FindPwdViewModel> implements View.OnClickListener {
    //    //用户身份 1普通用户 2医生 3咨询师 4医院
//    private int type;
    //是否选中用户协议(默认选中)   是否可点验证码      是否可点注册
    boolean isClickCode = false, isClickRegister = false;

    /*
    *  //获取验证码  device_id=1代表安卓
    public void sendCode() {
        accountService.sendCode(mobile.get(), String.valueOf(AccountHelper.getIdentity()), "1")
                .subscribe(new SimpleObserver<ResponseBean>() {
                    @Override
                    public void onSuccess(ResponseBean bean) {
                        sendCodeLiveData.postValue(bean);
                    }
                });
    }
    * */

    @Override
    public int getLayoutRes() {
        return R.layout.activity_find_pwd;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);
//        type = AccountHelper.getIdentity();

        ///监听手机号长度
        mBinding.etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //长度发生变化，监听到输入的长度为 editText.getText().length()
                if (mBinding.etPhone.getText().length() == 11) {
                    mBinding.tvGetCode.setBackgroundResource(R.drawable.rect_right_red4_border);
                    isClickCode = true;
                } else {
                    mBinding.tvGetCode.setBackgroundResource(R.drawable.rect_right_gray4_border);
                    isClickCode = false;
                }
                if (mBinding.etPhone.getText().length() == 11
                        && mBinding.etPwd.getText().length() >= 7
                        && mBinding.etPwdAgain.getText().length() >= 7) {
                    mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_red_background);
                    isClickRegister = true;
                } else {
                    mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_gray_background);
                    isClickRegister = false;
                }
            }
        });
        mBinding.etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBinding.etPhone.getText().length() == 11
                        && mBinding.etPwd.getText().length() >= 7
                        && mBinding.etPwdAgain.getText().length() >= 7) {
                    mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_red_background);
                    isClickRegister = true;
                } else {
                    mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_gray_background);
                    isClickRegister = false;
                }
            }
        });
        mBinding.etPwdAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBinding.etPhone.getText().length() == 11
                        && mBinding.etPwd.getText().length() >= 7
                        && mBinding.etPwdAgain.getText().length() >= 7) {
                    mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_red_background);
                    isClickRegister = true;
                } else {
                    mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_gray_background);
                    isClickRegister = false;
                }
            }
        });
        observe();


    }

    private void observe() {
        //获取验证码
        mViewModel.sendCodeLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                mBinding.tvGetCode.startCountDown();
            }
        });
        //retrievePassword
        mViewModel.retrievePasswordLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                toast("请重新登录!");
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:  //获取验证码
                if (isClickCode) {
                    String mobile = mBinding.etPhone.getText().toString().trim();
                    boolean checkResult = ContentChecker.getChecker(new ContentBody("手机号", mobile))
                            .addCondition(new NotEmptyCondition(this))
                            .addCondition(new PhoneNumCondition())
                            .getCheckResult();
                    if (checkResult) {
//                        if (type == Sys.TYPE_USER) {    //用户
//                            toast("用户 验证码");
//                        } else if (type == Sys.TYPE_DOCTOR) {    //医生
//                            toast("医生 验证码");
//                        } else if (type == Sys.TYPE_COUNSELOR) {    //咨询师
//                            toast("咨询师 验证码");
//                        } else if (type == Sys.TYPE_HOSPITAL) {    //医院
//                            toast("医院 验证码");
//                        }
                        showLoading(Sys.LOADING);
                        mViewModel.sendCode();
                    }
                }
                break;
            case R.id.tv_register://找回密码
                if (isClickRegister) {
                    ContentBody mobileBody = new ContentBody("手机号", mBinding.etPhone.getText().toString().trim());
                    ContentBody code = new ContentBody("验证码", mBinding.etPwd.getText().toString().trim());
                    String trim1 = mBinding.etPwd.getText().toString().trim();
                    String trim2 = mBinding.etPwdAgain.getText().toString().trim();
                    ContentBody pwd = new ContentBody("密码", trim1);
                    ContentBody pwd1 = new ContentBody("密码", trim2);
                    boolean checkResult = ContentChecker.getCheckMachine()
                            .putChecker(ContentChecker.getChecker(mobileBody)
                                    .addCondition(new NotEmptyCondition(this))
                                    .addCondition(new PhoneNumCondition()))
                            .putChecker(ContentChecker.getChecker(code)
                                    .addCondition(new NotEmptyCondition(this)))
                            .putChecker(ContentChecker.getChecker(pwd)
                                    .addCondition(new NotEmptyCondition(this)))
                            .putChecker(ContentChecker.getChecker(pwd1)
                                    .addCondition(new NotEmptyCondition(this)))
                            .checkAll();
                    if (checkResult) {
                        if (trim1.equals(trim2)) {
//                            if (type == Sys.TYPE_USER) {    //用户
//                                toast("普通用户 找回密码");
//                            } else if (type == Sys.TYPE_DOCTOR) {    //医生
//                                toast("医生用户 找回密码");
//                            } else if (type == Sys.TYPE_COUNSELOR) {    //咨询师
//                                toast("咨询师 找回密码");
//                            } else if (type == Sys.TYPE_HOSPITAL) {    //医院
//                                toast("医院 找回密码");
//                            }
                            showLoading(Sys.LOADING);
                            mViewModel.retrievePassword();
                        } else {
                            toast("两次密码输入不一致!");
                        }
                    }
                }
                break;
        }
    }
}
