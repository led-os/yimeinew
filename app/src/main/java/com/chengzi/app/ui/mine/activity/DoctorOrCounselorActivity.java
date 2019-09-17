package com.chengzi.app.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEvent;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityDoctorOrCounselorBindingImpl;
import com.chengzi.app.databinding.ItemDoctorOrCounselorLayoutBinding;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;
import com.chengzi.app.ui.mine.bean.DoctorListBean;
import com.chengzi.app.utils.NimUtils;

import java.util.ArrayList;

/**
 * 医生2/咨询师3 管理
 *
 * @ClassName:DoctorOrCounselorActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/11 0011   15:17
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/11 0011 handongkeji All rights reserved.
 */

public class DoctorOrCounselorActivity extends BaseActivity<ActivityDoctorOrCounselorBindingImpl, DoctorOrCounselorViewModel> {

    private PagingLoadHelper helper;

    //2医生 3咨询师
    private int type;

    public static void start(Context context, int type) {
        context.startActivity(new Intent(context, DoctorOrCounselorActivity.class)
                .putExtra("type", type)
        );
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_or_counselor;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type", 2);
        mViewModel.type.set(type);
        mBinding.topBar.setCenterText(type == 2 ? "医生管理" : "咨询师管理");

        DoctorOrCounselorManageAdapter adapter = new DoctorOrCounselorManageAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);

        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        mViewModel.listMutableLiveData.observe(this, objects -> {
            if (objects != null && objects.size() > 0) {
                helper.onComplete(objects);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
        helper.start();

        //同意/拒绝
        mViewModel.bindReviewLiveData.observe(this, responseBean -> {
            dismissLoading();
            if (responseBean != null && responseBean.isSuccess()) {
                helper.onPulldown();
            }
        });
        //解绑
        mViewModel.bindClearLiveData.observe(this, bean -> {
            dismissLoading();
            helper.onPulldown();
            NimUtils.sendUnBindMessage(bean.getYunxin_accid());
        });
    }

    public class DoctorOrCounselorManageAdapter extends BaseQuickAdapter<DoctorListBean, BaseViewHolder> {
        public DoctorOrCounselorManageAdapter() {
            super(R.layout.item_doctor_or_counselor_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorListBean item) {
            ItemDoctorOrCounselorLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(itemClickListener);
            binding.setBean(item);
            binding.setUrl(item.getImage());
            binding.setType(mViewModel.type.get());
            binding.executePendingBindings();


            // //'状态 0-待审核 1-已通过 2-已拒绝 ',
            String status = item.getStatus();
            if (status.equals("0")) {         //待审核显示  接受/拒绝
                binding.tvAccept.setVisibility(View.VISIBLE);
                binding.tvRefuse.setVisibility(View.VISIBLE);
                binding.tvUnbind.setVisibility(View.GONE);
            } else if (status.equals("1")) {  //同意显示 拒绝
                //绑定状态 1-已绑定 2-已解绑',
                String type = item.getType();
                if (type.equals("1")) {
                    binding.tvAccept.setVisibility(View.GONE);
                    binding.tvRefuse.setVisibility(View.GONE);
                    binding.tvUnbind.setVisibility(View.VISIBLE);
                } else {
                    binding.tvAccept.setVisibility(View.GONE);
                    binding.tvRefuse.setVisibility(View.GONE);
                    binding.tvUnbind.setVisibility(View.GONE);
                }
            } else {                        //已拒绝 都隐藏
                binding.tvAccept.setVisibility(View.GONE);
                binding.tvRefuse.setVisibility(View.GONE);
                binding.tvUnbind.setVisibility(View.GONE);
            }
        }
    }

    private ClickEventHandler<DoctorListBean> itemClickListener = (view, bean) -> {

        if (!ClickEvent.shouldClick(view)) {
            return;
        }

        //列表中 医生/咨询师userid
        String user_ids = bean.getUser_id();
        switch (view.getId()) {
            case R.id.cl_info:  //  医生/咨询师 主页
                if (type == 2) {    //医生主页
                    DoctorHomePageActivity.startDoctor(this, user_ids);
                } else {           // 咨询师主页
                    DoctorHomePageActivity.startCounselor(this, user_ids);
                }
                break;
            case R.id.tv_accept:  //  接受  1-已通过 2-已拒绝
                showLoading(Sys.LOADING);
                mViewModel.bindReview(user_ids, "1");
                break;
            case R.id.tv_refuse:  //  拒绝
                showLoading(Sys.LOADING);
                mViewModel.bindReview(user_ids, "2");
                break;
            case R.id.tv_unbind:  //  解绑

                showLoading(Sys.LOADING);
                mViewModel.bindClear(bean);
                break;
        }
    };
}