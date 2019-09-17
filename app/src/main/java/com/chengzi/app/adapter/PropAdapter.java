package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemDoctorHomepagePropLayoutBinding;

public class PropAdapter extends BaseQuickAdapter<PropAdapter.Prop, BaseViewHolder> {

    public PropAdapter() {
        super(R.layout.item_doctor_homepage_prop_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, Prop item) {
        ItemDoctorHomepagePropLayoutBinding layoutBinding = DataBindingUtil.bind(helper.itemView);
        layoutBinding.setBean(item);
        layoutBinding.executePendingBindings();
    }

    public static class Prop {
        public String propName;
        public String num;

        public Prop(String propName, String num) {
            this.propName = propName;
            this.num = num;
        }

        public String getPropName() {
            return propName;
        }

        public void setPropName(String propName) {
            this.propName = propName;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

}
