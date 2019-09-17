package com.chengzi.app.ui.mine.fragment;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.handong.framework.base.LazyloadFragment;
import com.chengzi.app.R;
import com.chengzi.app.adapter.NineImageAdapter;
import com.chengzi.app.databinding.FragmentHospitalLookEvaluateBindingImpl;
import com.chengzi.app.databinding.ItemHospitalLookEvaluateLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.LookReviewBean;
import com.chengzi.app.ui.mine.bean.LookUserNumBean;
import com.chengzi.app.ui.mine.viewmodel.HospitalLookEvaluateViewModel;

import java.util.ArrayList;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

/**
 * 评价
 *
 * @ClassName:HospitalLookEvaluateFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/5/20 0020   15:40
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/20 0020 handongkeji All rights reserved.
 */

public class HospitalLookEvaluateFragment extends LazyloadFragment<FragmentHospitalLookEvaluateBindingImpl, HospitalLookEvaluateViewModel> {


    private PagingLoadHelper helper;
    private String user_type;

    public static HospitalLookEvaluateFragment newInstance(String user_type) {
        HospitalLookEvaluateFragment fragment = new HospitalLookEvaluateFragment();
        Bundle args = new Bundle();
        args.putString("user_type", user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_hospital_look_evaluate;
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
        CheckLookAdapter adapter = new CheckLookAdapter();
        binding.swipeRefreshView.setAdapter(adapter);
        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.lookReviewLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    public class CheckLookAdapter extends BaseQuickAdapter<LookReviewBean, BaseViewHolder> {

        public CheckLookAdapter() {
            super(R.layout.item_hospital_look_evaluate_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, LookReviewBean item) {
            ItemHospitalLookEvaluateLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener1);
            binding.setBean(item);
            LookUserNumBean user = item.getUser();
            binding.setUrl(user.getHeadimg());

            LookUserNumBean doctor = item.getDoctor();
            if (doctor != null) {
                binding.clDoctor.setVisibility(View.VISIBLE);
                binding.setUrld(doctor.getHeadimg());
            } else {
                binding.clDoctor.setVisibility(View.GONE);
            }

            LookUserNumBean counselling = item.getCounselling();
            if (counselling != null) {
                binding.clCounselor.setVisibility(View.VISIBLE);
                binding.setUrlc(counselling.getHeadimg());
            } else {
                binding.clCounselor.setVisibility(View.GONE);
            }
            binding.executePendingBindings();

            //tv_sex性别
            String gender = item.getUser().getGender();
            binding.tvSex.setBackgroundResource(gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);

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

    private ClickEventHandler<LookReviewBean> clickListener1 = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  用户主页
                UserHomePageActivity.start(getContext(), bean.getUser_id());
                break;
            case R.id.cl_doctor:  //  医生主页
                DoctorHomePageActivity.startDoctor(getContext(), bean.getDoctor_id());
                break;
            case R.id.cl_counselor:  //  咨询师
                DoctorHomePageActivity.startCounselor(getContext(), bean.getCounselling_id());
                break;
        }
    };
}
