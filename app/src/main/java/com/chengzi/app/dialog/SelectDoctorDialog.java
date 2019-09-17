package com.chengzi.app.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogSelectDoctorLayoutBinding;
import com.chengzi.app.databinding.ItemSelectDoctorLayoutBinding;
import com.chengzi.app.ui.home.bean.DoctorBean;

import java.util.List;

public class SelectDoctorDialog extends BottomDialog implements View.OnClickListener {

    private DialogSelectDoctorLayoutBinding mBinding;

    private int userType;   //  2 医生     3  咨询师
    private SelectDoctorAdapter adapter;

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_select_doctor_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setListener(this);

        adapter = new SelectDoctorAdapter();
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        adapter.setOnItemClickListener((adapter1, view1, position) -> {
            DoctorBean item = adapter.getItem(position);
            if (listener != null) {
                listener.onSelected(item);
            }
            dismissAllowingStateLoss();
        });
        adapter.setNewData(list);
        mBinding.viewSwitcher.setDisplayedChild(1);

    }

    private List<DoctorBean> list;

    public void setData(List<DoctorBean> list) {
        this.list = list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }

    private class SelectDoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder> {

        public SelectDoctorAdapter() {
            super(R.layout.item_select_doctor_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorBean item) {
            ItemSelectDoctorLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.executePendingBindings();
        }
    }

    private OnSelectedListener listener;

    public void setListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnSelectedListener {
        void onSelected(DoctorBean object);
    }
}
