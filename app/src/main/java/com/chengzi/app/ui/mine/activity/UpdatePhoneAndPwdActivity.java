package com.chengzi.app.ui.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityUpdatePhoneAndPwdBindingImpl;
import com.chengzi.app.event.UpdatePasswordEvent;
import com.chengzi.app.ui.account.activity.LoginActivity;
import com.chengzi.app.ui.account.viewmodel.RegisterViewModel;
import com.chengzi.app.ui.mine.model.UpdatePhoneAndPwdViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * 修改手机号1 或者密码2
 *
 * @ClassName:UpdatePhoneAndPwdActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/8 0008   13:55
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */

public class UpdatePhoneAndPwdActivity extends BaseActivity<ActivityUpdatePhoneAndPwdBindingImpl, UpdatePhoneAndPwdViewModel> {

    private RegisterViewModel mRegViewModel;

    private int type;

    public static void start(Context context, int type) {
        context.startActivity(new Intent(context, UpdatePhoneAndPwdActivity.class)
                .putExtra("type", type)
        );
    }

    //   是否可点验证码      是否可点注册
    boolean isClickCode = false, isClickRegister = false;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_update_phone_and_pwd;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mRegViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        type = getIntent().getIntExtra("type", 1);
        mBinding.topBar.setCenterText(type == 1 ? "修改手机号" : "修改密码");
        mBinding.etPwd.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        mBinding.etPwdAgain.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
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
                if (type == 2) {
                    if (mBinding.etPhone.getText().length() == 11
                            && mBinding.etPwd.getText().length() >= 7
                            && mBinding.etPwdAgain.getText().length() >= 7) {
                        mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_red_background);
                        isClickRegister = true;
                    } else {
                        mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_gray_background);
                        isClickRegister = false;
                    }
                } else {
                    if (mBinding.etPhone.getText().length() == 11) {
                        mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_red_background);
                        isClickRegister = true;
                    } else {
                        mBinding.tvRegister.setBackgroundResource(R.drawable.rect_btn_gray_background);
                        isClickRegister = false;
                    }
                }
            }
        });
        if (type == 2) {
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
        }
        mBinding.setListener(setClickListener);


        resultData();
    }

    private void resultData() {
        mRegViewModel.sendCodeLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                mBinding.tvGetCode.startCountDown();
            }
        });

        mViewModel.responseBeanLiveData.observe(this, responseBean -> {
            if (responseBean != null && responseBean.isSuccess()) {
                toast("修改成功");
                int userType = AccountHelper.getIdentity();//将之前的用户类型取出了
                AccountHelper.logout();
                AccountHelper.setIdentity(userType);
                goActivity(LoginActivity.class);
                EventBus.getDefault().post(new UpdatePasswordEvent());
                finish();
            }
        });
    }

    private ClickEventHandler<Object> setClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_get_code: //获取验证码
                if (isClickCode) {
                    boolean checkResult = ContentChecker.getChecker(new ContentBody("手机号", mBinding.etPhone.getText().toString().trim()))
                            .addCondition(new NotEmptyCondition(this))
                            .addCondition(new PhoneNumCondition())
                            .getCheckResult();
                    if (checkResult) {
                        mRegViewModel.mobile.set(mBinding.etPhone.getText().toString().trim());
                        mRegViewModel.sendCode();
                    }
                }
                break;
            case R.id.tv_register:      //确认
                if (type == 1) {//修改手机号
                    ContentBody mobileBody = new ContentBody("手机号", mBinding.etPhone.getText().toString().trim());
                    ContentBody code = new ContentBody("验证码", mBinding.etCode.getText().toString().trim());
                    boolean checkResult = ContentChecker.getCheckMachine()
                            .putChecker(ContentChecker.getChecker(mobileBody)
                                    .addCondition(new NotEmptyCondition(this))
                                    .addCondition(new PhoneNumCondition()))
                            .putChecker(ContentChecker.getChecker(code)
                                    .addCondition(new NotEmptyCondition(this))).checkAll();
                    if (checkResult) {
                        showLoading("修改中");
                        mViewModel.changeMobile(mBinding.etPhone.getText().toString().trim(), mBinding.etCode.getText().toString().trim());
                    }
                } else {
                    ContentBody mobileBody = new ContentBody("手机号", mBinding.etPhone.getText().toString().trim());
                    ContentBody pwd = new ContentBody("新密码", mBinding.etPwd.getText().toString().trim());
                    ContentBody repwd = new ContentBody("重复新密码", mBinding.etPwdAgain.getText().toString().trim());
                    ContentBody code = new ContentBody("验证码", mBinding.etCode.getText().toString().trim());
                    boolean checkResult = ContentChecker.getCheckMachine()
                            .putChecker(ContentChecker.getChecker(mobileBody)
                                    .addCondition(new NotEmptyCondition(this))
                                    .addCondition(new PhoneNumCondition()))
                            .putChecker(ContentChecker.getChecker(pwd)
                                    .addCondition(new NotEmptyCondition(this)))
                            .putChecker(ContentChecker.getChecker(repwd)
                                    .addCondition(new NotEmptyCondition(this)))
                            .putChecker(ContentChecker.getChecker(code)
                                    .addCondition(new NotEmptyCondition(this))).checkAll();

                    if (checkResult) {
                        if (mBinding.etPwd.getText().toString().trim().equals(mBinding.etPwdAgain.getText().toString().trim())) {
                            showLoading("修改中");
                            mViewModel.changePwd(mBinding.etPhone.getText().toString().trim(), mBinding.etPwd.getText().toString().trim(), mBinding.etPwdAgain.getText().toString().trim(), mBinding.etCode.getText().toString().trim());
                        } else {
                            toast("两次密码不一致");
                        }
                    }
                }
                break;
        }
    };
}