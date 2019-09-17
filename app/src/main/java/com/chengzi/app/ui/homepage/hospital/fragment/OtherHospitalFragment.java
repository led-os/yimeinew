package com.chengzi.app.ui.homepage.hospital.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.FragmentOtherHospitalBinding;
import com.chengzi.app.databinding.ItemOtherHospitalLayoutBinding;
import com.chengzi.app.ui.homepage.RefreshableFragment;
import com.chengzi.app.ui.homepage.bean.HospitalHomeInfoBean;
import com.chengzi.app.ui.homepage.hospital.viewmodel.HospitalHomePageViewModel;
import com.chengzi.app.ui.homepage.hospital.activity.HospitalHomePageActivity;

import java.util.List;

/**
 * 医院主页--相关机构展示
 *
 * @ClassName:OtherHospitalFragment
 * @PackageName:com.yimei.app.ui.homepage.hospital.fragment
 * @Create On 2019/4/19 0019   13:58
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/19 0019 handongkeji All rights reserved.
 */
public class OtherHospitalFragment extends RefreshableFragment<FragmentOtherHospitalBinding, HospitalHomePageViewModel> {

    public OtherHospitalFragment() {
    }

    public static OtherHospitalFragment newInstance(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(OtherHospitalFragment.class.getSimpleName());
        if (fragment == null) {
            android.os.Bundle args = new Bundle();
            fragment = new OtherHospitalFragment();
            fragment.setArguments(args);
        }
        return (OtherHospitalFragment) fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_other_hospital;
    }

    @Override
    protected boolean initalizeViewModelFromActivity() {
        return true;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        OtherHospitalAdapter adapter = new OtherHospitalAdapter();
        binding.recyclerOtherOrg.setAdapter(adapter);

        viewModel.hospitalDetailLive.observe(this, bean -> {
            List<HospitalHomeInfoBean.MoreHospitalBean> more_hospital = bean.getMore_hospital();
            adapter.setNewData(more_hospital);
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean isRefreshFinished() {
        return true;
    }

    private static class OtherHospitalAdapter extends BaseQuickAdapter<HospitalHomeInfoBean.MoreHospitalBean,
            BaseViewHolder> implements ClickEventHandler<HospitalHomeInfoBean.MoreHospitalBean> {

        public OtherHospitalAdapter() {
            super(R.layout.item_other_hospital_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, HospitalHomeInfoBean.MoreHospitalBean item) {
            ItemOtherHospitalLayoutBinding itemBinding = DataBindingUtil.bind(helper.itemView);
            itemBinding.setBean(item);
            itemBinding.setListener(this);
            itemBinding.executePendingBindings();
        }

        @Override
        public void handleClick(View view, HospitalHomeInfoBean.MoreHospitalBean bean) {
            HospitalHomePageActivity.start(view.getContext(), bean.getId());
        }
    }
}
