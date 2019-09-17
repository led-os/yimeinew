package com.chengzi.app.ui.account.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.BuildConfig;
import com.chengzi.app.MainActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityLoginBindingImpl;
import com.chengzi.app.event.OnLoginSuccessEvent;
import com.chengzi.app.third.UmHelper;
import com.chengzi.app.ui.account.bean.ThirdLoginBean;
import com.chengzi.app.ui.account.viewmodel.LoginViewModel;
import com.chengzi.app.ui.bean.account.UserInfoBean;
import com.chengzi.app.utils.NimUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * 登录
 *
 * @ClassName:LoginActivity
 * @PackageName:com.yimei.app.ui.account.activity
 * @Create On 2019/3/26 0026   10:51
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/3/26 0026 handongkeji All rights reserved.
 */

public class LoginActivity extends BaseActivity<ActivityLoginBindingImpl, LoginViewModel> implements View.OnClickListener {

    //用户身份 1普通用户 2医生 3咨询师 4医院
    private int type;

    boolean isClick = false;

    private UMShareAPI mShareAPI;
    private String openid, uid, iconurl;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);
        mShareAPI = UMShareAPI.get(this);
        type = AccountHelper.getIdentity();
        mBinding.tvUserType.setText("用户登录");
//        if (type == Sys.TYPE_USER) {    //用户
//            mBinding.tvUserType.setText("普通用户");
//        } else if (type == Sys.TYPE_DOCTOR) {    //医生
//            mBinding.tvUserType.setText("医生用户");
//        } else if (type == Sys.TYPE_COUNSELOR) {    //咨询师
//            mBinding.tvUserType.setText("咨询师");
//        } else if (type == Sys.TYPE_HOSPITAL) {    //医院
//            mBinding.tvUserType.setText("医院");
//        }
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
                if (mBinding.etPhone.getText().length() == 11 && mBinding.etPwd.getText().length() >= 7) {
                    mBinding.tvLogin.setBackgroundResource(R.drawable.rect_btn_red_background);
                    isClick = true;
                } else {
                    mBinding.tvLogin.setBackgroundResource(R.drawable.rect_btn_gray_background);
                    isClick = false;
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
                //长度发生变化，监听到输入的长度为 editText.getText().length()
                if (mBinding.etPhone.getText().length() == 11 && mBinding.etPwd.getText().length() >= 7) {
                    mBinding.tvLogin.setBackgroundResource(R.drawable.rect_btn_red_background);
                    isClick = true;
                } else {
                    mBinding.tvLogin.setBackgroundResource(R.drawable.rect_btn_gray_background);
                    isClick = false;
                }
            }
        });

        mViewModel.loginLiveData.observe(this, bean -> {
            dismissLoading();
            if (bean == null) {
                ToastUtils.showShort("登录失败");
                return;
            }
            NimUtils.login(bean.getYunxin_accid(), bean.getYunxin_token(), new RequestCallbackWrapper<LoginInfo>() {
                @Override
                public void onResult(int code, LoginInfo result, Throwable exception) {
                    if (code == ResponseCode.RES_SUCCESS) {
                        if (BuildConfig.DEBUG) {
                            Log.d("aaa", "onResult: 登录云信成功");
                        }
                    }
                }
            });

            AccountHelper.setYunxinToken(bean.getYunxin_token());
            AccountHelper.setYunxinAccid(bean.getYunxin_accid());

            UserInfoBean userInfoBean = bean;
            AccountHelper.login(userInfoBean.getToken(), userInfoBean.getUser_id(), userInfoBean.getMobile(), userInfoBean.getGender(), userInfoBean.getImage(), userInfoBean.getName(), userInfoBean.getOnline_state());
            AccountHelper.setNickname(userInfoBean.getName());
            AccountHelper.setIdentity(userInfoBean.getType());
            AccountHelper.setAuthStatus(userInfoBean.getAuth_status());

            AccountHelper.setCity(userInfoBean.getCity_id(), userInfoBean.getCity_name(),
                    userInfoBean.getLatitude(), userInfoBean.getLongitude());

            UmHelper.addUmengAlias(userInfoBean.getUser_id(), LoginActivity.this);
            // 登录成功后 直接返回上个界面，不用再跳转到 MainActivity
            MainActivity.isSetMine = true;

            int identity = AccountHelper.getIdentity();
            if (identity > 0 && identity <= 4) {
                String[] tags = {"user", "doctor", "consultant", "service_organization"};
                UmHelper.addTag(LoginActivity.this, tags[identity - 1]);
            }

            EventBus.getDefault().post(new OnLoginSuccessEvent());

            finish();
        });

        mViewModel.thirdLoginLiveData.observe(this, beans -> {
            dismissLoading();
            //todo  判断是否已经存在用户，如果已经存在去主页，否则去绑定手机号
            if (beans.getCode() == Sys.SUCCESS_STATUS) { //登录成功
                ThirdLoginBean.DataEntity bean = beans.getData();
                String yunxin_accid = bean.getYunxin_accid();
                String yunxin_token = bean.getYunxin_token();
                if (!TextUtils.isEmpty(yunxin_accid) && !TextUtils.isEmpty(yunxin_token)) {
                    NimUtils.login(bean.getYunxin_accid(), bean.getYunxin_token(), new RequestCallbackWrapper<LoginInfo>() {
                        @Override
                        public void onResult(int code, LoginInfo result, Throwable exception) {
                            if (code == ResponseCode.RES_SUCCESS) {

                                AccountHelper.setYunxinToken(bean.getYunxin_token());
                                AccountHelper.setYunxinAccid(bean.getYunxin_accid());

                                AccountHelper.login(bean.getToken(), bean.getId(), bean.getMobile(), bean.getGender(), bean.getImage(), bean.getName(), bean.getOnline_state());
                                AccountHelper.setNickname(bean.getName());
                                String type = bean.getType();
                                AccountHelper.setIdentity(!TextUtils.isEmpty(type) ? Integer.parseInt(type) : 1);

                                AccountHelper.setCity(bean.getCity_id(), bean.getChange_city(),
                                        bean.getLatitude(), bean.getLongitude());

                                UmHelper.addUmengAlias(bean.getId(), LoginActivity.this);
                                // 登录成功后 直接返回上个界面，不用再跳转到 MainActivity
                                MainActivity.isSetMine = true;

                                int identity = AccountHelper.getIdentity();
                                if (identity > 0 && identity <= 4) {
                                    String[] tags = {"user", "doctor", "consultant", "service_organization"};
                                    UmHelper.addTag(LoginActivity.this, tags[identity - 1]);
                                }

                                finish();
                            }
                        }
                    });
                } else {    //说明调起过微信->但是还没有绑定手机号
//                    AccountHelper.saveOther(beans.getData().getId(), openid);
//                    LoginChooseActivity.start(this);
                }
            } else if (beans.getCode() == 201) {   //去绑定手机号
                //选择用户身份-->
                AccountHelper.saveOther(openid, iconurl, uid);
                LoginChooseActivity.start(this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:  //注册账号
                goActivity(LoginChooseActivity.class);
                break;
            case R.id.tv_find_pwd:  //找回密码
                goActivity(FindPwdActivity.class);
                break;
            case R.id.tv_login:     //登录  4465
                if (isClick) {
                    ContentBody mobileBody = new ContentBody("手机号", mBinding.etPhone.getText().toString().trim());
                    ContentBody code = new ContentBody("密码", mBinding.etPwd.getText().toString().trim());
                    boolean checkResult = ContentChecker.getCheckMachine()
                            .putChecker(ContentChecker.getChecker(mobileBody)
                                    .addCondition(new NotEmptyCondition(this))
                                    .addCondition(new PhoneNumCondition()))
                            .putChecker(ContentChecker.getChecker(code)
                                    .addCondition(new NotEmptyCondition(this)))
                            .checkAll();
                    if (checkResult) {
                        showLoading(Sys.LOADING);
                        mViewModel.login();
                    }
                }
                break;
            case R.id.iv_weixin_login:  //微信登录
                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
                break;
        }
    }

    UMAuthListener authListener = new UMAuthListener() {

        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //
            Log.d("TAG", ">>>>>>> onComplete >>>>>>>");
//            String uid = data.get("uid");
//            uid = "";
            if (platform == SHARE_MEDIA.WEIXIN) {
                //unionid:（6.2以前用unionid）uid
                uid = data.get("unionid");
            } else {
                uid = data.get("uid");
            }
            openid = data.get("openid");
            iconurl = data.get("iconurl");
            String accessToken = data.get("accessToken");

            mViewModel.thirdLogin(openid, uid, iconurl);
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Log.d("TAG", ">>>>>>> onError >>>>>>>");
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Log.d("TAG", ">>>>>>> onCancel >>>>>>>");
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}