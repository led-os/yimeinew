package com.chengzi.app.ui.homepage.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.LinearLayout;

import com.chengzi.app.R;
import com.chengzi.app.adapter.EstimateAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentSubEstimateBinding;
import com.chengzi.app.ui.goods.activity.GoodsDetailActivity;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.activity.EstimateActivity;
import com.chengzi.app.ui.homepage.viewmodel.SubEstimateViewModel;

/**
 * 医生/咨询师/医院 主页 中 评价的部分
 *
 * @ClassName:SubEstimateFragment
 * @PackageName:com.yimei.app.ui.homepage.fragment
 * @Create On 2019/4/18 0018   16:28
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/18 0018 handongkeji All rights reserved.
 */
public class SubEstimateFragment extends RefreshableFragment<FragmentSubEstimateBinding,
        SubEstimateViewModel> implements View.OnClickListener {


    private EstimateAdapter estimateAdapter;

    public SubEstimateFragment() {

    }

    /**
     * @param targetId 商品id | 医生id | 咨询师id | 医院id
     * @param type     评价类型 1 商品 ，2医生 ，3 咨询师， 4 医院
     */
    public static SubEstimateFragment newInstance(FragmentManager fm, String targetId, int type) {
        SubEstimateFragment fragment = (SubEstimateFragment) fm.findFragmentByTag(SubEstimateFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            args.putInt(Sys.EXTRA_TARGET_TYPE, type);
            args.putString(Sys.EXTRA_TARGET_ID, targetId);
            fragment = new SubEstimateFragment();
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sub_estimate;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        binding.setListener(this);

        int targetType = getArguments().getInt(Sys.EXTRA_TARGET_TYPE, 1);
        String targetId = getArguments().getString(Sys.EXTRA_TARGET_ID);
        viewModel.setTargetType(targetType);
        viewModel.setTargetId(targetId);

        if (viewModel.getTargetType() == 2 || viewModel.getTargetType() == 3) {
            binding.tvEvalute.setText(getString(R.string.ta_estimate));
        } else {
            binding.tvEvalute.setText(getString(R.string.good_estimate));
        }

        binding.estimateTypeView.setListener(type -> {
            if (viewModel.getTargetType() == 1) {
                ((GoodsDetailActivity) getActivity()).setCurrentPageItem(2);
            } else {
                EstimateActivity.start(getActivity(), viewModel.getTargetId(), viewModel.getTargetType());
            }
            return true;
        });

        initComments();

        viewModel.evaluateLive.observe(this, evaluateBeans -> {
            estimateAdapter.setNewData(evaluateBeans);
            if (estimateAdapter.getData().isEmpty()) {
                View empty = getLayoutInflater().inflate(R.layout.estimate_empty_layout, binding.recyclerComment, false);
                estimateAdapter.setEmptyView(empty);
                empty.setOnClickListener(this);
            }
            refreshComplete = true;
            finishRefresh();
        });

    }

    private void initComments() {

        estimateAdapter = new EstimateAdapter(viewModel.getTargetType());
//        estimateAdapter.setShowPics(false);
        estimateAdapter.bindToRecyclerView(binding.recyclerComment);
        binding.recyclerComment.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        estimateAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (viewModel.getTargetType() == 1) {
                ((GoodsDetailActivity) getActivity()).setCurrentPageItem(2);
            } else {
                EstimateActivity.start(getActivity(), viewModel.getTargetId(), viewModel.getTargetType());
            }
        });

    }

    @Override
    public void onRefresh() {
        refreshComplete = false;
        viewModel.evaluateList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.estimate_empty_view:
            case R.id.btn_view_all_estimate:
                if (viewModel.getTargetType() == 1) {
                    ((GoodsDetailActivity) getActivity()).setCurrentPageItem(2);
                } else {
                    EstimateActivity.start(getActivity(), viewModel.getTargetId(), viewModel.getTargetType());
                }
                break;
        }
    }
}
