package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentReservationManageBindingImpl;
import com.chengzi.app.databinding.ItemReservationManageLayoutBindingImpl;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.HospitalDoctorAppointmentBean;
import com.chengzi.app.ui.mine.viewmodel.ReservationManageViewModel;

import java.util.ArrayList;

/**
 * 预约管理-->医院(医生预约 /医院预约)
 *
 * @ClassName:ReservationManageActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/11 0011   10:07
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */
public class ReservationManageFragment extends LazyloadFragment<FragmentReservationManageBindingImpl, ReservationManageViewModel> {

    private PagingLoadHelper helper;

    //用户类型 2 医生 4 机构 （必传）
    private int type;

    public static ReservationManageFragment newInstance(int type) {
        ReservationManageFragment fragment = new ReservationManageFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_reservation_manage;
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type", 2);
        }
        ReservationManageAdapter adapter = new ReservationManageAdapter();
        binding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.type = String.valueOf(type);
        viewModel.hospitalDoctorAppointmentLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    public class ReservationManageAdapter extends BaseQuickAdapter<HospitalDoctorAppointmentBean, BaseViewHolder> {

        public ReservationManageAdapter() {
            super(R.layout.item_reservation_manage_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HospitalDoctorAppointmentBean item) {
            ItemReservationManageLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener);
            binding.setBean(item);
            HospitalDoctorAppointmentBean.CustomerEntity customer = item.getCustomer();
            binding.setUrl(customer.getHeadimg());
            HospitalDoctorAppointmentBean.CustomerEntity object = item.getObject();
            binding.setUrlh(object.getHeadimg());
            binding.executePendingBindings();
            //tv_sex性别
            String uu_gender = customer.getGender();
            binding.tvSex.setBackgroundResource(uu_gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(uu_gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);

            //接口类型 1 医生预约 2 医院预约
            binding.tvType.setVisibility(object.getType().equals("2") ? View.VISIBLE : View.GONE);
            binding.tvScore.setText(object.getType().equals("2") ? "审美：" + customer.getGrade() + "分       技术：" + customer.getSkill_grade() + "分"
                    : "评分：" + object.getGrade() + "分");
        }
    }

    private ClickEventHandler<HospitalDoctorAppointmentBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  用户主页
                UserHomePageActivity.start(getContext(), bean.getCustomer().getId());
                break;
            case R.id.cl_doctor_or_hospital:  //  医生 或者 医院  主页
//                toast(position == 0 ? "医生主页" : "医院主页");
//                String doctor_id = bean.getDoctor_id();
//                String hospital_id = bean.getTo_uid();
                String id = bean.getObject().getId();
                String type = bean.getObject().getType();
                if (type.equals("2") && !TextUtils.isEmpty(id)) {
                    if (id.equals(AccountHelper.getUserId())) { //医生自己的主页
                        DoctorHomePageActivity.startDoctorSelt(getContext());
                    } else {    //别人的医生主页
                        DoctorHomePageActivity.startDoctor(getContext(), id);
                    }
                } else if (type.equals("4") && !TextUtils.isEmpty(id)) {
                    if (id.equals(AccountHelper.getUserId())) { //医院自己的主页
                        HospitalHomePageActivity.startSelf(getContext());
                    } else {    //别人的医院主页
                        HospitalHomePageActivity.start(getContext(), id);
                    }
                }
                break;
        }
    };
}