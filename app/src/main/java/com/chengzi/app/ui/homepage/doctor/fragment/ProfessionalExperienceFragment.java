package com.chengzi.app.ui.homepage.doctor.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentProfessionalExperienceBinding;
import com.chengzi.app.databinding.ItemImage2LayoutBindingImpl;
import com.chengzi.app.databinding.ItemProfessionalExperienceLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.homepage.bean.UserRecordListBean;
import com.chengzi.app.ui.homepage.doctor.activity.AddRecordActivity;
import com.chengzi.app.ui.homepage.viewmodel.ProfessionExperienceViewModel;

import java.util.ArrayList;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

/**
 * Ta/我 的履历
 *
 * @ClassName:ProfessionalExperienceFragment
 * @PackageName:com.yimei.app.ui.homepage.doctor.fragment
 * @Create On 2019/5/28 0028   09:57
 * @Site:http://www.handongkeji.com
 * @author:
 * @Copyrights 2019/5/28 0028 handongkeji All rights reserved.
 */

public class ProfessionalExperienceFragment extends LazyloadFragment<FragmentProfessionalExperienceBinding, ProfessionExperienceViewModel> {

    private PagingLoadHelper helper;
    public static ProfessionalExperienceFragment fragment;

    public ProfessionalExperienceFragment() {
    }

    public static ProfessionalExperienceFragment newInstance(boolean isSelf, String doctor_id, int user_type) {
        android.os.Bundle args = new Bundle();
        ProfessionalExperienceFragment fragment = new ProfessionalExperienceFragment();
        args.putBoolean("isSelf", isSelf);
        args.putString("doctor_id", doctor_id);
        args.putInt("user_type", user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_professional_experience;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        fragment = this;
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);

        Bundle arguments = getArguments();
        if (arguments != null) {
            viewModel.setSelf(arguments.getBoolean("isSelf"));
            viewModel.setDoctorId(arguments.getString("doctor_id"));
            viewModel.setUserType(arguments.getInt("user_type"));
            if (viewModel.isSelf()) {
                //是自己履历的情况下 才可以添加履历
                binding.btnAddExperience.setVisibility(View.VISIBLE);
            }
            binding.setIsSelf(viewModel.isSelf());
        }
        binding.setListener(clickListener);
        RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
        pool.setMaxRecycledViews(0, 30);
        ExperienceAdapter adapter = new ExperienceAdapter(pool);
        binding.swipeRefreshView.setAdapter(adapter);
        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        binding.swipeRefreshView.setLoadMoreViewGone(true);
        viewModel.userRecordListLiveData.observe(this, list -> {
            if (list != null && list.size() > 0) {
                helper.onComplete(list);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    //添加成功后刷新
    public void refresh() {
        helper.onPulldown();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragment = null;
    }

    private ClickEventHandler<Object> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.btn_add_experience:  //  添加履历
                startActivity(new Intent(getContext(), AddRecordActivity.class)
                        .putExtra("doctorId", viewModel.getDoctorId())
                );
                break;
        }
    };

    private class ExperienceAdapter extends BaseQuickAdapter<UserRecordListBean, BaseViewHolder> {
        private RecyclerView.RecycledViewPool pool;

        public ExperienceAdapter(RecyclerView.RecycledViewPool pool) {
            super(R.layout.item_professional_experience_layout);
            this.pool = pool;
        }

        @Override
        protected void convert(BaseViewHolder helper, UserRecordListBean item) {
            ItemProfessionalExperienceLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setBean(item);
            binding.executePendingBindings();
            ImageAdapter imageAdapter = new ImageAdapter();
//            binding.nineGridView.swapAdapter(imageAdapter, false);
//            binding.nineGridView.setRecycledViewPool(pool);
            ArrayList<String> imgs = (ArrayList<String>) item.getImage();
            if (imgs != null && imgs.size() > 0) {
                binding.nineGridView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                binding.nineGridView.setAdapter(imageAdapter);
                imageAdapter.setNewData(imgs);
                imageAdapter.setOnItemClickListener((adapter1, view1, position) -> {
                    startActivity(new Intent(getContext(), PictureSwitcherActivity.class)
                            .putExtra(PICTURE_URLS, imgs)
                            .putExtra(PICTURE_INDEX, position)
                    );
                });
            } else {
                imageAdapter.setNewData(new ArrayList<>());
            }
        }
    }

    private static class ImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public ImageAdapter() {
            super(R.layout.item_image2_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ItemImage2LayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setUrl(item);
            binding.executePendingBindings();
        }
    }
}