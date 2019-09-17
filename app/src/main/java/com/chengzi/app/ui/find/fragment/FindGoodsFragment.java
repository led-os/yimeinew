package com.chengzi.app.ui.find.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.FindBaseAdapter;
import com.chengzi.app.adapter.MainGoodsAdapter;
import com.chengzi.app.databinding.FragmentFindGoodsBinding;
import com.chengzi.app.ui.find.viewmodel.FindGoodsViewModel;
import com.chengzi.app.ui.home.bean.GoodBean;

import java.util.List;

/**
 * 找商品
 *
 * @ClassName:FindGoodsFragment
 * @PackageName:com.yimei.app.ui.find.fragment
 * @Create On 2019/4/11 0011   09:47
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class FindGoodsFragment extends BaseFragment<FragmentFindGoodsBinding, FindGoodsViewModel> {

    public FindGoodsFragment() {
    }

    public static FindGoodsFragment newInstance() {
        android.os.Bundle args = new Bundle();
        FindGoodsFragment fragment = new FindGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find_goods;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 50);

        FindBaseAdapter<GoodBean> baseAdapter = new FindBaseAdapter();
        baseAdapter.setFactory(new FindBaseAdapter.ItemAdapterFactory<GoodBean>() {
            @Override
            public BaseQuickAdapter createAdapter(List<GoodBean> list) {
                MainGoodsAdapter adapter = new MainGoodsAdapter(2);
                adapter.setNewData(list);
//                adapter.setOnItemClickListener((adapter1, view, position) -> {
//                    GoodBean item = adapter.getItem(position);
//                    //找商品->商品搜索推广
//                    GoodsDetailActivity.start(getActivity(), item.getId(), item.getPromotion_id(), 2);
//                });
                return adapter;
            }

            @Override
            public void setRecyclerPool(RecyclerView recyclerView) {
                recyclerView.setRecycledViewPool(pool);
            }
        });

        binding.swipeRefreshView.setAdapter(baseAdapter);
        binding.swipeRefreshView.setPullupEnable(false);


        PagingLoadHelper helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        helper.start();
        viewModel.goodsLive.observe(this, list -> {
            helper.onComplete(list);
        });
    }
}
