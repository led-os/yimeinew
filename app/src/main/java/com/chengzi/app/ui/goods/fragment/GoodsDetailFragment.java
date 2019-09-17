package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentGoodsDetailBinding;
import com.chengzi.app.ui.goods.viewmodel.GoodsDetailViewModel;
import com.chengzi.app.widget.HDWebView;

/**
 * 商品--> 详情--> 商品详情
 *
 * @ClassName:GoodsDetailFragment
 * @PackageName:com.yimei.app.ui.goods.fragment
 * @Create On 2019/4/16 0016   15:14
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class GoodsDetailFragment extends BaseFragment<FragmentGoodsDetailBinding,
        GoodsDetailViewModel> {

    private HDWebView webView;

    public GoodsDetailFragment() {
    }

    public static GoodsDetailFragment newInstance() {
        android.os.Bundle args = new Bundle();
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        binding.goodsInfoContainer.removeAllViews();
        webView = new HDWebView(getActivity());
        webView.scrollable(true);
        webView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        binding.goodsInfoContainer.addView(webView);

        viewModel.goodDetailLive.observe(this,bean -> {
            String goods_info = bean.getGoods_info();   //  商品详情 图文混合

            webView.setContent(goods_info);

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.goodsInfoContainer.removeAllViews();
    }
}
