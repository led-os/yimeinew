package com.chengzi.app.ui.mine.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handong.framework.base.BaseActivity;
import com.handongkeji.utils.FormatUtil;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityDataNalysisBindingImpl;
import com.chengzi.app.ui.mine.bean.DataAnalysisBean;
import com.chengzi.app.ui.mine.viewmodel.DataAnlysisViewModel;

/**
 * 数据分析-->医院
 *
 * @ClassName:DataNalysisActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/11 0011   09:16
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class DataNalysisActivity extends BaseActivity<ActivityDataNalysisBindingImpl, DataAnlysisViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_data_nalysis;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //数据分析
        showLoading(Sys.LOADING);
        mViewModel.dataAnalysis();
        mViewModel.responseBeanLiveData.observe(this, dataAnalysisBean -> {
            dismissLoading();
            if (dataAnalysisBean != null && dataAnalysisBean.getData() != null) {
                DataAnalysisBean data = dataAnalysisBean.getData();
                //皮肤/手术订单比
                String no_operation_order_count = data.getNo_operation_order_count();
                String operation_order_count = data.getOperation_order_count();
                mBinding.etSkinSurgery.setContent(no_operation_order_count + "/" + operation_order_count);
                //皮肤订单占比  手术订单占比
                try {
                    double pifu = Double.parseDouble(no_operation_order_count);
                    double shoushu = Double.parseDouble(operation_order_count);
                    double all = pifu + shoushu;
                    if (all > 0) {  //    java.lang.ArithmeticException: divide by zero 被除数不能为0
                        double pifu1 = pifu / all;
                        double shoushu1 = shoushu / all;//皮肤订单占比et_skin
                        mBinding.etSkin.setContent(FormatUtil.format2Decimal(pifu1 * 100) + "%");
                        mBinding.etSurgery.setContent(FormatUtil.format2Decimal(shoushu1 * 100) + "%");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //今日/本月成交总数
//                mBinding.etTodayTotalOrderCount.setContent(data.getToday_total_order_count());
//                mBinding.etMonthTotalOrderCount.setContent(data.getMonth_total_order_count());
                mBinding.etTodayTotalOrderCount.setContent(data.getToday_nopro_order_count());
                mBinding.etMonthTotalOrderCount.setContent(data.getMonth_nopro_order_count());
                //今日/本月推广花费
                mBinding.etTodayAdvertPrice.setContent("¥" + data.getToday_advert_price());
                mBinding.etMonthAdvertPrice.setContent("¥" + data.getMonth_advert_price());
                //今日/本月推广成交数(日/月成单)
                mBinding.etTodayProOrderCount.setContent(data.getToday_pro_order_count() + "单");
                mBinding.etMonthProOrderCount.setContent(data.getMonth_pro_order_count() + "单");
                //今日访客  今日商品访客
                mBinding.etTodayVisitCount.setContent(data.getToday_visit_count() + "人");
                mBinding.etTodayGoodsVisitCount.setContent(data.getToday_goods_visit_count() + "人");
            }
        });
    }
}