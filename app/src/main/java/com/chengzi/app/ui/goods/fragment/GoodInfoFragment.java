package com.chengzi.app.ui.goods.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.viewpager2.widget.ViewPager2;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentGoodInfoBinding;
import com.chengzi.app.databinding.ItemLoopPicLayoutBinding;
import com.chengzi.app.databinding.ItemLoopVideoLayoutBinding;
import com.chengzi.app.ui.goods.bean.BannerBean;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.utils.ProxyHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;

public class GoodInfoFragment extends RefreshableFragment<FragmentGoodInfoBinding, GoodsDetailViewModel> {

    private LoopPagerAdapter loopPagerAdapter;

    public GoodInfoFragment() {
    }

    public static GoodInfoFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(GoodInfoFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new GoodInfoFragment();
            android.os.Bundle args = new Bundle();
            fragment.setArguments(args);
        }
        return (GoodInfoFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_good_info;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.setLoopPos(viewModel.loopPos);
        initLoop();

        viewModel.goodDetailLive.observe(this, goodDetailBean -> {

            binding.setBean(goodDetailBean);
            bindGoodInfo(goodDetailBean);

        });
    }

    private void initLoop() {
        loopPagerAdapter = new LoopPagerAdapter();
        binding.viewPager2.setAdapter(loopPagerAdapter);
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                viewModel.loopPos.set((position + 1) + "/" + loopPagerAdapter.getData().size());
            }
        });
    }

    private void bindGoodInfo(GoodDetailBean bean) {

        List<BannerBean> imagesArr = bean.getImages_arr();

        if (imagesArr == null || imagesArr.isEmpty()) {
            binding.loopLayout.setVisibility(View.GONE);
        } else {
            binding.loopLayout.setVisibility(View.VISIBLE);
            Collections.sort(imagesArr, (o1, o2) -> o2.getType() - o1.getType());
            loopPagerAdapter.setNewData(imagesArr);
        }

        viewModel.loopPos.set(1 + "/" + loopPagerAdapter.getData().size());

        binding.tvKillPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        if (bean.getIs_killing() == 1) {  //  秒杀商品

            long current_time = bean.getCurrent_time() * 1000;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(current_time);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

            int kill_time = bean.getKill_time();

            if (kill_time > hourOfDay) {  //  未开始

                binding.tvStartKillTime.setText(kill_time + ":00开始秒杀抢购");
                binding.llInKill.setVisibility(View.GONE);
                binding.tvStartKillTime.setVisibility(View.VISIBLE);

                binding.tvSpellPrice.setVisibility(View.VISIBLE);
                binding.tvKillPrice.setVisibility(View.GONE);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
                spannableStringBuilder.append(getString(R.string.rmb_symbol),
                        absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(bean.getBuy_price());

                binding.tvBuyPrice.setText(spannableStringBuilder);

            } else if ((kill_time + 2) <= hourOfDay) {  //  已结束

                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, kill_time);
                calendar.set(Calendar.MINUTE, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                String format = sdf.format(new Date(calendar.getTimeInMillis()));

                binding.tvStartKillTime.setText(format + "开始秒杀抢购");
                binding.tvStartKillTime.setVisibility(View.VISIBLE);
                binding.llInKill.setVisibility(View.GONE);

                binding.tvSpellPrice.setVisibility(View.VISIBLE);
                binding.tvKillPrice.setVisibility(View.GONE);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
                spannableStringBuilder.append(getString(R.string.rmb_symbol),
                        absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(bean.getBuy_price());

                binding.tvBuyPrice.setText(spannableStringBuilder);

            } else {      //  秒杀中

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
                spannableStringBuilder.append(getString(R.string.rmb_symbol),
                        absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(bean.getKill_price());

                binding.tvBuyPrice.setText(spannableStringBuilder);
                binding.tvKillPrice.setText(getString(R.string.price_place_holder_with_unit, bean.getBuy_price()));

                binding.tvSpellPrice.setVisibility(View.GONE);
                binding.tvKillPrice.setVisibility(View.VISIBLE);

                binding.tvStartKillTime.setVisibility(View.GONE);
                binding.llInKill.setVisibility(View.VISIBLE);
                binding.llInKill.setRemainTime(bean.getKill_remain_time());

            }

        } else {

            binding.tvSpellPrice.setVisibility(View.VISIBLE);
            binding.tvKillPrice.setVisibility(View.GONE);
            binding.tvStartKillTime.setVisibility(View.GONE);
            binding.llInKill.setVisibility(View.GONE);

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(SizeUtils.sp2px(11));
            spannableStringBuilder.append(getString(R.string.rmb_symbol),
                    absoluteSizeSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(bean.getBuy_price());

            binding.tvBuyPrice.setText(spannableStringBuilder);

        }
    }

    @Override
    public void onRefresh() {

    }

    private static class LoopPagerAdapter extends BaseMultiItemQuickAdapter<BannerBean, BaseViewHolder> {

        public LoopPagerAdapter() {
            super(new ArrayList<>());
            addItemType(0, R.layout.item_loop_pic_layout);
            addItemType(1, R.layout.item_loop_video_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, BannerBean item) {
            int itemViewType = getItemViewType(helper.getAdapterPosition());
            if (itemViewType == 0) {     //  图片
                ItemLoopPicLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
                layoutBinding.setBean(item);
                layoutBinding.executePendingBindings();
            } else {        //  视频
                ItemLoopVideoLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
                if (!TextUtils.isEmpty(item.getUrl())) {
                    String newVideoUrl = ProxyHelper.getProxy().getProxyUrl(item.getUrl(), false);
                    layoutBinding.videoplayer.setUp(newVideoUrl, "", Jzvd.SCREEN_WINDOW_NORMAL);
                }else{
                    String tempUrl = null;
                    layoutBinding.videoplayer.setUp(new JZDataSource(tempUrl),Jzvd.SCREEN_WINDOW_NORMAL);
                }
                layoutBinding.videoplayer.seekToInAdvance = 0;
                ImageLoader.loadImage(layoutBinding.videoplayer.thumbImageView, item.getUrl(), 0, R.color.gray);
            }
        }

        @Override
        public void onViewRecycled(@NonNull BaseViewHolder holder) {
            super.onViewRecycled(holder);
            if (holder.getItemViewType() == 1) {
                Jzvd.releaseAllVideos();
            }
        }
    }

}
