package com.chengzi.app.ui.account.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
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
import com.chengzi.app.databinding.ActivityRegisterBindingImpl;
import com.chengzi.app.third.UmHelper;
import com.chengzi.app.ui.account.viewmodel.RegisterViewModel;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.utils.NimUtils;

/**
 * 1注册 2普通用户绑定手机号(同注册页面 同三方登录绑定手机号功能ThridBindPhoneActivity)
 *
 * @ClassName:RegisterActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/4/9 0009   17:34
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */

public class RegisterActivity extends BaseActivity<ActivityRegisterBindingImpl, RegisterViewModel> implements View.OnClickListener {
    //用户身份 1注册 2普通用户绑定手机号
    private int activityType;
    //用户身份 1普通用户 2医生 3咨询师 4医院
    private int type;
    //是否选中用户协议(默认选中)   是否可点验证码      是否可点注册
    boolean isChoose = true, isClickCode = false, isClickRegister = false;

    public static void start(Context context, int activityType) {
        context.startActivity(new Intent(context, RegisterActivity.class)
                .putExtra("activityType", activityType)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);
        type = AccountHelper.getIdentity();
        activityType = getIntent().getIntExtra("activityType", -1);
        mBinding.topBar.setCenterText(activityType == 1 ? "注册" : "账户绑定");

        //注册的时候 显示的用户协议
        if (activityType == 1) {
            String indentity = "";
            if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                indentity = "医生";
            } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
                indentity = "咨询师";
            } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                indentity = "医院";
            } else {
                indentity = "用户";
            }
            mBinding.tvUserAgreement.setText(indentity + "使用协议");
        }

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

        mViewModel.registerLiveData.observe(this, bean -> {
            dismissLoading();
            if (bean == null) {
                ToastUtils.showShort("注册失败");
                return;
            }
            NimUtils.login(bean.getYunxin_accid(), bean.getYunxin_token(), new RequestCallbackWrapper<LoginInfo>() {
                @Override
                public void onResult(int code, LoginInfo result, Throwable exception) {
                    if (code != ResponseCode.RES_SUCCESS) {
                        ToastUtils.showShort("云信登录失败");
                        return;
                    }

                    AccountHelper.setYunxinToken(bean.getYunxin_token());
                    AccountHelper.setYunxinAccid(bean.getYunxin_accid());

                    UserInfoBean userInfoBean = bean;
                    AccountHelper.login(userInfoBean.getToken(), userInfoBean.getUser_id(), userInfoBean.getMobile(), userInfoBean.getGender(), userInfoBean.getImage(), userInfoBean.getName(), userInfoBean.getOnline_state());
                    AccountHelper.setNickname(userInfoBean.getName());
                    AccountHelper.setCity(userInfoBean.getCity_id(), userInfoBean.getCity_name(), userInfoBean.getLatitude(), userInfoBean.getLongitude());
                    UmHelper.addUmengAlias(userInfoBean.getUser_id(), RegisterActivity.this);

                    int identity = AccountHelper.getIdentity();
                    if (identity > 0 && identity <= 4) {
                        String[] tags = {"user", "doctor", "consultant", "service_organization"};
                        UmHelper.addTag(RegisterActivity.this, tags[identity - 1]);
                    }

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
                }
            });
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
                            UmHelper.addUmengAlias(userInfoBean.getUser_id(), RegisterActivity.this);
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

        //获取验证码
        mViewModel.sendCodeLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                mBinding.tvGetCode.startCountDown();
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
//                        if (activityType == 1) {
                        showLoading(Sys.LOADING);
                        mViewModel.sendCode();
//                        } else {
//                            toast("三方普通用户绑定手机号");
//                        }
                    }
                }
                break;
            case R.id.tv_register://注册/绑定三方普通用户
                if (isClickRegister) {
                    String phone = mBinding.etPhone.getText().toString().trim();
                    ContentBody mobileBody = new ContentBody("手机号", phone);
                    ContentBody code = new ContentBody("验证码", mBinding.etCode.getText().toString().trim());
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
                        if (trim1.equals(trim2) && isChoose) {
                            showLoading(Sys.LOADING);
                            if (activityType == 1) {
                                mViewModel.register();
                            } else {
                                showLoading("");
                                mViewModel.thirdBind();
                            }
                        } else if (!trim1.equals(trim2)) {
                            toast("两次密码输入不一致!");
                        } else if (!isChoose) {
                            toast("请阅读并同意用户使用协议!");
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
            case R.id.tv_user_agreement://用户协议- 14-用户使用协议 15-医生使用协议  16-咨询师使用协议  17-医院使用协议
//                goActivity(UserAgreementActivity.class);
                String t_id = "14";
                if (activityType == 1) {
                    if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
                        t_id = "15";
                    } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
                        t_id = "16";
                    } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
                        t_id = "17";
                    }
                }
                LookVipIntroduceActivity.start(this, t_id);
                break;
        }
    }
}