package com.chengzi.app.ui.home.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentAcademicDynamicBinding;
import com.chengzi.app.databinding.ItemAcademicLayoutBinding;
import com.chengzi.app.ui.find.activity.ThreePartiesActivity;
import com.chengzi.app.ui.home.bean.HomeArticleBean;
import com.chengzi.app.ui.home.viewmodel.MainDynamicViewModel;
import com.chengzi.app.ui.home.viewmodel.RecommandViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 首页--学术动态
 *
 * @ClassName:AcademicDynamicFragment
 * @PackageName:com.yimei.app.ui.home.fragment
 * @Create On 2019/4/2 0002   19:24
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/2 0002 handongkeji All rights reserved.
 */
public class AcademicDynamicFragment extends BaseFragment<FragmentAcademicDynamicBinding, MainDynamicViewModel> {

    private boolean isResumed;
    private boolean isHidden;

    private AcademicAdapter adapter;

    public static AcademicDynamicFragment newInstance() {
        Bundle args = new Bundle();
        AcademicDynamicFragment fragment = new AcademicDynamicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AcademicDynamicFragment() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_academic_dynamic;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        adapter = new AcademicAdapter();
        binding.recyclerAcademicDynamic.setAdapter(adapter);


        viewModel.homeArticleLive.observe(this, homeArticleBean -> {
            dismissLoading();
            if (homeArticleBean != null && homeArticleBean.size() > 0) {
                binding.recyclerAcademicDynamic.setVisibility(View.VISIBLE);
                binding.emptyView.setVisibility(View.GONE);
                adapter.setNewData(homeArticleBean);
            } else {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.recyclerAcademicDynamic.setVisibility(View.GONE);
            }
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String type = adapter.getData().get(position).getType();
            int types = 0;
            if (type.equals("1")) {
                types = ThreePartiesActivity.THREEPARTIES_RECRUITMENT;
            } else if (type.equals("2")) {
                types = ThreePartiesActivity.THREEPARTIES_MEETING;
            } else if (type.equals("3")) {
                types = ThreePartiesActivity.THREEPARTIES_TRAINING;
            }
            startActivity(new Intent(getActivity(), ThreePartiesActivity.class).putExtra("extra", types));
        });
        handler.sendEmptyMessageDelayed(0, 1500);

        RecommandViewModel homeViewModel = ViewModelProviders.of(getActivity()).get(RecommandViewModel.class);
        homeViewModel.refreshLive.observe(this,aBoolean -> {
            viewModel.homeArticle();
        });
    }

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {

        WeakReference<AcademicDynamicFragment> fragment;

        public MyHandler(AcademicDynamicFragment fragment) {
            this.fragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (fragment.get() == null) {
                return;
            }
            AcademicDynamicFragment instance = fragment.get();
            List<HomeArticleBean> data = instance.adapter.getData();

            if (data != null && data.size() > 0) {
                HomeArticleBean o = data.get(0);
                instance.adapter.remove(0);
                instance.adapter.addData(o);

                if (instance.isResumed && instance.getUserVisibleHint() && !instance.isHidden) {
                    sendEmptyMessageDelayed(0, 1500);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.homeArticle();
        isResumed = true;
        startAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        isResumed = false;
        stopAction();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            startAction();
        } else {
            stopAction();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.isHidden = hidden;
        if (isHidden) {
            stopAction();
        } else {
            startAction();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAction();
    }

    private void startAction() {
        boolean isVisible = isResumed && getUserVisibleHint() && !isHidden;
        if (isVisible && !handler.hasMessages(0) && adapter != null && adapter.getData() != null && adapter.getData().size() > 0) {
            handler.sendEmptyMessageDelayed(0, 1500);
        }
    }

    private void stopAction() {
        handler.removeMessages(0);
    }


    private static class AcademicAdapter extends BaseQuickAdapter<HomeArticleBean, BaseViewHolder> {

        public AcademicAdapter() {
            super(R.layout.item_academic_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HomeArticleBean item) {
            ItemAcademicLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.executePendingBindings();
        }
    }
}
