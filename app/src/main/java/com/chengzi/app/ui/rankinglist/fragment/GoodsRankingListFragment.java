package com.chengzi.app.ui.rankinglist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.GoodsListAdapter;
import com.chengzi.app.databinding.FragmentGoodsRankingListBinding;
import com.chengzi.app.ui.rankinglist.viewmodel.GoodsRankListViewModel;

/**
 * 商品排行榜
 * @ClassName:GoodsRankingListFragment

 * @PackageName:com.yimei.app.ui.rankinglist.fragment

 * @Create On 2019/4/10 0010   14:03

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class GoodsRankingListFragment extends RankingListBaseFragment<FragmentGoodsRankingListBinding,
        GoodsRankListViewModel> {

    private PagingLoadHelper helper;

    public GoodsRankingListFragment() {
    }

    public static GoodsRankingListFragment newInstance() {
        android.os.Bundle args = new Bundle();
        GoodsRankingListFragment fragment = new GoodsRankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_goods_ranking_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        GoodsListAdapter adapter = new GoodsListAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPullupEnable(false);
        binding.swipeRefreshView.setLoadMoreViewGone(true);

        viewModel.goodsListLive.observe(this, objects -> {
            helper.onComplete(objects);
        });
    }

    @Override
    protected void loadData() {
        helper.start();
    }
}
