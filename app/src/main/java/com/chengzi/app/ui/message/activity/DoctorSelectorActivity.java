package com.chengzi.app.ui.message.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ActivityDoctorSelectorBinding;
import com.chengzi.app.databinding.ItemDoctorSelectorLayoutBinding;
import com.chengzi.app.im.session.extension.OnlineCaseAttachment;
import com.chengzi.app.ui.message.bean.AttentionDoctorBean;
import com.chengzi.app.ui.message.viewmodel.DoctorSelectorViewModel;

import java.io.Serializable;

public class DoctorSelectorActivity extends BaseActivity<ActivityDoctorSelectorBinding, DoctorSelectorViewModel> {

    public static final String RESULT_DATA = "RESULT_DATA"; // 返回结果

    @Override
    public int getLayoutRes() {
        return R.layout.activity_doctor_selector;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

        parseIntent();

        AttentinDoctorAdapter adapter = new AttentinDoctorAdapter();
        mBinding.swipeRefreshView.setAdapter(adapter);
        mBinding.swipeRefreshView.setPullupEnable(false);
        mBinding.swipeRefreshView.setPulldownEnable(false);

        mViewModel.attentionDoctorLive.observe(this, attentionDoctorBeans -> {
            adapter.setNewData(attentionDoctorBeans);
        });

        mViewModel.getAttentionDoctor();
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            AttentionDoctorBean item = adapter.getItem(position);
            if (!TextUtils.isEmpty(mViewModel.getCaseUserId())) {
                mViewModel.caseForwardLive.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        mViewModel.caseForwardLive.removeObserver(this);
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_DATA, item.getYunxin_id());
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
                showLoading("");
                mViewModel.caseForward(item.getCover_id());
            }else{
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, item.getYunxin_id());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });
        mViewModel.getAttentionDoctor();

    }

    private void parseIntent() {
        Serializable extra = getIntent().getSerializableExtra("extra");
        if (extra == null) {
            return;
        }
        if (extra instanceof OnlineCaseAttachment) {
            OnlineCaseAttachment onlineCaseAttachment = (OnlineCaseAttachment) extra;
            String userId = onlineCaseAttachment.getCaseUserId();
            mViewModel.setCaseUserId(userId);
        }
    }

    private class AttentinDoctorAdapter extends BaseQuickAdapter<AttentionDoctorBean, BaseViewHolder> {

        public AttentinDoctorAdapter() {
            super(R.layout.item_doctor_selector_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, AttentionDoctorBean item) {
            ItemDoctorSelectorLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }

}
