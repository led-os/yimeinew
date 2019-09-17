package com.chengzi.app.ui.mine.activity.myorder;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityHospitalEvaluationBindingImpl;
import com.chengzi.app.ui.mine.viewmodel.MyOrderListViewModel;
import com.chengzi.app.utils.OrderStatusHelp;

/**
 * 医院身份 订单管理中 的评价(医生评价)
 *
 * @ClassName:HospitalEvaluationActivity
 * @PackageName:com.yimei.app.ui.mine.activity.myorder
 * @Create On 2019/4/18 0018   13:48
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */

public class HospitalEvaluationActivity extends BaseActivity<ActivityHospitalEvaluationBindingImpl, MyOrderListViewModel> {

    //订单id
    private String order_id;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_evaluation;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(complaintsClickListener);
        order_id = getIntent().getStringExtra("order_id");
        if (TextUtils.isEmpty(order_id)) {
            toast("订单不存在或非待评价状态!");
            finish();
        }
        mViewModel.orderEvaluationLive.observe(this, bean -> {
            dismissLoading();
            if (bean.getStatus() == Sys.SUCCESS_STATUS) {
                toast("评论成功！");
                //确认使用成功  //刷新-->医院状态下的 全部/(待使用)/待评价/已完成
                OrderStatusHelp.refreshOrderList(0);
                OrderStatusHelp.refreshOrderList(2);
                OrderStatusHelp.refreshOrderList(3);
                finish();
            }
        });
    }

    private ClickEventHandler<Object> complaintsClickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_submit:  //  提交
                //满意度
                int doctor_skill = mBinding.rbDoctor1.getCurCount();
                //用户评价的内容  content
                String content = mBinding.etNumber.getText().toString().trim();
                if (doctor_skill == 0) {
                    toast("请选择用户满意分!");
                    break;
                }
                showLoading(Sys.LOADING);
                mViewModel.orderEvaluation(order_id, String.valueOf(doctor_skill), content);
                break;
        }
    };
}
