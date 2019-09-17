package com.chengzi.app.ui.mine.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.adapter.NineImageAdapter;
import com.chengzi.app.databinding.FragmentHospitalLookComplaintBindingImpl;
import com.chengzi.app.databinding.ItemHospitalLookComplaintLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.bean.LookComplaintBean;
import com.chengzi.app.ui.mine.viewmodel.HospitalLookComplaintViewModel;

import java.util.ArrayList;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

/**
 * 投诉
 *
 * @ClassName:HospitalLookComplaintFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/5/20 0020   15:39
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/20 0020 handongkeji All rights reserved.
 */

public class HospitalLookComplaintFragment extends LazyloadFragment<FragmentHospitalLookComplaintBindingImpl, HospitalLookComplaintViewModel> {
    private PagingLoadHelper helper;
    private String user_type;

    public static HospitalLookComplaintFragment newInstance(String user_type) {
        HospitalLookComplaintFragment fragment = new HospitalLookComplaintFragment();
        Bundle args = new Bundle();
        args.putString("user_type", user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_look_complaint;
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
        HospitalLookComplaintAdapter adapter = new HospitalLookComplaintAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.lookComplaintLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }


    public class HospitalLookComplaintAdapter extends BaseQuickAdapter<LookComplaintBean, BaseViewHolder> {
        public HospitalLookComplaintAdapter() {
            super(R.layout.item_hospital_look_complaint_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, LookComplaintBean item) {
            ItemHospitalLookComplaintLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener1);
            binding.setBean(item);
            binding.setUrl(item.getUser().getHeadimg());
            binding.executePendingBindings();

            //评分
            LookComplaintBean.UserEntity user = item.getUser();
            String grade = user.getGrade();
            String skill_grade = user.getSkill_grade();
            String title = user.getType().equals("2") ? "技术: " : "服务: ";
            binding.tvGrade.setText("审美: " + grade + "分  " + title + skill_grade + "分");

            binding.mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            NineImageAdapter imageAdapter = new NineImageAdapter();
            binding.mRecyclerView.setAdapter(imageAdapter);
            ArrayList<String> strings = (ArrayList<String>) item.getImage();
            if (strings != null && strings.size() > 0)
                imageAdapter.setNewData(strings);
            //看大图
            imageAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    startActivity(new Intent(getContext(), PictureSwitcherActivity.class)
                            .putExtra(PICTURE_URLS, strings)
                            .putExtra(PICTURE_INDEX, position)
                    );
                }
            });
        }
    }

    private ClickEventHandler<LookComplaintBean> clickListener1 = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  医生/咨询师主页
                if (viewModel.user_type.get().equals("2"))
                    DoctorHomePageActivity.startDoctor(getContext(), bean.getUser().getId());
                else
                    DoctorHomePageActivity.startCounselor(getContext(), bean.getUser().getId());
                break;
        }
    };
}
