package com.chengzi.app.ui.mine.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityOrangeCreditBindingImpl;

/**
 * 橙子信用分
 *
 * @ClassName:OrangeCreditActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/1 0001   14:12
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/1 0001 handongkeji All rights reserved.
 */

public class OrangeCreditActivity extends BaseActivity<ActivityOrangeCreditBindingImpl, BaseViewModel> {
    //橙子信用分
    private String orange_create = "0";

    @Override
    public int getLayoutRes() {
        return R.layout.activity_orange_credit;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        orange_create = getIntent().getStringExtra("orange_create");
        //0-350     一般
        //351-550   普通
        //551-650   中等
        //651-750   优秀
        //751-850   极好
        //851-1000  最佳
        String text = "";
        int score = (int) Double.parseDouble(orange_create);
        if (score <= 350) {
            text = "一般";
        } else if (score >= 351 && score <= 550) {
            text = "普通";
        } else if (score >= 551 && score <= 650) {
            text = "中等";
        } else if (score >= 651 && score <= 750) {
            text = "优秀";
        } else if (score >= 751 && score <= 850) {
            text = "极好";
        } else if (score >= 851) {
            text = "普通";
        }
        mBinding.tvScore.setText(orange_create + "分\n" + text);
        //普通用户-->用户  爱好  履约  财力  关系(默认)
        //医生    -->用户  专业  履约  技术  关系
        //咨询师  -->用户  专业  履约  评分  关系
        //医院    -->用户  技术  履约  评分  关系
        if (AccountHelper.getIdentity() == Sys.TYPE_DOCTOR) {
            mBinding.tv2.setText("专业");
            mBinding.tv4.setText("技术");
        } else if (AccountHelper.getIdentity() == Sys.TYPE_COUNSELOR) {
            mBinding.tv2.setText("专业");
            mBinding.tv4.setText("评分");
        } else if (AccountHelper.getIdentity() == Sys.TYPE_HOSPITAL) {
            mBinding.tv2.setText("技术");
            mBinding.tv4.setText("评分");
        }
    }
}