package com.chengzi.app.ui.peopleraise.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityConfirmRaiseOrderBinding;
import com.chengzi.app.ui.pay.activity.PayActivity;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.viewmodel.ConfirmRaiseOrderViewModel;

/**
 * 美人筹确认订单
 *
 * @ClassName:ConfirmRaiseOrderActivity
 * @PackageName:com.yimei.app.ui.peopleraise.activity
 * @Create On 2019/5/14 0014   15:12
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/5/14 0014 handongkeji All rights reserved.
 */
public class ConfirmRaiseOrderActivity extends BaseActivity<ActivityConfirmRaiseOrderBinding,
        ConfirmRaiseOrderViewModel> implements View.OnClickListener {

    private static final String EXTRA_PLAN_ID = "extra_plan_id";

    public static void start(Context context, String planId) {
        Intent intent = new Intent(context, ConfirmRaiseOrderActivity.class);
        intent.putExtra(EXTRA_PLAN_ID, planId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_confirm_raise_order;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(this);

        Intent intent = getIntent();
        String planId = intent.getStringExtra(EXTRA_PLAN_ID);
        mViewModel.setPlanId(planId);

        observe();

        mViewModel.planOrder();

    }

    private void observe(){
        mViewModel.raiseLive.observe(this,raiseBean -> {
            bindData(raiseBean);
        });

        mViewModel.submitOrderLive.observe(this,submitOrderBean -> {
            dismissLoading();
            String id = submitOrderBean.getId();
            PayActivity.start(this,id,2);
            finish();
        });
    }

    private void bindData(RaiseBean raiseBean){
        mBinding.tvCategoryName.setText(raiseBean.getCategory_name());

        String price = raiseBean.getPrice();
        price = TextUtils.isEmpty(price)?"0":price;

        SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
        AbsoluteSizeSpan as = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
        spanBuilder.append(getString(R.string.rmb_symbol), as, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spanBuilder.append(price);

        mBinding.tvPrice.setText(spanBuilder);

        int peopleNum = raiseBean.getPeople_number();
        mBinding.tvPeopleNum.setText(peopleNum + getString(R.string.order_person));

        SpannableStringBuilder amountSpanBuilder = new SpannableStringBuilder();
        amountSpanBuilder.append(getString(R.string.actully_pay_amount));

        String actuallyAmount = getString(R.string.price_place_holder_with_unit, price);
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        amountSpanBuilder.append(actuallyAmount, fcs, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mBinding.tvPayPrice.setText(amountSpanBuilder);
    }

    @Override
    public void onClick(View v) {

        if (!ClickEvent.shouldClick(v)) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_confirm:  //  确认订单
                if (AccountHelper.shouldLogin(this)) {
                    return;
                }
                showLoading("");
                mViewModel.planSubmitOrder();
                break;
        }
    }
}
