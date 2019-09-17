package com.chengzi.app.ui.rankinglist.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentSearchRankingListBinding;
import com.chengzi.app.databinding.ItemSearchRankingListLayoutBinding;
import com.chengzi.app.ui.rankinglist.bean.SearchRankingBean;
import com.chengzi.app.ui.rankinglist.viewmodel.SearchRankingListViewModel;
import com.chengzi.app.ui.search.activity.SearchResultActivity;

public class SearchRankingListFragment extends RankingListBaseFragment<FragmentSearchRankingListBinding,
        SearchRankingListViewModel> {

    private PagingLoadHelper helper;

    public SearchRankingListFragment() {
    }

    public static SearchRankingListFragment newInstance() {
        android.os.Bundle args = new Bundle();
        SearchRankingListFragment fragment = new SearchRankingListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_search_ranking_list;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        SearchAdapter adapter = new SearchAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.setPullupEnable(false);
        binding.swipeRefreshView.setLoadMoreViewGone(true);
        viewModel.searchLive.observe(this, objects -> {
            helper.onComplete(objects);
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            SearchRankingBean item = adapter.getItem(position);
            SearchResultActivity.start(getActivity(), item.getKey());
        });

    }

    @Override
    protected void loadData() {
        helper.start();
    }

    private static class SearchAdapter extends BaseQuickAdapter<SearchRankingBean, BaseViewHolder> {

        public SearchAdapter() {
            super(R.layout.item_search_ranking_list_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, SearchRankingBean item) {
            ItemSearchRankingListLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setPos(helper.getAdapterPosition() + 1);
            layoutBinding.setText(item.getKey());
            layoutBinding.executePendingBindings();
        }
    }
}
