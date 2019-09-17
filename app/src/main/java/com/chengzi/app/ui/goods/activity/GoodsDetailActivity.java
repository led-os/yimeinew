package com.chengzi.app.ui.goods.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;

import com.chengzi.app.BuildConfig;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityGoodsDetailBinding;
import com.chengzi.app.dialog.ShareDialog;
import com.chengzi.app.ui.goods.bean.BannerBean;
import com.chengzi.app.ui.goods.bean.GoodDetailBean;
import com.chengzi.app.ui.goods.fragment.DetailFragment;
import com.chengzi.app.ui.goods.fragment.EstimateFragment;
import com.chengzi.app.ui.goods.fragment.GoodsFragment;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;

import java.util.List;

import cn.jzvd.Jzvd;

/**
 * 商品详情
 *
 * @ClassName:GoodsDetailActivity
 * @PackageName:com.yimei.app.ui.goods.activity
 * @Create On 2019/4/15 0015   17:18
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/15 0015 handongkeji All rights reserved.
 */
public class GoodsDetailActivity extends BaseActivity<ActivityGoodsDetailBinding,
        GoodsDetailViewModel> implements View.OnClickListener {

    private static final String EXTRA_PROMOTION_ID = "extra_promotion_id";


    public static void start(Context context, String goodId) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(Sys.EXTRA, goodId);
        context.startActivity(intent);
    }

    //1-专区推广 首页专区和VIP专区 /2-搜索推广 找和搜索
    public static void start(Context context, String goodId, String promotionId, int type) {
        Intent intent = new Intent(context, GoodsDetailActivity.class);
        intent.putExtra(Sys.EXTRA, goodId)
                .putExtra(EXTRA_PROMOTION_ID, promotionId)
                .putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        mBinding.setListener(this);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.viewPager.setAdapter(new GoodsDetailPagerAdapter(getSupportFragmentManager()));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

    }

    private void parseIntent() {
        String goodId = getIntent().getStringExtra(Sys.EXTRA);
        mViewModel.setGoodId(goodId);

        String promotionId = getIntent().getStringExtra(EXTRA_PROMOTION_ID);
        mViewModel.setPromotionId(promotionId);
        //1-专区推广 /2-搜索推广
        int type = getIntent().getIntExtra("type", 0);
        if (type != 0)
            mViewModel.setType(String.valueOf(type));
    }

    public void setCurrentPageItem(int currentPageItem) {
        if (currentPageItem < 0 || currentPageItem > mBinding.viewPager.getAdapter().getCount()) {
            throw new IndexOutOfBoundsException();
        }
        mBinding.viewPager.setCurrentItem(currentPageItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.btn_share:
                GoodDetailBean detailBean = mViewModel.goodDetailLive.getValue();
                if (detailBean == null) {
                    return;
                }
                ShareDialog shareDialog = new ShareDialog();
                shareDialog.show(getSupportFragmentManager(), "ShareDialog");
                shareDialog.setTitle(detailBean.getName())
                        .setContent(detailBean.getName())
                        .setUrl(String.format("%1$sshareGoods/%2$s",
                                TextUtils.equals("env_test_", BuildConfig.FLAVOR) ? BuildConfig.debugUrl : BuildConfig.releaseUrl
                                , detailBean.getId())
                        );
//                        .setUrl("http://api.inchengzi.com/shareGoods/" + detailBean.getId());
                List<BannerBean> images_arr = detailBean.getImages_arr();
                if (images_arr != null && images_arr.size() > 0) {
                    BannerBean bannerBean = images_arr.get(0);
                    if (bannerBean.getType() == 1) {
                        shareDialog.setShareImage(bannerBean.getCover_image());
                    } else {
                        if (images_arr.size() <= 1) {
                            shareDialog.setShareImage(null);
                        } else {
                            shareDialog.setShareImage(images_arr.get(1).getCover_image());
                        }
                    }
                } else {
                    shareDialog.setShareImage(null);
                }

                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private class GoodsDetailPagerAdapter extends FragmentPagerAdapter {

        String[] title = {"商品", "详情", "评价"};

        public GoodsDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return GoodsFragment.newInstance();
                case 1:
                    return DetailFragment.newInstance();
                case 2:
                    return EstimateFragment.newInstance(mViewModel.getGoodId(), 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
