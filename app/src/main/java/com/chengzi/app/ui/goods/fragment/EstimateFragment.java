package com.chengzi.app.ui.goods.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.EstimateAdapter;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.FragmentEstimateBinding;
import com.chengzi.app.ui.goods.viewmodel.EstimateViewModel;
import com.chengzi.app.widget.EstimateTypeView;

/**
 * 评价
 *
 * @ClassName:EstimateFragment
 * @PackageName:com.yimei.app.ui.goods.fragment
 * @Create On 2019/4/16 0016   10:17
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/16 0016 handongkeji All rights reserved.
 */
public class EstimateFragment extends LazyloadFragment<FragmentEstimateBinding, EstimateViewModel> {

    private PagingLoadHelper helper;

    public EstimateFragment() {
    }

    public static EstimateFragment newInstance(String targetId, int targetType) {
        android.os.Bundle args = new Bundle();
        args.putString(Sys.EXTRA_TARGET_ID, targetId);
        args.putInt(Sys.EXTRA_TARGET_TYPE, targetType);
        EstimateFragment fragment = new EstimateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_estimate;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        int targetType = getArguments().getInt(Sys.EXTRA_TARGET_TYPE, 1);
        String targetId = getArguments().getString(Sys.EXTRA_TARGET_ID);
        viewModel.setTargetId(targetId);
        viewModel.setTargetType(targetType);

        helper = new PagingLoadHelper(binding.swipeRefreshViewEstimate, viewModel);

        initRecyclerView();

        viewModel.liveData.observe(this, list -> {
            helper.onComplete(list);
        });

    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    private void initRecyclerView() {

        EstimateAdapter adapter = new EstimateAdapter(viewModel.getTargetType());
        adapter.setHeaderAndEmpty(true);
        adapter.setShowPics(true);
        EstimateTypeView estimateTypeView = initHeader();

        adapter.addHeaderView(estimateTypeView);

        binding.swipeRefreshViewEstimate.setAdapter(adapter);
        binding.swipeRefreshViewEstimate.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        estimateTypeView.setListener(type -> {
            viewModel.setType(String.valueOf(type));
            helper.start();
            return false;
        });

    }

    private EstimateTypeView initHeader() {
        EstimateTypeView estimateTypeView = new EstimateTypeView(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = SizeUtils.dp2px(18);
        layoutParams.rightMargin = SizeUtils.dp2px(3);
        layoutParams.topMargin = SizeUtils.dp2px(10);
        estimateTypeView.setLayoutParams(layoutParams);
        return estimateTypeView;
    }

}
