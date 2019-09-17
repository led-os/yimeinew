package com.chengzi.app.ui.homepage.hospital.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityHospitalHomePageBinding;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;
import com.chengzi.app.ui.homepage.hospital.fragment.HospitalHomePageFragment;

/**
 * 医院主页
 *
 * @ClassName:HospitalHomePageActivity
 * @PackageName:com.yimei.app.ui.homepage.hospital.activity
 * @Create On 2019/4/19 0019   10:13
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class HospitalHomePageActivity extends BaseActivity<ActivityHospitalHomePageBinding,
        HospitalHomePageViewModel> {

    private static final String EXTRA_SELF = "extra_self";
    private static final String EXTRA_TARGET_USER_ID = "extra_target_user_id";
    private static final String EXTRA_PROMOTION_ID = "extra_promotion_id";

    public static void start(Context context, String targetId) {
        context.startActivity(new Intent(context, HospitalHomePageActivity.class)
                .putExtra(EXTRA_TARGET_USER_ID, targetId)
                .putExtra(EXTRA_SELF, TextUtils.equals(targetId, AccountHelper.getUserId()))
        );
    }

    public static void start(Context context, String targetId, String promotionId) {
        context.startActivity(new Intent(context, HospitalHomePageActivity.class)
                .putExtra(EXTRA_TARGET_USER_ID, targetId)
                .putExtra(EXTRA_PROMOTION_ID, promotionId)
                .putExtra(EXTRA_SELF, TextUtils.equals(targetId, AccountHelper.getUserId()))
        );
    }

    public static void startSelf(Context context) {
        Intent intent = new Intent(context, HospitalHomePageActivity.class);
        intent.putExtra(EXTRA_TARGET_USER_ID, AccountHelper.getUserId())
                .putExtra(EXTRA_SELF, true);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_hospital_home_page;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.homepage_container, HospitalHomePageFragment.newInstance(),
                        HospitalHomePageFragment.class.getSimpleName())
                .commit();

    }

    private void parseIntent() {
        String hospitalId = getIntent().getStringExtra(EXTRA_TARGET_USER_ID);
        boolean isSelf = getIntent().getBooleanExtra(EXTRA_SELF, false);
        mViewModel.setHospitalId(hospitalId);
        mViewModel.setSelf(isSelf);

        String promotionId = getIntent().getStringExtra(EXTRA_PROMOTION_ID);
        mViewModel.setPromotionId(promotionId);

    }

}
