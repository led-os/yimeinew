package com.chengzi.app.ui.peopleraise.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.dialog.OptionsDialog;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityPostPeopleRaiseBinding;
import com.chengzi.app.ui.common.bean.CityBean;
import com.chengzi.app.ui.home.activity.SelectCityActivity;
import com.chengzi.app.ui.pay.activity.PayActivity;
import com.chengzi.app.ui.peopleraise.viewmodel.PostPeopleRaiseViewModel;
import com.chengzi.app.ui.search.activity.AllGoodsActivity;

/**
 * 发布美人筹
 *
 * @ClassName:PostPeopleRaiseActivity
 * @PackageName:com.yimei.app.ui.peopleraise.activity
 * @Create On 2019/4/9 0009   16:42
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */
public class PostPeopleRaiseActivity extends BaseActivity<ActivityPostPeopleRaiseBinding,
        PostPeopleRaiseViewModel> implements View.OnClickListener {

    public static final int REQUEST_CODE_SELECT_TYPE = 0x01;
    public static final int REQUEST_CODE_SELECT_CITY = 0x02;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_post_people_raise;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);
        mBinding.setViewModel(mViewModel);

        mBinding.tvInitiator.setText(getString(R.string.initiator) + ":  " + AccountHelper.getNickname());

        mViewModel.addLive.observe(this, bean -> {
            dismissLoading();
            PayActivity.start(this, bean.getId(), 2);
            finish();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_type:  //  美人筹类型
                startActivityForResult(new Intent(this, AllGoodsActivity.class)
                                .putExtra(Sys.EXTRA, true)
                                .putExtra(Sys.EXTRA_FOR_CATEGORY, true)
                                .putExtra(Sys.EXTRA_OP_CATEGORY, true)
                        , REQUEST_CODE_SELECT_TYPE, data -> {

                            Bundle bundle = data.getExtras();

                            String third_id = bundle.getString("third_id");
                            String categoryName = bundle.getString("categoryName");

                            mViewModel.setCategoryId(third_id);
                            mBinding.tvType.setText(categoryName);

                        });
                break;
            case R.id.layout_location_city:  //  所在城市
                startActivityForResult(new Intent(this, SelectCityActivity.class)
                        , REQUEST_CODE_SELECT_CITY, data -> {
                            CityBean cityBean = (CityBean) data.getSerializableExtra("city");
                            mBinding.tvLocationCity.setText(cityBean.getCity_name());
                            mViewModel.setCityId(cityBean.getCity_id());
                        });
                break;
            case R.id.layout_time:  //  时间
                String[] options = {"3天", "7天", "15天"};
                new OptionsDialog.Builder<String>()
                        .showCancel(true)
                        .setData(options)
                        .setListener((view, s, index) -> {
                            mBinding.tvTime.setText(s);
                            String option = options[index];
                            mViewModel.setDays(option.substring(0, option.length() - 1));
                        })
                        .build()
                        .show(getSupportFragmentManager(), "OptionsDialog");
                break;
            case R.id.layout_aim_achieve:  //  达成目标
                String[] optNum = {"3人", "5人"};
                new OptionsDialog.Builder<String>()
                        .showCancel(true)
                        .setData(optNum)
                        .setListener((view, s, index) -> {
                            mBinding.tvAim.setText(s);
                            mViewModel.setPeopleNum(optNum[index].substring(0, 1));
                        })
                        .build()
                        .show(getSupportFragmentManager(), "OptionsDialog");
                break;
            case R.id.layout_price:  //  输入价格
                mBinding.editPrice.requestFocus();
                mBinding.editPrice.post(() -> KeyboardUtils.showSoftInput(mBinding.editPrice));
                break;
            case R.id.btn_post:  //  发布
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.getCategoryId())) {
                    ToastUtils.showShort("请选择美人筹类型");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.getCityId())) {
                    ToastUtils.showShort("请选择所在城市");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.getDays())) {
                    ToastUtils.showShort("请选择时间");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.getPeopleNum())) {
                    ToastUtils.showShort("请选择目标人数");
                    return;
                }
                if (TextUtils.isEmpty(mViewModel.price.get())) {
                    ToastUtils.showShort("请输入价格");
                    return;
                }
                showLoading("");
                mViewModel.planAdd();
                break;
        }
    }
}
