package com.chengzi.app.ui.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.handong.framework.base.BaseFragment;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentMainDynamicBinding;

import java.util.List;

/**
 * 首页动态
 *
 * @ClassName:MainDynamicFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/4/2 0002   19:04
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */
public class MainDynamicFragment extends BaseFragment<FragmentMainDynamicBinding, BaseViewModel>
        implements View.OnClickListener {

    public static MainDynamicFragment newInstance() {
        Bundle args = new Bundle();
        MainDynamicFragment fragment = new MainDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainDynamicFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main_dynamic;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setListener(this);

        binding.viewPagerDynamic.setAdapter(new MainDynamicPagerAdapter(getChildFragmentManager()));
        binding.viewPagerDynamic.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.btnTradeDynamic.setSelected(true);
                    binding.btnAcademicDynamic.setSelected(false);
                } else {
                    binding.btnTradeDynamic.setSelected(false);
                    binding.btnAcademicDynamic.setSelected(true);
                }
            }
        });
        binding.viewPagerDynamic.setCurrentItem(0);
        binding.btnTradeDynamic.setSelected(true);
        binding.btnAcademicDynamic.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_trade_dynamic:
                binding.viewPagerDynamic.setCurrentItem(0);
                break;
            case R.id.btn_academic_dynamic:
                binding.viewPagerDynamic.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onHiddenChanged(hidden);
            }
        }
    }

    private class MainDynamicPagerAdapter extends FragmentPagerAdapter {

        public MainDynamicPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return TradingDynamicFragment.newInstance();
                case 1:
                    return AcademicDynamicFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
