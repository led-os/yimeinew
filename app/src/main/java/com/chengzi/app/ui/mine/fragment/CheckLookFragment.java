package com.chengzi.app.ui.mine.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.LazyloadFragment;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentCheckLookBindingImpl;
import com.chengzi.app.databinding.ItemCheckLookLayoutBinding;
import com.chengzi.app.ui.homepage.user.activity.UserHomePageActivity;
import com.chengzi.app.ui.mine.bean.MyAppointmentBean;
import com.chengzi.app.ui.mine.viewmodel.CheckLookViewModel;

import java.util.ArrayList;

/**
 * 医生/咨询师 查看(预约我的/投诉我的/下单我的)
 *
 * @ClassName:CheckLookFragment
 * @PackageName:com.yimei.app.ui.mine.fragment
 * @Create On 2019/4/10 0010   10:04
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/10 0010 handongkeji All rights reserved.
 */
public class CheckLookFragment extends LazyloadFragment<FragmentCheckLookBindingImpl, CheckLookViewModel> {

    private PagingLoadHelper helper;

    public static CheckLookFragment newInstance() {
        CheckLookFragment fragment = new CheckLookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_check_look;
    }

    @Override
    public void onLazyload() {
        helper.start();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
//        Bundle arguments = getArguments();
//        if (arguments != null) {
//            position = arguments.getInt("position", 0);
//        }
        MyBookingAdapter adapter = new MyBookingAdapter();
        binding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(binding.swipeRefreshView, viewModel);
        viewModel.myAppointmentLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    public class MyBookingAdapter extends BaseQuickAdapter<MyAppointmentBean, BaseViewHolder> {
        public MyBookingAdapter() {
            super(R.layout.item_check_look_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, MyAppointmentBean item) {
            ItemCheckLookLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickListener);
            binding.setBean(item);
            binding.setUrl(item.getUu_image());
            binding.executePendingBindings();
            //tv_sex性别
            String uu_gender = item.getUu_gender();
            binding.tvSex.setBackgroundResource(uu_gender.equals("1") ? R.drawable.rect_btn_blue_background : R.drawable.rect_btn_red_background);
            Drawable drawable_n = getResources().getDrawable(uu_gender.equals("1") ? R.drawable.nan : R.drawable.nv);
            drawable_n.setBounds(0, 0, drawable_n.getIntrinsicWidth(), drawable_n.getIntrinsicHeight());
            binding.tvSex.setCompoundDrawables(drawable_n, null, null, null);

            //预约人 姓名 手机号 时间 内容
            binding.elName.setContent(item.getAppointment_name());
            binding.elPhone.setContent(item.getAppointment_phone());
            binding.elTime.setContent(item.getAppointed_time());
            binding.elContent.setContent(item.getContent());
        }
    }

    private ClickEventHandler<MyAppointmentBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.cl_info:  //  用户主页
                UserHomePageActivity.start(getContext(), bean.getUser_id());
                break;
        }
    };
}