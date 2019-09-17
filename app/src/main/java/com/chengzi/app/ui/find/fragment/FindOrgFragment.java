package com.chengzi.app.ui.find.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.handong.framework.base.BaseFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.FindBaseAdapter;
import com.chengzi.app.adapter.FindOrgAdapter;
import com.chengzi.app.databinding.FragmentFindOrgBinding;
import com.chengzi.app.ui.find.viewmodel.FindOrgViewModel;
import com.chengzi.app.ui.home.bean.HospitalBean;

import java.util.List;
/**
 * 找机构
 * @ClassName:FindOrgFragment

 * @PackageName:com.yimei.app.ui.find.fragment

 * @Create On 2019/4/11 0011   10:47

 * @Site:http://www.handongkeji.com

 * @author:zhouhao

 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class FindOrgFragment extends BaseFragment<FragmentFindOrgBinding, FindOrgViewModel> {

    public FindOrgFragment() {
        // Required empty public constructor
    }

    public static FindOrgFragment newInstance() {
        android.os.Bundle args = new Bundle();
        FindOrgFragment fragment = new FindOrgFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_find_org;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0,50);

        FindBaseAdapter<HospitalBean> baseAdapter = new FindBaseAdapter();
        baseAdapter.setFactory(new FindBaseAdapter.ItemAdapterFactory<HospitalBean>() {
            @Override
            public BaseQuickAdapter createAdapter(List<HospitalBean> list) {
                FindOrgAdapter adapter = new FindOrgAdapter();
                adapter.setNewData(list);
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
        viewModel.orgLive.observe(this, list -> {
            helper.onComplete(list);
        });
    }

}
