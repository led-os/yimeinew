package com.chengzi.app.ui.mine.activity.beautifulprofile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.handong.framework.utils.ClickEventHandler;
import com.nevermore.oceans.pagingload.PagingLoadHelper;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityBeautifulProfileBindingImpl;
import com.chengzi.app.databinding.ItemBeautifulProfileLayoutBinding;
import com.chengzi.app.databinding.ItemPicLayoutBinding;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;
import com.chengzi.app.ui.mine.bean.RecordListBean;
import com.chengzi.app.ui.mine.viewmodel.BeautifulProfileViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 美丽档案
 *
 * @ClassName:BeautifulProfileActivity
 * @PackageName:com.yimei.app.ui.mine.activity.beautifulprofile
 * @Create On 2019/4/4 0004   18:02
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class BeautifulProfileActivity extends BaseActivity<ActivityBeautifulProfileBindingImpl, BeautifulProfileViewModel> {

    private PagingLoadHelper helper;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_beautiful_profile;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        //美丽档案
        BeautifulProfileAdapter adapter = new BeautifulProfileAdapter(clickListener);
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        helper = new PagingLoadHelper(mBinding.swipeRefreshView, mViewModel);
        mViewModel.recordListLiveData.observe(this, recordListBean -> {
            dismissLoading();
            if (recordListBean != null && recordListBean.size() > 0) {
                helper.onComplete(recordListBean);
            } else {
                helper.onComplete(new ArrayList<>());
            }
        });
    }

    private ClickEventHandler<RecordListBean> clickListener = (view, bean) -> {
        switch (view.getId()) {
            case R.id.tv_pic:   //美丽相片 tv_pic
                ArrayList<String> strings = (ArrayList<String>) bean.getImage();
                startActivity(new Intent(this, BeautifulPhotoAlbumActivity.class)
                        .putExtra("order_id", bean.getOrder_id())
                        .putStringArrayListExtra("strings", strings)
                );
                break;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        helper.start();
    }

    //参与用户信息
    public class BeautifulProfileAdapter extends BaseQuickAdapter<RecordListBean, BaseViewHolder> {
        private ClickEventHandler<RecordListBean> clickEventHandler;

        public BeautifulProfileAdapter(ClickEventHandler<RecordListBean> clickEventHandler) {
            super(R.layout.item_beautiful_profile_layout);
            this.clickEventHandler = clickEventHandler;
        }

        @Override
        protected void convert(BaseViewHolder helper, RecordListBean item) {
            ItemBeautifulProfileLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setListener(clickEventHandler);
            //绑定实体类 医生头像 咨询师头像 医院头像
            binding.setBean(item);
            //医生
            RecordListBean.HosipitalInfoEntity doctor_info = item.getDoctor_info();
            if (doctor_info != null) {
                binding.llDoctor.setVisibility(View.VISIBLE);
                binding.setUrlDoctor(doctor_info.getImage());
            } else {
                binding.llDoctor.setVisibility(View.GONE);
            }
            //咨询师
            RecordListBean.HosipitalInfoEntity counselling_info = item.getCounselling_info();
            if (counselling_info != null) {
                binding.llCounselor.setVisibility(View.VISIBLE);
                binding.setUrlCounselor(counselling_info.getImage());
            } else {
                binding.llCounselor.setVisibility(View.GONE);
            }
            //医院
            RecordListBean.HosipitalInfoEntity hosipital_info = item.getHosipital_info();
            if (hosipital_info != null) {
                binding.llHosipital.setVisibility(View.VISIBLE);
                binding.setUrlHospital(hosipital_info.getImage());
            } else {
                binding.llCounselor.setVisibility(View.GONE);
            }
            binding.executePendingBindings();

            PicAdapter adapter = new PicAdapter();
            binding.mRecyclerView.setLayoutManager(new GridLayoutManager(BeautifulProfileActivity.this, 3));
            binding.mRecyclerView.setAdapter(adapter);
            List<String> image = item.getImage();
            adapter.setNewData(image);
            //看大图
            adapter.setOnItemClickListener((BaseQuickAdapter adapter1, View view, int position) -> {
                List<String> images = adapter.getData();
                PictureSwitcherActivity.start(BeautifulProfileActivity.this, (ArrayList<String>) images, position);
            });
        }
    }

    public class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public PicAdapter() {
            super(R.layout.item_pic_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ItemPicLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
            binding.setUrl(item);
            binding.executePendingBindings();
        }
    }
}