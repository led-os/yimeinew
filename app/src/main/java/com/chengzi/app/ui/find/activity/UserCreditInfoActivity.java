package com.chengzi.app.ui.find.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityUserCreditInfoBindingImpl;
import com.chengzi.app.ui.find.bean.CreditSearchDetailBean;
import com.chengzi.app.ui.find.viewmodel.CreditEnqiryViewModel;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.utils.CardUtils;

/**
 * 用户信用信息
 *
 * @ClassName:UserCreditInfoActivity
 * @PackageName:com.yimei.app.ui.find.activity
 * @Create On 2019/4/19 0019   16:54
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class UserCreditInfoActivity extends BaseActivity<ActivityUserCreditInfoBindingImpl, CreditEnqiryViewModel> {

    private int types;

    //types -->等于1 普通用户 时 才显示 查看主页
    public static void start(Context context, String search_id, int types) {
        context.startActivity(new Intent(context, UserCreditInfoActivity.class)
                .putExtra(Sys.EXTRA, search_id)
                .putExtra("types", types)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_user_credit_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);
        CardUtils.setCardShadowColor(mBinding.cdView, Color.parseColor("#F29C9F"), Color.parseColor("#fefbfb"));
        String search_id = getIntent().getStringExtra(Sys.EXTRA);
        //普通用户 才显示 查看主页
        types = getIntent().getIntExtra("types", 0);
        mBinding.tvLookHomePage.setVisibility(types == 1 ? View.VISIBLE : View.GONE);

        showLoading(Sys.LOADING);
        mViewModel.creditSearchDetail(search_id);
        mViewModel.creditSearchDetailLiveData.observe(this, searchDetailBean -> {
            dismissLoading();
            if (searchDetailBean != null && searchDetailBean.getData() != null) {
                CreditSearchDetailBean data = searchDetailBean.getData();
                mBinding.setBean(data);
                mBinding.setUrl(data.getImage());
           /*     //tv_sex设置性别
                String gender = data.getGender();
                mBinding.tvSex.setBackgroundResource(gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
                Drawable drawable_n = getResources().getDrawable(gender.equals("1") ? R.drawable.nan : R.drawable.nv);
                drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
                mBinding.tvSex.setCompoundDrawables(drawable_n, null, null, null);*/
            }
        });
    }

    private ClickEventHandler<CreditSearchDetailBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_look_home_page:  //  查看用户主页

                int type = bean.getTypes();
                String user_id = bean.getId();
                if (user_id.equals(AccountHelper.getUserId()) && type == AccountHelper.getIdentity()) { //自己的主页
                    if (type == Sys.TYPE_USER) {
                        UserHomePageActivity.startSelf(this);
//                    } else if (type == Sys.TYPE_DOCTOR) {
//                        DoctorHomePageActivity.startDoctorSelt(this);
//                    } else if (type == Sys.TYPE_COUNSELOR) {
//                        DoctorHomePageActivity.startCounselorSelt(this);
//                    } else if (type == Sys.TYPE_HOSPITAL) {
//                        HospitalHomePageActivity.startSelf(this);
                    }
                } else {        //别人的主页
                    if (type == Sys.TYPE_USER) {
                        UserHomePageActivity.start(this, user_id);
//                    } else if (type == Sys.TYPE_DOCTOR) {
//                        DoctorHomePageActivity.startDoctor(this, user_id);
//                    } else if (type == Sys.TYPE_COUNSELOR) {
//                        DoctorHomePageActivity.startCounselor(this, user_id);
//                    } else if (type == Sys.TYPE_HOSPITAL) {
//                        HospitalHomePageActivity.start(this, user_id);
                    }
                }
                break;
        }
    };
}