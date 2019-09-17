package com.chengzi.app.ui.mine.activity.mywallet;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityBindCorporateAccountBindingImpl;
import com.chengzi.app.ui.account.viewmodel.RegisterViewModel;
import com.chengzi.app.ui.mine.bean.CoAccountBean;
import com.chengzi.app.ui.mine.viewmodel.MyWalletViewModel;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.nevermore.oceans.uits.content_check.ContentBody;
import com.nevermore.oceans.uits.content_check.ContentChecker;
import com.nevermore.oceans.uits.content_check.NotEmptyCondition;
import com.nevermore.oceans.uits.content_check.PhoneNumCondition;

/**
 * 钱包-->绑定对公账户
 *
 * @ClassName:BindCorporateAccountActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/1 0001   15:10
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */

public class BindCorporateAccountActivity extends BaseActivity<ActivityBindCorporateAccountBindingImpl, BaseViewModel> implements View.OnClickListener {

    public static final int REQUEST_CODE_SELECT_CITY = 0x01;
    public static final int REQUEST_CODE_SELECT_BANK = 0x02;

    private MyWalletViewModel mWalletViewModel;

    private RegisterViewModel mRegisterViewModel;

    private CoAccountBean mCoAccountBean;
    private String province_name, city_name;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_bind_corporate_account;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mWalletViewModel = ViewModelProviders.of(this).get(MyWalletViewModel.class);
        mRegisterViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        mBinding.setListener(this::onClick);
        mCoAccountBean = new CoAccountBean();
        mBinding.setDataBean(mCoAccountBean);

        //监听银行卡号输入-自动获取银行名称
        mBinding.etCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {//输入之后，校验银行卡
//                if (!TextUtils.isEmpty(mBinding.etCardNo.getText().toString().trim()) &&
//                        BankUtils.checkBankCard(mBinding.etCardNo.getText().toString().trim())) {
//                    BankInfoBean bankInfoBean = new BankInfoBean(mBinding.etCardNo.getText().toString().trim());
//                    mCoAccountBean.setBank_name(bankInfoBean.getBankName());
//                } else {
//
//                }
            }
        });
        resultData();
    }

    private void resultData() {
        mWalletViewModel.responseBeanLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                finish();
            }
        });

        //获取验证码
        mRegisterViewModel.sendCodeLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                mBinding.cdbGetCode.startCountDown();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.el_choose_city: //请选择银行卡所属城市
                startActivityForResult(new Intent(this, SelectProvinceCitiesActivity.class),
                        REQUEST_CODE_SELECT_CITY, data -> {
//                            province_id = data.getStringExtra("province_id");
                            province_name = data.getStringExtra("province_name");
//                            city_id = data.getStringExtra("city_id");
                            city_name = data.getStringExtra("city_name");
                            mBinding.elChooseCity.setContent(province_name + " " + city_name);
                        });
                break;

            case R.id.el_choose_bank:
                startActivityForResult(new Intent(this, SelectBankActivity.class), REQUEST_CODE_SELECT_BANK,
                        data -> {
                            String bankName = data.getStringExtra("bankName");
                            mCoAccountBean.setBank_name(bankName);
                        });
                break;
            case R.id.cdb_get_code: //获取验证码
                mBinding.cdbGetCode.startCountDown();

                ContentBody telBody = new ContentBody("预留手机号", mCoAccountBean.getTel());
                boolean checkAll = ContentChecker.getCheckMachine()
                        .putChecker(ContentChecker.getChecker(telBody)
                                .addCondition(new NotEmptyCondition(this))
                                .addCondition(new PhoneNumCondition()))
                        .checkAll();
                if (checkAll) {
                    showLoading(Sys.LOADING);
                    mRegisterViewModel.mobile.set(mCoAccountBean.getTel());
                    mRegisterViewModel.sendCode();
                }
                break;
            case R.id.tv_bind: //确定绑定
                ContentBody nameBody = new ContentBody("单位名称", mCoAccountBean.getName());
//                ContentBody idCardBody = new ContentBody("身份证号", mCoAccountBean.getCard_id());
                ContentBody bankNumBody = new ContentBody("银行卡号", mCoAccountBean.getBank_id());
                ContentBody bankNameBody = new ContentBody("银行卡名称", mCoAccountBean.getBank_name());
//                ContentBody mobileBody = new ContentBody("预留手机号", mCoAccountBean.getTel());
//                ContentBody code = new ContentBody("验证码", mCoAccountBean.getM_code());
                ContentChecker.Machine checker = ContentChecker.getCheckMachine()
                        .putChecker(ContentChecker.getChecker(nameBody)
                                .addCondition(new NotEmptyCondition(this)))
//                        .putChecker(ContentChecker.getChecker(idCardBody)
//                                .addCondition(new NotEmptyCondition(this))
//                                .addCondition(new IDCardNoCondition()))
                        .putChecker(ContentChecker.getChecker(bankNumBody)
                                .addCondition(new NotEmptyCondition(this)))
//                                .addCondition(new BankNumCondition()))
                        .putChecker(ContentChecker.getChecker(bankNameBody)
                                .addCondition(new NotEmptyCondition(this)))
                        .putChecker(ContentChecker.getChecker(nameBody)
                                .addCondition(new NotEmptyCondition(this)))
//                        .putChecker(ContentChecker.getChecker(idCardBody)
//                                .addCondition(new NotEmptyCondition(this))
//                                .addCondition(new IDCardNoCondition()))
                        .putChecker(ContentChecker.getChecker(bankNumBody)
                                .addCondition(new NotEmptyCondition(this)))
//                                .addCondition(new BankNumCondition()))
                        .putChecker(ContentChecker.getChecker(bankNameBody)
                                .addCondition(new NotEmptyCondition(this)));
//                        .putChecker(ContentChecker.getChecker(mobileBody)
//                                .addCondition(new NotEmptyCondition(this))
//                                .addCondition(new PhoneNumCondition()))
//                        .putChecker(ContentChecker.getChecker(code)
//                                .addCondition(new NotEmptyCondition(this)))

                ContentChecker contentChecker = ContentChecker.getChecker(new ContentBody("开户支行", mCoAccountBean.getOpeningBranch()))
                        .addCondition(new NotEmptyCondition(this, "请输入开户支行！"));
                checker.putChecker(contentChecker);

                boolean checkResult = checker.checkAll();
                if (checkResult) {
                    if (!TextUtils.isEmpty(province_name) && !TextUtils.isEmpty(city_name)) {
                        showLoading("绑定中");
                        mWalletViewModel.bindCoAccount(mCoAccountBean.getName(), mCoAccountBean.getCard_id(), mCoAccountBean.getBank_id(), mCoAccountBean.getBank_name(), //mCoAccountBean.getTel(), mCoAccountBean.getM_code(),
                                province_name, city_name, mCoAccountBean.getOpeningBranch());
                    } else {
                        toast("请选择该银行卡的所属城市");
                    }
                }
                break;
        }
    }

}