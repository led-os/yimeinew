package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentDoctorCheckLookBindingImpl;
import com.chengzi.app.databinding.ItemDoctorCheckLookLayoutBinding;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.LookHospitalOrderBean;
import com.chengzi.app.ui.mine.bean.LookUserNumBean;
import com.chengzi.app.ui.mine.viewmodel.DoctorCheckLookViewModel;

import java.util.ArrayList;


/**
 * 医院 -->查看-->下单
 *
 * @ClassName:HospitalCheckLookActivity
 * @PackageName:com.yimei.app.ui.mine.activity.checklook
 * @Create On 2019/4/11 0011   11:06
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class DoctorCheckLookFragment extends LazyloadFragment<FragmentDoctorCheckLookBindingImpl, DoctorCheckLookViewModel> {

    private PagingLoadHelper helper;
    private String user_type;

    public static DoctorCheckLookFragment newInstance(String user_type) {
        DoctorCheckLookFragment fragment = new DoctorCheckLookFragment();
        Bundle args = new Bundle();
        args.putString("user_type", user_type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_doctor_check_look;
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
        viewModel.lookOrderLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }


    public class CheckLookAdapter extends BaseQuickAdapter<LookHospitalOrderBean, BaseViewHolder> {

        public CheckLookAdapter() {
            super(R.layout.item_doctor_check_look_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, LookHospitalOrderBean item) {
            ItemDoctorCheckLookLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
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
            String gender = user.getGender();
            binding.tvSex.setBackgroundResource(gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);
        }
    }

    private ClickEventHandler<LookHospitalOrderBean> clickListener1 = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  用户主页
                UserHomePageActivity.start(getContext(), bean.getUser().getId());
                break;
            case R.id.cl_doctor:  //  医生主页
                DoctorHomePageActivity.startDoctor(getContext(), bean.getDoctor().getId());
                break;
            case R.id.cl_counselor:  //  咨询师
                DoctorHomePageActivity.startCounselor(getContext(), bean.getCounselling().getId());
                break;
        }
    };
}
