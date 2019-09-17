package com.chengzi.app.ui.find.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityFindDetailBinding;
import com.chengzi.app.ui.find.fragment.FindCaseFragment;
import com.chengzi.app.ui.find.fragment.FindDoctorFragment;
import com.chengzi.app.ui.find.fragment.FindGoodsFragment;
import com.chengzi.app.ui.find.fragment.FindOrgFragment;
import com.chengzi.app.ui.onlinequestionandanswer.activity.AskQuestionActivity;
import com.chengzi.app.ui.privaterefer.activity.PrivateReferActivity;
import com.chengzi.app.ui.rankinglist.activity.RankingListActivity;
import com.chengzi.app.ui.search.activity.SearchResultActivity;

/**
 * 找 医生/商品/机构/咨询师/案例
 *
 * @ClassName:LookforDetailActivity
 * @PackageName:com.yimei.app.ui.lookfor.activity
 * @Create On 2019/4/10 0010   15:52
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class FindDetailActivity extends BaseActivity<ActivityFindDetailBinding,
        BaseViewModel> implements View.OnClickListener {

    public static final int FIND_DOCTOR = 0x01;
    public static final int FIND_GOODS = 0x02;
    public static final int FIND_ORG = 0x03;
    public static final int FIND_COUNSOLER = 0x04;
    public static final int FIND_CASE = 0x05;
    private int extra;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_find_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        mBinding.setListener(this);

        extra = getIntent().getIntExtra("extra", FIND_DOCTOR);

        String title = "";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment");
        if (fragment == null) {
            if (extra == FIND_DOCTOR) {
                fragment = FindDoctorFragment.newInstance(0);
                title = getString(R.string.lookfor_doctor);
            } else if (extra == FIND_GOODS) {
                fragment = FindGoodsFragment.newInstance();
                title = getString(R.string.lookfor_good);
            } else if (extra == FIND_ORG) {
                fragment = FindOrgFragment.newInstance();
                title = getString(R.string.lookfor_org);
            } else if (extra == FIND_COUNSOLER) {
                fragment = FindDoctorFragment.newInstance(1);
                title = getString(R.string.lookfor_counsoler);
            } else if (extra == FIND_CASE) {
                fragment = FindCaseFragment.newInstance();
                title = getString(R.string.lookfor_case);
            }
        }
        mBinding.topBar.setCenterText(title);
        if (fragment == null) {
            finish();
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, "fragment")
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search: //  搜索->  0-商品 1-机构 2-医生 3-案例 4-咨询师
                int tab_select_position = 0;
                if (extra == FIND_GOODS) {
                    tab_select_position = 0;
                } else if (extra == FIND_ORG) {
                    tab_select_position = 1;
                } else if (extra == FIND_DOCTOR) {
                    tab_select_position = 2;
                } else if (extra == FIND_CASE) {
                    tab_select_position = 3;
                } else if (extra == FIND_COUNSOLER) {
                    tab_select_position = 4;
                }
                SearchResultActivity.start(this, tab_select_position);
                break;
            case R.id.btn_ranking_list:  //  排行榜
                int extra = getIntent().getIntExtra("extra", FIND_DOCTOR);
                RankingListActivity.start(this, extra);
                break;
            case R.id.btn_ask:   //  我要问
                startActivity(new Intent(this, AskQuestionActivity.class));
                break;
            case R.id.btn_refer:  //  私享咨询
                PrivateReferActivity.start(this);
                break;
        }
    }
}
