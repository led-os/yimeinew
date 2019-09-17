package com.chengzi.app.ui.mine.activity.mywallet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alipay.sdk.app.AuthTask;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityWithDrawalBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.dialog.SetLimitDialog;
import com.chengzi.app.dialog.WithDrawalDialog;
import com.chengzi.app.ui.mine.viewmodel.MyWalletViewModel;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * 提现
 *
 * @ClassName:WithDrawalActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/1 0001   16:20
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */
public class WithDrawalActivity extends BaseActivity<ActivityWithDrawalBindingImpl, MyWalletViewModel> implements View.OnClickListener {

    private int bindWX;
    private int bindALI;
    private int bindCoAccount;

    private String accountNum;//余额


    private UMShareAPI mShareAPI;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_with_drawal;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this::onClick);
        mShareAPI = UMShareAPI.get(this);
        resultData();

        //提现金额（小数点后两位）
        SetLimitDialog.setPriceMode(mBinding.etTixianNum, 2);
    }

    private void resultData() {
        /**
         * 钱包信息
         */
        mViewModel.walletInfoLiveData.observe(this, walletBeanResponseBean -> {
            dismissLoading();
            if (walletBeanResponseBean != null && walletBeanResponseBean.getData() != null) {
                bindWX = walletBeanResponseBean.getData().getBind_wx();
                bindALI = walletBeanResponseBean.getData().getBind_ali();
                bindCoAccount = walletBeanResponseBean.getData().getBind_acount();
                accountNum = walletBeanResponseBean.getData().getSum();

                mBinding.tvAccountNum.setText("可提现：¥" + accountNum);
            }
        });

        /**
         * 提现
         */
        mViewModel.withDrawLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                goActivity(ApplyWithDrawalsSucActivity.class);
            }
        });

        /**
         * 绑定
         */
        mViewModel.responseBeanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mViewModel.walletInfo();
            }
        });

        mViewModel.aliAuthLive.observe(this, info -> {
            if (TextUtils.isEmpty(info)) {
                return;
            }
            Schedulers.io().createWorker().schedule(() -> {
                AuthTask task = new AuthTask(this);
                Map<String, String> authV2 = task.authV2(info, true);
                Log.d("aaa", "onClick: " + authV2);
                if (!TextUtils.equals("9000", authV2.get("resultStatus"))) {
                    return;
                }
                String result = authV2.get("result");

                if (TextUtils.isEmpty(result)) {
                    return;
                }
                String auth_code = null;
                String[] split = result.split("&");
                for (String s : split) {
                    if (TextUtils.isEmpty(s) || !s.contains("=")) {
                        continue;
                    }
                    String[] strings = s.split("=");
                    if (strings.length != 2) {
                        continue;
                    }
                    if (TextUtils.equals(strings[0], "auth_code")) {
                        auth_code = strings[1];
                    }
                }
                Log.d("aaa", "resultData: " + auth_code);
                mViewModel.bindALi(auth_code);
            });

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading(Sys.LOADING);
        mViewModel.walletInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_withdrawal:    //切换 到账方式
                if (AccountHelper.getIdentity() != 4) {
                    //其他三类用户
                    new WithDrawalDialog(this)
                            .setTvText1Visible(false)
                            .setTvText1("微信")
                            .setText1Listener(v1 -> {
                                //微信
                                if (bindWX == 0) {//未绑定
                                    new MessageDialogBuilder(this)
                                            .setMessage("您还未绑定微信，不可提现")
                                            .setTvSure("去绑定")
                                            .setSureListener(v2 -> {
                                                //绑定微信
                                                mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
                                            })
                                            .show();
                                } else {
                                    Drawable weixin = getResources().getDrawable(R.drawable.zhifu_weixin);
                                    weixin.setBounds(0, 0, weixin.getMinimumWidth(), weixin.getMinimumHeight());
                                    Drawable right = getResources().getDrawable(R.drawable.jiantou_hui);
                                    right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
                                    mBinding.tvWay.setCompoundDrawables(weixin, null, right, null);
                                    mBinding.tvWay.setText("微信");
                                    mViewModel.type.set("0");
                                }

                            })
                            .setTvText2("支付宝")
                            .setText2Listener(v1 -> {
                                        //支付
                                        if (bindALI == 0) {
                                            new MessageDialogBuilder(this)
                                                    .setMessage("您还未绑定支付宝，不可提现")
                                                    .setTvSure("去绑定")
                                                    .setSureListener(v2 -> {

                                                        mViewModel.aliAuthParams();

                                                        //绑定支付宝
//                                                        showLoading(Sys.LOADING);
//                                                        mViewModel.getAliAuthInfo();
                                                        //弹窗绑定
//                                                        new BindAliDialog(this)
//                                                                .setSureListener(text -> {
//                                                                    if (!TextUtils.isEmpty(text)) {
//                                                                        showLoading(Sys.LOADING);
//                                                                        mViewModel.bindALi(text);
//                                                                    } else {
//                                                                        toast("请输入支付宝账号!");
//                                                                    }
//
//                                                                })
//                                                                .setTitle("绑定支付宝")
//                                                                .setMessageHint("请输入绑定的支付支付宝账号")
//                                                                .setYuan(false)
//                                                                .show();
                                                    })
                                                    .show();
                                        } else {
                                            Drawable alizhi = getResources().getDrawable(R.drawable.zhifu_zhifubao);
                                            alizhi.setBounds(0, 0, alizhi.getMinimumWidth(), alizhi.getMinimumHeight());
                                            Drawable right = getResources().getDrawable(R.drawable.jiantou_hui);
                                            right.setBounds(0, 0, right.getMinimumWidth(), right.getMinimumHeight());
                                            mBinding.tvWay.setCompoundDrawables(alizhi, null, right, null);
                                            mBinding.tvWay.setText("支付宝");
                                            mViewModel.type.set("1");
                                        }
                                    }

                            )
                            .show();
                } else {
                    //医院用户（AccountHelper.getIdentity() ==4）
                    new WithDrawalDialog(this)
                            .setTvText1("银行卡")
                            .setText1Listener(v1 -> {
                                //微信
                                if (bindCoAccount == 0) {//未绑定
                                    new MessageDialogBuilder(this)
                                            .setMessage("您还未绑定银行卡，请先绑定银行卡")
                                            .setTvSure("去绑定")
                                            .setSureListener(v2 -> {
                                                //去绑定
                                                goActivity(BindBankCardActivity.class);
                                            })
                                            .show();
                                } else {
                                    mBinding.tvWay.setText("银行卡");
                                    mViewModel.type.set("2");
                                }

                            })
                            .setTvText2Visible(false).show();
                }

                break;
            case R.id.tv_apply_withdrawals:    //申请提现
                if (!ClickEvent.shouldClick(v)) {//防止多次点击
                    return;
                }
                if (!TextUtils.isEmpty(mViewModel.type.get())) {
                    if (!TextUtils.isEmpty(mBinding.etTixianNum.getText().toString())) {
                        if (mBinding.etTixianNum.getText().toString().trim().startsWith(".")) {
                            toast("请输入正确金额");
                        } else {
                            double tixian = Double.parseDouble(mBinding.etTixianNum.getText().toString().trim());
                            double account = TextUtils.isEmpty(accountNum) ? 0.00 : Double.parseDouble(accountNum);

                            if (tixian < 10) {
                                toast("提现金额不能少于10元");
                                return;
                            }

                            if (account <= 0) {
                                ToastUtils.showShort("余额不足！");
                                return;
                            }

                            if (tixian > account) {
                                ToastUtils.showShort("您最大可提现：¥" + accountNum);
                                return;
                            }

                            if (AccountHelper.getIdentity() == 4) {
                                if (tixian > 50000) {
                                    ToastUtils.showShort("单次提现金额最多为5万元");
                                    return;
                                }
                            } else {
                                if (tixian > 20000) {
                                    ToastUtils.showShort("单次提现金额最多为2万元");
                                    return;
                                }
                            }

                            //提现
                            showLoading(Sys.LOADING);
                            mViewModel.withDraw(mBinding.etTixianNum.getText().toString().trim());

                        }
                    }
                } else {
                    toast("请选择到账方式");
                }
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
            String uid = data.get("uid");
            if (platform == SHARE_MEDIA.WEIXIN) {
                //微信授权
                Log.d("TAG", ">>>>>>> uid >>>>>>>" + uid);
                mViewModel.bindWX(uid);
            } else if (platform == SHARE_MEDIA.ALIPAY) {
//                //支付宝授权
//                mViewModel.bindALi(uid);
            }
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