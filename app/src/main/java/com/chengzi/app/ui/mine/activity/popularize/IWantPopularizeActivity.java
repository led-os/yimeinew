package com.chengzi.app.ui.mine.activity.popularize;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityIwantPopularizeBindingImpl;
import com.chengzi.app.ui.mine.activity.vip.LookVipIntroduceActivity;
import com.chengzi.app.ui.mine.bean.PromotionInfoBean;
import com.chengzi.app.ui.mine.viewmodel.IWantPopularizeViewModel;

/**
 * 个人中心 医院 我要推广
 *
 * @ClassName:IWantPopularizeActivity
 * @PackageName:com.yimei.app.ui.mine.activity.popularize
 * @Create On 2019/4/12 0012   17:11
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */
public class IWantPopularizeActivity extends BaseActivity<ActivityIwantPopularizeBindingImpl, IWantPopularizeViewModel> {

    public static double promotion_price = 0.00;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_iwant_popularize;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        //推广规则 1推广规则
        mBinding.topBar.setOnRightClickListener(v -> {
//            UserAgreementActivity.strat(this, 1)
            LookVipIntroduceActivity.start(this, "12");
        });

        //我要推广->个人信息
        showLoading(Sys.LOADING);
        mViewModel.promotionInfoLiveData.observe(this, promotionInfoBean -> {
            dismissLoading();
            if (promotionInfoBean != null && promotionInfoBean.getData() != null) {
                PromotionInfoBean data = promotionInfoBean.getData();
                //推广金额(一次扣费的金额)-->以下的所有的设置推广限额必须大于此推广金额
                promotion_price = !TextUtils.isEmpty(data.getPromotion_price()) ? Double.parseDouble(data.getPromotion_price()) : 0.00;
                mBinding.setBean(data);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.promotionInfo();
    }

    //推广限额:如果不为空时 判断是不是小于推广金额
    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_recharge:  //充值
                goActivity(RechargeActivity.class);
                break;
            case R.id.tv_zone_promote:  //  专区推广
                goActivity(ZonePromoteActivity.class);
                break;
            case R.id.tv_adv_promote:  // Banner广告位推广
                goActivity(AdvPromoteActivity.class);
                break;
            case R.id.tv_goods_search_promotion:  //  商品站内搜索推广
                goActivity(GoodsSearchPromtionActivity.class);
                break;
            case R.id.tv_doctor_search_promotion:  // 医生站内搜索推广
                DoctorOrCounselorPromotionActivity.start(this, 2);
                break;
            case R.id.tv_counselor_search_promotion:  //  咨询师站内搜索推广
                DoctorOrCounselorPromotionActivity.start(this, 3);
                break;
            case R.id.tv_case_search_promotion:  // 案例站内搜索推广
                goActivity(CaseSearchPromotionActivity.class);
                break;
            case R.id.tv_hospital_search_promotion:  // 医院站内搜索推广
                goActivity(HospitalSearchPromotionActivity.class);
                break;
        }
    };
}