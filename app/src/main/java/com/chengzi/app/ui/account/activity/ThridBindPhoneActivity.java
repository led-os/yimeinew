package com.chengzi.app.ui.account.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.nevermore.oceans.uits.ImageLoader;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityThridBindPhoneBindingImpl;
import com.chengzi.app.third.UmHelper;
import com.chengzi.app.ui.account.viewmodel.LoginViewModel;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.utils.NimUtils;

/**
 * 三方注册绑定账户
 *
 * @ClassName:ThridBindPhoneActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   16:07
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */
public class ThridBindPhoneActivity extends BaseActivity<ActivityThridBindPhoneBindingImpl, LoginViewModel> implements View.OnClickListener {
    //用户身份 (1普通用户) 2医生 3咨询师 4医院
    private int type;
    //是否选中用户协议(默认选中)   是否可点验证码      是否可点注册
    boolean isClickCode = false, isClickRegister = false, isChoose = true;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_thrid_bind_phone;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        type = AccountHelper.getIdentity();

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
        mViewModel.wxBindLiveData.observe(this, beans -> {
            dismissLoading();
            if (beans != null && beans.isSuccess()) {
                UserInfoBean bean = beans.getData();
                NimUtils.login(bean.getYunxin_accid(), bean.getYunxin_token(), new RequestCallbackWrapper<LoginInfo>() {
                    @Override
                    public void onResult(int code, LoginInfo result, Throwable exception) {
                        if (code == ResponseCode.RES_SUCCESS) {

                            AccountHelper.setYunxinToken(bean.getYunxin_token());
                            AccountHelper.setYunxinAccid(bean.getYunxin_accid());

                            UserInfoBean userInfoBean = bean;
                            AccountHelper.login(userInfoBean.getToken(), userInfoBean.getUser_id(), userInfoBean.getMobile(), userInfoBean.getGender(), userInfoBean.getImage(), userInfoBean.getName(), userInfoBean.getOnline_state());
                            AccountHelper.setNickname(userInfoBean.getName());
                            AccountHelper.setCity(userInfoBean.getCity_id(), userInfoBean.getCity_name(), userInfoBean.getLatitude(), userInfoBean.getLongitude());
                            UmHelper.addUmengAlias(userInfoBean.getUser_id(), ThridBindPhoneActivity.this);
                            //认证 -
                            if (userInfoBean.getJump().equals("0")) {
                                //用户身份 0未登录 1普通用户 2医生 3咨询师 4医院
                                if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                                    goActivity(DoctorCertifiedActivity.class);
                                } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
                                    goActivity(CounselorCertifiedActivity.class);
                                } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                                    goActivity(HospitalCertifiedActivity.class);
                                } else {
                                    finishAffinity();
                                    goActivity(MainActivity.class);
                                }
                            } else {
                                finishAffinity();
                                goActivity(MainActivity.class);
                            }
                        } else {

                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_code:  //获取验证码
                if (isClickCode) {
                    boolean checkResult = ContentChecker.getChecker(new ContentBody("手机号", mBinding.etPhone.getText().toString().trim()))
                            .addCondition(new NotEmptyCondition(this))
                            .addCondition(new PhoneNumCondition())
                            .getCheckResult();
                    if (checkResult) {
                        showLoading(Sys.LOADING);
                        mViewModel.sendCode(mBinding.etPhone.getText().toString().trim());
                    }
                }
                break;
            case R.id.tv_register://确认注册三方
                if (isClickRegister) {
                    String phone = mBinding.etPhone.getText().toString().trim();
                    ContentBody mobileBody = new ContentBody("手机号", phone);
                    String codes = mBinding.etCode.getText().toString().trim();
                    ContentBody code = new ContentBody("验证码", codes);
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
                            showLoading("");
                            mViewModel.thirdBind(phone, trim1, trim2, codes);
                        } else {
                            toast("两次密码输入不一致!");
                        }
                    }
                }
                break;
            case R.id.iv_announcement://同意用户协议
            case R.id.tv_announcement:
                if (isChoose) {
                    isChoose = false;
                } else {
                    isChoose = true;
                }
                ImageLoader.loadImage(mBinding.ivAnnouncement, isChoose ? R.drawable.gouxuan_yigouxuan : R.drawable.gouxuan_weigouxuan);
                break;
            case R.id.tv_user_agreement://14-用户使用协议  15-医生使用协议  16-咨询师使用协议  17-医院使用协议
//                goActivity(UserAgreementActivity.class);
                String t_id = "14";
                if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                    t_id = "15";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
                    t_id = "16";
                } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                    t_id = "17";
                }
                LookVipIntroduceActivity.start(this, t_id);
                break;
        }
    }
}