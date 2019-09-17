package com.chengzi.app.ui.mine.activity.popularize;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHospitalSearchPromotionBinding;
import com.chengzi.app.dialog.SetLimitDialog;
import com.chengzi.app.ui.mine.bean.HospitalPromotionBean;
import com.chengzi.app.ui.mine.viewmodel.IWantPopularizeViewModel;

/**
 * 医院推广
 *
 * @ClassName:HospitalSearchPromotionActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/15 0015   13:47
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */

public class HospitalSearchPromotionActivity extends BaseActivity<ActivityHospitalSearchPromotionBinding, IWantPopularizeViewModel> {

    //设置的限额
    private String money;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_search_promotion;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        observe();
    }

    private void observe() {
        //查看医院是否推广
        showLoading(Sys.LOADING);
        mViewModel.hospitalPromotion();
        mViewModel.hospitalPromotionLiveData.observe(this, hospitalPromotionBean -> {
            dismissLoading();
            if (hospitalPromotionBean != null && hospitalPromotionBean.getData() != null) {
                HospitalPromotionBean data = hospitalPromotionBean.getData();
                mBinding.setBean(data);
                mBinding.setUrl(data.getImage());
                boolean is_extension = data.isIs_extension();
                mBinding.ivPromote.setSelected(is_extension);
            }
        });

        //设置限额
        mViewModel.setSearchPromotionAmountLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mBinding.tvPromoteMoney.setText(money);
            }
        });

        //开启推广
        mViewModel.addSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mBinding.ivPromote.setSelected(true);
            }
        });
        //关闭推广
        mViewModel.removeSearchPromotionItemLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                mBinding.ivPromote.setSelected(false);
            }
        });
    }

    private ClickEventHandler<HospitalPromotionBean> clickListener = (view, bean) -> {
        boolean iv_promote = mBinding.ivPromote.isSelected();
        switch (view.getId()) {
            case R.id.iv_promote:  //推广
                showLoading(Sys.LOADING);
                if (!iv_promote) {  //开启
                    mViewModel.addSearchPromotionItem(bean.getId(), "4");
                } else {
                    mViewModel.removeSearchPromotionItem(bean.getId(), "4");
                }
                break;
            case R.id.iv_limit:  //设置限额
//                if (iv_promote) {
                new SetLimitDialog(this, 2)
                        .setSureListener(text -> {
                            money = text; ////推广限额:如果不为空时 判断是不是小于推广金额
                            if (!TextUtils.isEmpty(money)) {
                                double v = Double.parseDouble(money);
                                if (v < IWantPopularizeActivity.promotion_price) {
                                    toast(getResources().getString(R.string.want_popularize));
                                    return;
                                }
                            }
                            showLoading(Sys.LOADING);
                            mViewModel.setSearchPromotionAmount(bean.getId(), "4", text);
                        })
                        .show();
//                } else {
//                    toast("请先开启推广!");
//                }
                break;
        }
    };
}