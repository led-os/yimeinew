package com.chengzi.app.dialog;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.DialogDiscountCouponLayoutBinding;
import com.chengzi.app.databinding.ItemDiscountCouponLayoutBinding;
import com.chengzi.app.ui.mine.bean.CouponListBean;

import java.util.List;

public class DiscountCouponDialog extends BottomDialog implements View.OnClickListener {

    private DialogDiscountCouponLayoutBinding mBinding;
    private CouponAdapter adapter;
    private CouponListBean selectedCoupon;

    public void setSelectedCoupon(CouponListBean selectedCoupon) {
        this.selectedCoupon = selectedCoupon;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_discount_coupon_layout, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setListener(this);
        adapter = new CouponAdapter();
        adapter.bindToRecyclerView(mBinding.recyclerView);
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

    }

    public void setData(List<CouponListBean> list) {
        if (selectedCoupon != null && list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (TextUtils.equals(selectedCoupon.getId(), list.get(i).getId())) {
                    adapter.selectPos.set(i);
                    break;
                }
            }
        }
        adapter.setNewData(list);
        mBinding.viewSwitcher.setDisplayedChild(1);
        if (adapter.getData().isEmpty()) {
            adapter.setEmptyView(R.layout.empty_discount_coupon_layout);
        }
    }

    public void setHospitalName(CharSequence text) {
        mBinding.tvHospitalName.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismissAllowingStateLoss();
                break;
        }
    }

    private OnCouponSelectedlistener selectedlistener;

    public void setSelectedlistener(OnCouponSelectedlistener selectedlistener) {
        this.selectedlistener = selectedlistener;
    }

    private class CouponAdapter extends BaseQuickAdapter<CouponListBean, BaseViewHolder> {

        public final ObservableInt selectPos = new ObservableInt(-1);

        public CouponAdapter() {
            super(R.layout.item_discount_coupon_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, CouponListBean item) {
            ItemDiscountCouponLayoutBinding layotBinding = DataBindingUtil.bind(helper.itemView);
            layotBinding.setBean(item);
            layotBinding.setListener(v -> {
                int adapterPosition = helper.getAdapterPosition();
                if (selectPos.get() == adapterPosition) {
                    selectPos.set(-1);
                    if (selectedlistener != null) {
                        selectedlistener.onCouponSelected(null);
                    }
                } else {
                    selectPos.set(adapterPosition);
                    if (selectedlistener != null) {
                        selectedlistener.onCouponSelected(item);
                    }
                }

                dismissAllowingStateLoss();
            });
            layotBinding.setPos(helper.getAdapterPosition());
            layotBinding.setSelectPos(selectPos);
            layotBinding.executePendingBindings();

        }

    }

    public interface OnCouponSelectedlistener {
        void onCouponSelected(CouponListBean object);
    }
}
