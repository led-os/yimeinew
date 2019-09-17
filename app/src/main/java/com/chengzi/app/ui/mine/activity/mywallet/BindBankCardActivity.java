package com.chengzi.app.ui.mine.activity.mywallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityBindBankCardBinding;
import com.handong.framework.base.BaseActivity;
/**
 * 绑定银行卡
 * @ClassName:BindBankCardActivity

 * @PackageName:com.chengzi.app.ui.mine.activity.mywallet

 * @Create On 2019/9/4 0004   16:28

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/9/4 0004 handongkeji All rights reserved.
 */
public class BindBankCardActivity extends BaseActivity<ActivityBindBankCardBinding, BindBankCardViewModel> implements View.OnClickListener {

    public static final int REQUEST_CODE_SELECT_CITY = 0x01;
    public static final int REQUEST_CODE_SELECT_BANK = 0x02;


    public static void start(Context context) {
        Intent intent = new Intent(context, BindBankCardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_bind_bank_card;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mViewModel.bindBankcardLive.observe(this,aBoolean -> {
            dismissLoading();
            ToastUtils.showShort("绑定成功");
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.el_choose_city:
                startActivityForResult(new Intent(this, SelectProvinceCitiesActivity.class),
                        REQUEST_CODE_SELECT_CITY, data -> {
//                            province_id = data.getStringExtra("province_id");
                            String province_name = data.getStringExtra("province_name");
//                            city_id = data.getStringExtra("city_id");
                            String city_name = data.getStringExtra("city_name");
                            mBinding.elChooseCity.setContent(province_name + " " + city_name);
                            mViewModel.provinceName.set(province_name);
                            mViewModel.cityName.set(city_name);
                        });
                break;
            case R.id.el_choose_bank:
                startActivityForResult(new Intent(this, SelectBankActivity.class), REQUEST_CODE_SELECT_BANK,
                        data -> {
                            String bankName = data.getStringExtra("bankName");
                            String bankId = data.getStringExtra("bankId");
                            mBinding.elChooseBank.setContent(bankName);
                            mViewModel.bankName = bankName;
                            mViewModel.bankId = bankId;
                        });
                break;
            case R.id.tv_bind:
                if (TextUtils.isEmpty(mViewModel.name.get()) || mViewModel.name.get().trim().isEmpty()) {
                    ToastUtils.showShort("请输入持卡人姓名！");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.idCardNumber.get())) {
                    ToastUtils.showShort("请输入持卡人身份证号！");
                    return;
                }
                if (!RegexUtils.isIDCard18(mViewModel.idCardNumber.get()) && !RegexUtils.isIDCard15(mViewModel.idCardNumber.get())) {
                    ToastUtils.showShort("持卡人身份证号码无效！");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.provinceName.get()) || TextUtils.isEmpty(mViewModel.cityName.get())) {
                    ToastUtils.showShort("请选择城市！");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.bankCardNumber.get()) || mViewModel.bankCardNumber.get().trim().isEmpty()) {
                    ToastUtils.showShort("请输入银行卡号！");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.bankId)) {
                    ToastUtils.showShort("请选择银行卡所属银行！");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.mobile.get())) {
                    ToastUtils.showShort("请输入银行预留手机号！");
                    return;
                }
                if (!RegexUtils.isMobileSimple(mViewModel.mobile.get())) {
                    ToastUtils.showShort("银行预留手机号不正确！");
                    return;
                }
                showLoading("");
                mViewModel.bindBankCard();
                break;
        }
    }
}
