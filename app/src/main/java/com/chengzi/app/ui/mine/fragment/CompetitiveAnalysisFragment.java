package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.chengzi.app.databinding.FragmentCompetitiveAnalysisBindingImpl;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemCompetitiveAnalysisLayoutBinding;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.bean.LookSheetNumBean;
import com.chengzi.app.ui.mine.viewmodel.CompetitiveAnalysisViewModel;

import java.util.ArrayList;

/**
 * 竞对分析
 */
public class CompetitiveAnalysisFragment extends LazyloadFragment<FragmentCompetitiveAnalysisBindingImpl, CompetitiveAnalysisViewModel> {

    private PagingLoadHelper helper;
    private String user_type;

    public static CompetitiveAnalysisFragment newInstance(String user_type) {
        CompetitiveAnalysisFragment fragment = new CompetitiveAnalysisFragment();
        Bundle args = new Bundle();
        args.putString("user_type", user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_competitive_analysis;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            user_type = arguments.getString("user_type");
        }
        //医生/咨询师
        viewModel.user_type.set(user_type);
        CompetitiveAnalysisAdapter adapter = new CompetitiveAnalysisAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            LookSheetNumBean bean = adapter.getData().get(position);
            if (viewModel.user_type.get().equals("2")) {
                DoctorHomePageActivity.startDoctor(getContext(), bean.getId());
            } else {
                DoctorHomePageActivity.startCounselor(getContext(), bean.getId());
            }
        });
        observe();
    }

    //竞对分析
    private void observe() {
        viewModel.competitiveAnalysisLiveData.observe(this, objects -> {
            if (objects != null) {
//                helper.onComplete(objects.getData());
                if (user_type.equals("2")) {
                    if (objects.getDoctorRanking() != null && objects.getDoctorRanking().size() > 0) {
                        helper.onComplete(objects.getDoctorRanking());
                    } else {
                        helper.onComplete(new ArrayList<>());
                    }
                } else {
                    if (objects.getConsultantRanking() != null && objects.getConsultantRanking().size() > 0) {
                        helper.onComplete(objects.getConsultantRanking());
                    } else {
                        helper.onComplete(new ArrayList<>());
                    }
                }
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    public static class CompetitiveAnalysisAdapter extends BaseQuickAdapter<LookSheetNumBean, BaseViewHolder> {
        public CompetitiveAnalysisAdapter() {
            super(R.layout.item_competitive_analysis_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, LookSheetNumBean item) {
            ItemCompetitiveAnalysisLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.executePendingBindings();
        }
    }
}