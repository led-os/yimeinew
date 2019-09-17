package com.chengzi.app;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.handong.framework.base.BaseActivity;
import com.handong.framework.base.BaseViewModel;
import com.chengzi.app.databinding.ActivityGuidanceBinding;
import com.chengzi.app.databinding.GuidanceItemLayoutBinding;


public class GuidanceActivity extends BaseActivity<ActivityGuidanceBinding, BaseViewModel> {

    public final ObservableInt selectedPos = new ObservableInt();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_guidance;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding.setSelectedPos(selectedPos);
        LayoutInflater layoutInflater = getLayoutInflater();
        mBinding.viewPager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                GuidanceItemLayoutBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.guidance_item_layout, container, false);
                binding.tvName.setVisibility(View.GONE);
                binding.image.setScaleType(ImageView.ScaleType.FIT_XY);
                int drawable = getResources().getIdentifier("guidance_" + (position + 1), "drawable", getPackageName());
                binding.image.setImageResource(drawable);
                container.addView(binding.getRoot());
                return binding.getRoot();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    mBinding.tvExperience.setVisibility(View.VISIBLE);
                    mBinding.pointLayout.setVisibility(View.GONE);
                } else {
                    mBinding.tvExperience.setVisibility(View.GONE);
                    mBinding.pointLayout.setVisibility(View.VISIBLE);
                }
                selectedPos.set(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.tvSkip.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        mBinding.tvExperience.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


}
