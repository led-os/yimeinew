package com.chengzi.app.ui.homepage.doctor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityDoctorHomePageBinding;
import com.chengzi.app.ui.homepage.doctor.fragment.DoctorHomepageFragment;
import com.chengzi.app.ui.homepage.doctor.fragment.ProfessionalExperienceFragment;
import com.chengzi.app.ui.homepage.doctor.viewmodel.DoctorHomePageViewModel;

/**
 * 医生主页
 *
 * @ClassName:DoctorHomePageActivity
 * @PackageName:com.yimei.app.ui.doctorhomepage.activity
 * @Create On 2019/4/17 0017   09:57
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/17 0017 handongkeji All rights reserved.
 */
public class DoctorHomePageActivity extends BaseActivity<ActivityDoctorHomePageBinding,
        DoctorHomePageViewModel> implements View.OnClickListener {

    private static final String EXTRA_SELF = "extra_self";
    private static final String EXTRA_USER_TYPE = "extra_user_type";
    private static final String EXTRA_PROMOTION_ID = "extra_promotion_id";

    public static void startDoctor(Context context, String doctorId) {
        Intent intent = new Intent(context, DoctorHomePageActivity.class);
        intent.putExtra(Sys.EXTRA, doctorId)
                .putExtra(EXTRA_SELF, TextUtils.equals(doctorId, AccountHelper.getUserId()))
                .putExtra(EXTRA_USER_TYPE, 2);
        context.startActivity(intent);
    }

    public static void startDoctor(Context context, String doctorId, String promotionId) {
        Intent intent = new Intent(context, DoctorHomePageActivity.class);
        intent.putExtra(Sys.EXTRA, doctorId)
                .putExtra(EXTRA_SELF, TextUtils.equals(doctorId, AccountHelper.getUserId()))
                .putExtra(EXTRA_PROMOTION_ID, promotionId)
                .putExtra(EXTRA_USER_TYPE, 2);
        context.startActivity(intent);
    }

    public static void startDoctorSelt(Context context) {
        Intent intent = new Intent(context, DoctorHomePageActivity.class);
        intent.putExtra(EXTRA_SELF, true)
                .putExtra(EXTRA_USER_TYPE, 2);
        context.startActivity(intent);
    }

    public static void startCounselor(Context context, String doctorId) {
        Intent intent = new Intent(context, DoctorHomePageActivity.class);
        intent.putExtra(Sys.EXTRA, doctorId)
                .putExtra(EXTRA_SELF, TextUtils.equals(doctorId, AccountHelper.getUserId()))
                .putExtra(EXTRA_USER_TYPE, 3);
        context.startActivity(intent);
    }

    public static void startCounselor(Context context, String doctorId, String promotionId) {
        Intent intent = new Intent(context, DoctorHomePageActivity.class);
        intent.putExtra(Sys.EXTRA, doctorId)
                .putExtra(EXTRA_SELF, TextUtils.equals(doctorId, AccountHelper.getUserId()))
                .putExtra(EXTRA_PROMOTION_ID, promotionId)
                .putExtra(EXTRA_USER_TYPE, 3);
        context.startActivity(intent);
    }

    public static void startCounselorSelt(Context context) {
        Intent intent = new Intent(context, DoctorHomePageActivity.class);
        intent.putExtra(EXTRA_SELF, true)
                .putExtra(EXTRA_USER_TYPE, 3);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_home_page;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setListener(this);

        mBinding.viewPager.setAdapter(new DoctorHomepageAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);


    }

    private void parseIntent() {
        boolean self = getIntent().getBooleanExtra(EXTRA_SELF, false);
        mViewModel.setSelf(self);

        int userType = getIntent().getIntExtra(EXTRA_USER_TYPE, 2);
        mViewModel.setUserType(userType);

        if (self) {
            mViewModel.setDoctorId(AccountHelper.getUserId());
            title = new String[]{"我的主页", "我的履历"};
        } else {
            String doctorId = getIntent().getStringExtra(Sys.EXTRA);
            mViewModel.setDoctorId(doctorId);
            if (userType == 2) {
                title = new String[]{"医生主页", "Ta的履历"};
            } else {
                title = new String[]{"咨询师主页", "Ta的履历"};
            }
        }

        String promotionId = getIntent().getStringExtra(EXTRA_PROMOTION_ID);
        mViewModel.setPromotionId(promotionId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    private String[] title = {"医生主页", "Ta的履历"};

    private class DoctorHomepageAdapter extends FragmentPagerAdapter {


        public DoctorHomepageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return DoctorHomepageFragment.newInstance();
                case 1:
                    return ProfessionalExperienceFragment.newInstance(mViewModel.isSelf(), mViewModel.getDoctorId(), mViewModel.getUserType());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
