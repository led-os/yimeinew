package com.chengzi.app.ui.mine.activity;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.adapters.LazyFPagerAdapter;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityCompetitiveAnalysisBindingImpl;
import com.chengzi.app.ui.mine.fragment.CompetitiveAnalysisFragment;
import com.chengzi.app.ui.mine.viewmodel.CompetitiveAnalysisViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 竞对分析
 *
 * @ClassName:CompetitiveAnalysisActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/12 0012   09:48
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/12 0012 handongkeji All rights reserved.
 */

public class CompetitiveAnalysisActivity extends BaseActivity<ActivityCompetitiveAnalysisBindingImpl, CompetitiveAnalysisViewModel> {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_competitive_analysis;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mBinding.setListener(clickListener);

        //获取头部信息
        mViewModel.user_type.set("2");
        mViewModel.onRequest(1, 1);
        mViewModel.competitiveAnalysisLiveData.observe(this, competitiveAnalysisBean -> {
            dismissLoading();
            if (competitiveAnalysisBean != null) {
                String hospitalName = competitiveAnalysisBean.getHospitalName().getName();
                mBinding.tvHospitalName.setText(hospitalName);
                String city_name = competitiveAnalysisBean.getCity().getCity_name();
                mBinding.tvCity.setText(city_name);
                String cityRankings = competitiveAnalysisBean.getCityRankings();
                mBinding.tvCityRank.setText(cityRankings);
            }
        });

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(CompetitiveAnalysisFragment.newInstance("2"));
        fragments.add(CompetitiveAnalysisFragment.newInstance("3"));
        LazyFPagerAdapter adapter = new LazyFPagerAdapter(getSupportFragmentManager(), fragments);
        mBinding.viewPager.setAdapter(adapter);

        mBinding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    mBinding.rbDoctor.setChecked(true);
                } else {
                    mBinding.rbCounselor.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private ClickEventHandler clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.rb_doctor:  //  医生
                mBinding.viewPager.setCurrentItem(0, false);
                break;
            case R.id.rb_counselor:  // 咨询师
                mBinding.viewPager.setCurrentItem(1, false);
                break;
        }
    };
}