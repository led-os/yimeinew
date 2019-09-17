package com.chengzi.app.ui.mine.activity.mywallet;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alipay.sdk.app.AuthTask;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMyWalletBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.mine.bean.WalletBean;
import com.chengzi.app.ui.mine.viewmodel.MyWalletViewModel;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * 钱包 -->普通用户 医生 咨询师 医院  通用页面
 *
 * @ClassName:MyWalletActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/1 0001   14:20
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */

public class MyWalletActivity extends BaseActivity<ActivityMyWalletBindingImpl, MyWalletViewModel> implements View.OnClickListener {

    private UMShareAPI mShareAPI;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_wallet;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this::onClick);
        if (AccountHelper.getIdentity() == Sys.TYPE_USER
                || AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR
                || AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
            //医生用户/咨询师 微信 支付宝 (对公账户) 账户明细 提现 (提示语)
            setVisiblity(false, true, false, false, true, true);
        } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
            //医院     (微信 支付宝) 对公账户 账户明细 提现 (提示语)
            setVisiblity(false, false, true, false, true, true);
        }

        mShareAPI = UMShareAPI.get(this);
        resultData();
    }

    private void resultData() {
        /**
         * 钱包信息
         */
        mViewModel.walletInfoLiveData.observe(this, walletBeanResponseBean -> {
            dismissLoading();
            if (walletBeanResponseBean != null && walletBeanResponseBean.getData() != null) {
                initUI(walletBeanResponseBean.getData());
            }
        });

        /**
         * 绑定或者解绑
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
        showLoading("加载中");
        mViewModel.walletInfo();
    }

    private void initUI(WalletBean data) {
        mBinding.tvAccountNum.setText(data.getSum());//余额

        //是否绑定微信 (0 未绑定  1 已经绑定)
        mBinding.tvWeixin.setTag(data.getBind_wx());
        mBinding.tvWeixin.setText(data.getBind_wx() == 0 ? "去绑定" : "解除绑定");
        mBinding.tvWeixin.setTextColor(data.getBind_wx() == 0 ? Color.parseColor("#FF526A") : Color.parseColor("#999999"));

        //是否绑定支付宝  (0 未绑定  1 已经绑定)
        mBinding.tvZhifubao.setTag(data.getBind_ali());
        mBinding.tvZhifubao.setText(data.getBind_ali() == 0 ? "去绑定" : "解除绑定");
        mBinding.tvZhifubao.setTextColor(data.getBind_ali() == 0 ? Color.parseColor("#FF526A") : Color.parseColor("#999999"));

        //是否绑定对公账户  (0 未绑定  1 已经绑定)
        mBinding.tvHospital.setTag(data.getBind_acount());
        mBinding.tvHospital.setText(data.getBind_acount() == 0 ? "去绑定" : "解除绑定");
        mBinding.tvHospital.setTextColor(data.getBind_acount() == 0 ? Color.parseColor("#FF526A") : Color.parseColor("#999999"));

        //是否绑定银行卡  (0 未绑定  1 已经绑定)
        mBinding.tvBankcard.setTag(data.getBind_acount());
        mBinding.tvBankcard.setText(data.getBind_acount() == 0 ? "去绑定" : "解除绑定");
        mBinding.tvBankcard.setTextColor(data.getBind_acount() == 0 ? Color.parseColor("#FF526A") : Color.parseColor("#999999"));

    }

    private void setVisiblity(boolean weixin, boolean zhifubao, boolean duigongzhanghu, boolean bankcard,
                              boolean accountdetails, boolean withdrawal) {
        mBinding.rlWeixin.setVisibility(weixin ? View.VISIBLE : View.GONE);
        mBinding.rlZhifubao.setVisibility(zhifubao ? View.VISIBLE : View.GONE);
        mBinding.rlHospital.setVisibility(duigongzhanghu ? View.VISIBLE : View.GONE);
        mBinding.rlBankcard.setVisibility(bankcard ? View.VISIBLE : View.GONE);
        mBinding.tvAccountDetails.setVisibility(accountdetails ? View.VISIBLE : View.GONE);
        mBinding.tvWithdrawal.setVisibility(withdrawal ? View.VISIBLE : View.GONE);
//        mBinding.tvAverageUserShow.setVisibility(toast ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_weixin:    //微信 绑定/解除绑定
            case R.id.tv_weixin:
                int bingWxTag = 0;
                if (mBinding.tvWeixin.getTag() != null) {
                    bingWxTag = (int) mBinding.tvWeixin.getTag();
                }
                if (bingWxTag == 0) {//未绑定
                    mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
                } else {//已经绑定
                    new MessageDialogBuilder(this)
                            .setMessage("确定解除绑定?")
                            .setSureListener(view1 -> {
                                mViewModel.unBindAccount(MyWalletViewModel.BIND_ACCOUNT_TYPE_WX);
                            })
                            .show();
                }
                break;
            case R.id.rl_zhifubao:    //支付宝 绑定/解除绑定
            case R.id.tv_zhifubao:


                int bingAlTag = 0;
                if (mBinding.tvZhifubao.getTag() != null) {
                    bingAlTag = (int) mBinding.tvZhifubao.getTag();
                }
                if (bingAlTag == 0) {//未绑定

                    mViewModel.aliAuthParams();

                    //弹窗绑定
//                    new BindAliDialog(this)
//                            .setSureListener(text -> {
//                                if (!TextUtils.isEmpty(text)) {
//                                    showLoading(Sys.LOADING);
//                                    mViewModel.bindALi(text);
//                                } else {
//                                    toast("请输入支付宝账号!");
//                                }
//
//                            })
//                            .setTitle("绑定支付宝")
//                            .setMessageHint("请输入支付宝账号")
//                            .setYuan(false)
//                            .show();
                } else {//已经绑定
                    new MessageDialogBuilder(this)
                            .setMessage("确定解除绑定?")
                            .setSureListener(view1 -> {
                                mViewModel.unBindAccount(MyWalletViewModel.BIND_ACCOUNT_TYPE_ALI);
                            })
                            .show();
                }
                break;
            case R.id.rl_hospital:    //对公账户 绑定/解除绑定
            case R.id.tv_hospital:
                int bingAccoutTag = 0;
                if (mBinding.tvHospital.getTag() != null) {
                    bingAccoutTag = (int) mBinding.tvHospital.getTag();
                }
                if (bingAccoutTag == 0) {//未绑定
                    goActivity(BindCorporateAccountActivity.class);
                } else {//已经绑定
                    new MessageDialogBuilder(this)
                            .setMessage("确定解除绑定?")
                            .setSureListener(view1 -> {
                                mViewModel.unBindAccount(MyWalletViewModel.BIND_ACCOUNT_TYPE_ACCOUNT);
                            })
                            .show();
                }
                break;
            case R.id.tv_account_details:    //账户明细
                goActivity(AccountDetailsActivity.class);
                break;
            case R.id.tv_withdrawal:    //提现
                goActivity(WithDrawalActivity.class);
                break;
            case R.id.rl_bankcard:      //  绑定银行卡
                if (mBinding.tvBankcard.getTag() != null && ((int) mBinding.tvBankcard.getTag() == 1)) {
                    new MessageDialogBuilder(this)
                            .setMessage("确定解除绑定?")
                            .setSureListener(view1 -> {
                                mViewModel.unBindAccount(MyWalletViewModel.BIND_ACCOUNT_TYPE_ACCOUNT);
                            })
                            .show();
                } else {
                    BindBankCardActivity.start(this);
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