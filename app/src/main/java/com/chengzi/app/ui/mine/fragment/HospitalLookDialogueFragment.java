package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentHospitalLookDialogueBindingImpl;
import com.chengzi.app.databinding.ItemCompetitiveAnalysisLayoutBinding;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.bean.LookSheetNumBean;
import com.chengzi.app.ui.mine.viewmodel.HospitalLookDialogueViewModel;

import java.util.ArrayList;

/**
 * 对话量
 *
 * @ClassName:HospitalLookDialogueFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/5/20 0020   11:24
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/20 0020 handongkeji All rights reserved.
 */

public class HospitalLookDialogueFragment extends LazyloadFragment<FragmentHospitalLookDialogueBindingImpl, HospitalLookDialogueViewModel> {

    private PagingLoadHelper helper;
    private String user_type;

    public static HospitalLookDialogueFragment newInstance(String user_type) {
        HospitalLookDialogueFragment fragment = new HospitalLookDialogueFragment();
        Bundle args = new Bundle();
        args.putString("user_type", user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_look_dialogue;
    }

    @Override
    public void onLazyload() {
        helper.start();
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
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            LookSheetNumBean lookSheetNumBean = adapter.getData().get(position);
            //用户类型
            String type = lookSheetNumBean.getType();
            if (type.equals("2")) { //医生主页
                DoctorHomePageActivity.startDoctor(getContext(), lookSheetNumBean.getId());
            } else if (type.equals("3")) {    //咨询师主页
                DoctorHomePageActivity.startCounselor(getContext(), lookSheetNumBean.getId());
            }
        });

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.lookSheetNumLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
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
            binding.tvHint.setText("对话量：");
        }
    }
}