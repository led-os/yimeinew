package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemDoctorListLayoutBinding;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;

public class DoctorListAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder>
        implements ClickEventHandler<DoctorBean> {

    private int userType;
    //
    private int begoodat = 0;

    public DoctorListAdapter(int userType) {
        super(R.layout.item_doctor_list_layout);
        this.userType = userType;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorBean item) {
        ItemDoctorListLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);
        binding.setUserType(userType);
        binding.setBegoodat(begoodat);
        binding.setListener(this);
        binding.executePendingBindings();
    }

    @Override
    public void handleClick(View view, DoctorBean bean) {

        if (userType == 2) {
            DoctorHomePageActivity.startDoctor(view.getContext(), bean.getId());
        } else if (userType == 3) {
            DoctorHomePageActivity.startCounselor(view.getContext(), bean.getId());
        }
    }

    //1说明显示-begoodat 其他-begoodatString
    public void setBeGoodAt(int begoodat) {
        this.begoodat = begoodat;
    }
}
