package com.chengzi.app.ui.find.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentThreePartiesBindingImpl;
import com.chengzi.app.databinding.ItemThreePartiesLayoutBindingImpl;
import com.chengzi.app.ui.find.activity.ThreePartiesDetailActivity;
import com.chengzi.app.ui.find.bean.ArticleManageBean;
import com.chengzi.app.ui.find.viewmodel.ThreePartiesViewModel;

import java.util.ArrayList;

/**
 * 三方-->人才招聘 行业会议发布 培训发布
 *
 * @ClassName:ThreePartiesFragment
 * @PackageName:com.yimei.app.ui.find.fragment
 * @Create On 2019/4/19 0019   14:24
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */

public class ThreePartiesFragment extends LazyloadFragment<FragmentThreePartiesBindingImpl, ThreePartiesViewModel> {

    //1-人才招聘 2-行业会议 3-培训发布
    private int type = 0;

    private PagingLoadHelper helper;

    public static ThreePartiesFragment newInstance(int type) {
        ThreePartiesFragment fragment = new ThreePartiesFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_three_parties;
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type", 0);
        }
        ThreePartiesAdapter adapter = new ThreePartiesAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.type.set(String.valueOf(type));
        viewModel.articleManageLive.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });

        adapter.setOnItemClickListener((adapter1, view, position1) ->
                ThreePartiesDetailActivity.start(getContext(), adapter.getItem(position1))
        );
    }

    public class ThreePartiesAdapter extends BaseQuickAdapter<ArticleManageBean, BaseViewHolder> {
        public ThreePartiesAdapter() {
            super(R.layout.item_three_parties_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, ArticleManageBean item) {
            ItemThreePartiesLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setUrl(item.getImg());
            binding.executePendingBindings();
        }
    }
}
