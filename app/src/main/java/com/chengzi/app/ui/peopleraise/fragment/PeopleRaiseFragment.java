package com.chengzi.app.ui.peopleraise.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.PeopleRaiseAdapter;
import com.chengzi.app.databinding.FragmentPeopleRaiseBinding;
import com.chengzi.app.event.AddRaiseEvent;
import com.chengzi.app.ui.peopleraise.OnTopicChangeRegistry;
import com.chengzi.app.ui.peopleraise.activity.PeopleRaiseDetailActivity;
import com.chengzi.app.ui.peopleraise.bean.RaiseBean;
import com.chengzi.app.ui.peopleraise.viewmodel.PeopleRaiseViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 美人筹列表
 *
 * @ClassName:PeopleRaiseFragment
 * @PackageName:com.yimei.app.ui.peopleraise.fragment
 * @Create On 2019/4/9 0009   16:20
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */
public class PeopleRaiseFragment extends LazyloadFragment<FragmentPeopleRaiseBinding,
        PeopleRaiseViewModel> {

    private static final String EXTRA_STATUS = "extra_status";
    private PagingLoadHelper helper;
    private boolean needRefresh;

    public PeopleRaiseFragment() {
    }

    public static PeopleRaiseFragment newInstance(int status) {
        android.os.Bundle args = new Bundle();
        args.putInt(EXTRA_STATUS, status);
        PeopleRaiseFragment fragment = new PeopleRaiseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_people_raise;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((OnTopicChangeRegistry<String>) getActivity()).observeCategory(categoryId -> {
            viewModel.setCategoryId(categoryId);
            if (getUserVisibleHint()) {
                onLazyload();
            } else {
                needRefresh = true;
            }
        });
        ((OnTopicChangeRegistry<String>) getActivity()).observeSearch(keyword -> {
            viewModel.setKeyword(keyword);
            if (getUserVisibleHint()) {
                onLazyload();
            } else {
                needRefresh = true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        int status = getArguments().getInt(EXTRA_STATUS);
        viewModel.setStatus(status == 1 ? "2" : (status == 2 ? "4" : "1"));

        initRecycler();
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        viewModel.liveData.observe(this, objects -> {
            helper.onComplete(objects);
        });
    }

    private void initRecycler() {

        PeopleRaiseAdapter adapter = new PeopleRaiseAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            RaiseBean item = adapter.getItem(position);
            PeopleRaiseDetailActivity.start(getActivity(), item.getId(), viewModel.getCategoryId());
        });
    }

    @Override
    public void onLazyload() {
        if (!TextUtils.isEmpty(viewModel.getCategoryId())) {
            needRefresh = false;
            helper.start();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && needRefresh) {
            needRefresh = false;
            onLazyload();
        }
    }

    @Subscribe
    public void onNewRaiseAdded(AddRaiseEvent event){
        int status = getArguments().getInt(EXTRA_STATUS);
        if (status == 0) {
            helper.start();
        }
    }
}
