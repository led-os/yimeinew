package com.chengzi.app.dialog;

import android.arch.lifecycle.MutableLiveData;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogAddToCarSelectDoctorLayoutBinding;
import com.chengzi.app.databinding.ItemAddcarDoctorLayoutBinding;
import com.chengzi.app.ui.home.bean.DoctorBean;

import java.util.List;

public class AddToCarSelectDoctorDialog extends BottomDialog implements View.OnClickListener {

    private DialogAddToCarSelectDoctorLayoutBinding mBinding;
    private MutableLiveData<List<DoctorBean>> doctorLive = new MutableLiveData<>();
    private MutableLiveData<List<DoctorBean>> counselorLive = new MutableLiveData<>();
    private DoctorAdapter doctorAdapter;
    private DoctorAdapter counselorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.dialog_add_to_car_select_doctor_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setListener(this);

        doctorAdapter = new DoctorAdapter();
        mBinding.recyclerDoctor.setAdapter(doctorAdapter);
        doctorLive.observe(this, doctorBeans -> {
            doctorAdapter.setNewData(doctorBeans);
        });

        doctorAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            DoctorBean item = doctorAdapter.getItem(position);
            switch (view1.getId()) {
                case R.id.btn_select:
                    doctorAdapter.selectedId.set(item.getId());
                    break;
            }
        });

        counselorAdapter = new DoctorAdapter();
        mBinding.recyclerCounselor.setAdapter(counselorAdapter);
        counselorLive.observe(this, doctorBeans -> {
            counselorAdapter.setNewData(doctorBeans);
        });
        counselorAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            DoctorBean item = counselorAdapter.getItem(position);
            switch (view1.getId()) {
                case R.id.btn_select:
                    counselorAdapter.selectedId.set(item.getId());
                    break;
            }
        });
    }

    public void setDoctors(List<DoctorBean> doctors) {
        doctorLive.postValue(doctors);
    }

    public void setCounselors(List<DoctorBean> counselors) {
        counselorLive.postValue(counselors);
    }

    public void setHospitalName(String hospitalName) {
        mBinding.tvHospitalName.setText(hospitalName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_add:
                if (TextUtils.isEmpty(doctorAdapter.selectedId.get()) &&
                        TextUtils.isEmpty(counselorAdapter.selectedId.get())) {
                    ToastUtils.showShort("至少选择一个医生或一个咨询师");
                    return;
                }
                if (listener != null) {
                    listener.onSelected(doctorAdapter.selectedId.get(), counselorAdapter.selectedId.get());
                }
                dismissAllowingStateLoss();
                break;
        }

    }

    private class DoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder> {

        public final ObservableField<String> selectedId = new ObservableField<>();

        public DoctorAdapter() {
            super(R.layout.item_addcar_doctor_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, DoctorBean item) {
            ItemAddcarDoctorLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
            layoutBinding.setBean(item);
            layoutBinding.setSelectedId(selectedId);

            helper.addOnClickListener(R.id.btn_select);

            layoutBinding.executePendingBindings();
        }
    }

    private OnSelectedListener listener;

    public void setListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnSelectedListener {
        void onSelected(String doctorId, String counselorId);
    }
}
