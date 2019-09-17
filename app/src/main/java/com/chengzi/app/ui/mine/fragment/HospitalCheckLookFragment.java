package com.chengzi.app.ui.mine.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.handong.framework.base.BaseViewModel;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.adapters.LazyFPagerAdapter;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentHospitalCheckLookBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HospitalCheckLookFragment extends LazyloadFragment<FragmentHospitalCheckLookBinding, BaseViewModel> {
    //0下单 1评价 2投诉 3对话量
    private int position = 0;

    public static HospitalCheckLookFragment newInstance(int position) {
        HospitalCheckLookFragment fragment = new HospitalCheckLookFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_check_look;
    }

    @Override
    public void onLazyload() {
        List<android.support.v4.app.Fragment> fragments = new ArrayList<>();
        if (position == 0) {         //下单
            binding.rgButton.setVisibility(View.GONE);
            fragments.add(DoctorCheckLookFragment.newInstance("2"));
//            fragments.add(DoctorCheckLookFragment.newInstance("3"));
        } else if (position == 1) {   //评价
            binding.rgButton.setVisibility(View.GONE);
            fragments.add(HospitalLookEvaluateFragment.newInstance("2"));
//            fragments.add(HospitalLookEvaluateFragment.newInstance("3"));
        } else if (position == 2) {   //投诉
            binding.rgButton.setVisibility(View.VISIBLE);
            fragments.add(HospitalLookComplaintFragment.newInstance("2"));
            fragments.add(HospitalLookComplaintFragment.newInstance("3"));
        } else if (position == 3) {  //对话量
            binding.rgButton.setVisibility(View.VISIBLE);
            fragments.add(HospitalLookDialogueFragment.newInstance("2"));
            fragments.add(HospitalLookDialogueFragment.newInstance("3"));
        }
        LazyFPagerAdapter adapter = new LazyFPagerAdapter(getChildFragmentManager(), fragments);
        binding.viewPager.setAdapter(adapter);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            position = arguments.getInt("position", 0);
        }
        binding.setListener(clickListener);

        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    binding.rbDoctor.setChecked(true);
                } else {
                    binding.rbCounselor.setChecked(true);
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
                binding.viewPager.setCurrentItem(0, false);
                break;
            case R.id.rb_counselor:  // 咨询师
                binding.viewPager.setCurrentItem(1, false);
                break;
        }
    };
}