package com.chengzi.app.ui.mine.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityMyBookingBindingImpl;
import com.chengzi.app.databinding.ItemMyBookingLayoutBindingImpl;
import com.chengzi.app.dialog.MessageDialogBuilder;
import com.chengzi.app.ui.mine.bean.MyAppointmentBean;
import com.chengzi.app.ui.mine.viewmodel.MyBookingModel;

import java.util.ArrayList;

/**
 * 我的预约 --->普通用户
 *
 * @ClassName:MyBookingActity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/8 0008   14:26
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/8 0008 handongkeji All rights reserved.
 */
public class MyBookingActivity extends BaseActivity<ActivityMyBookingBindingImpl, MyBookingModel> {
    private PagingLoadHelper helper;
    private MyBookingAdapter adapter;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_my_booking;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        adapter = new MyBookingAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
//        binding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        mViewModel.myAppointmentLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        //取消预约
        mViewModel.cancelAppointmentLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean.getStatus() == Sys.SUCCESS_STATUS) {
                helper.onPulldown();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.start();
    }

    public class MyBookingAdapter extends BaseQuickAdapter<MyAppointmentBean, BaseViewHolder> {
        public MyBookingAdapter() {
            super(R.layout.item_my_booking_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, MyAppointmentBean item) {
            ItemMyBookingLayoutBindingImpl binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            binding.setBean(item);
            binding.setUrl(item.getUu_image());
            binding.executePendingBindings();
            //预约人 姓名 手机号 时间
            binding.elName.setContent(item.getAppointment_name());
            binding.elPhone.setContent(item.getAppointment_phone());
            binding.elTime.setContent(item.getAppointed_time());
            //区分是医生还是医院-->医生显示职称和评分
            binding.tvZhicheng.setVisibility(item.getType().equals(String.valueOf(Sys.TYPE_DOCTOR)) ? View.VISIBLE : View.GONE);
        }
    }

    private ClickEventHandler<MyAppointmentBean> clickEventHandler = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_cancellation:  //取消预约
                new MessageDialogBuilder(this)
                        .setMessage("确认取消预约？")
                        .setSureListener(v -> {
//                                toast("取消预约" + (mViewModel.selectedProvincePos.get() + 1))
                                    showLoading(Sys.LOADING);
                                    mViewModel.cancelAppointment(bean.getId());
                                }
                        )
                        .show();
                break;
        }
    };
}