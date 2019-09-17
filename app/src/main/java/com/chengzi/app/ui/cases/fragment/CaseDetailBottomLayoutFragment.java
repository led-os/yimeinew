package com.chengzi.app.ui.cases.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.handong.framework.base.BaseFragment;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentCaseDetailBottomLayoutBinding;
import com.chengzi.app.ui.cases.viewmodel.CaseDetailViewModel;
import com.chengzi.app.ui.comment.viewmodel.CommentContainerViewModel;

public class CaseDetailBottomLayoutFragment extends BaseFragment<FragmentCaseDetailBottomLayoutBinding,
        CaseDetailViewModel> implements View.OnClickListener {

    private CommentContainerViewModel commentContainerViewModel;

    public CaseDetailBottomLayoutFragment() {
    }

    public static CaseDetailBottomLayoutFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(CaseDetailBottomLayoutFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new CaseDetailBottomLayoutFragment();
            fragment.setArguments(args);
        }
        return (CaseDetailBottomLayoutFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_case_detail_bottom_layout;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        commentContainerViewModel = ViewModelProviders.of(getActivity())
                .get(CommentContainerViewModel.class);

        binding.setViewModel(commentContainerViewModel);
        binding.setListener(this);
    }

    @Override
    public void onClick(View v) {
        commentContainerViewModel.isComment.set(true);
        commentContainerViewModel.topComment();
    }
}
