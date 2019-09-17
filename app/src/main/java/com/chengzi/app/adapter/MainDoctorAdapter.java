package com.chengzi.app.adapter;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.utils.ClickEventHandler;
import com.chengzi.app.R;
import com.chengzi.app.databinding.ItemMainDoctorLayoutBinding;
import com.chengzi.app.ui.home.bean.DoctorBean;
import com.chengzi.app.ui.homepage.doctor.activity.DoctorHomePageActivity;

/**
 * 首页--频道专区--医生
 *
 * @ClassName:MainDoctorAdapter
 * @PackageName:com.yimei.app.adapter
 * @Create On 2019/4/3 0003   14:45
 * @Site:http://www.handongkeji.com
 * @author:zhouhao
 * @Copyrights 2019/4/3 0003 handongkeji All rights reserved.
 */
public class MainDoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder>
        implements ClickEventHandler<DoctorBean> {

    private int userType;   //  2 医生 ，3 咨询师
    private int average;    // 1 显示平均分  机构主页的医生和咨询师

    public void setAverage(int average) {
        this.average = average;
    }

    public MainDoctorAdapter(int userType) {
        super(R.layout.item_main_doctor_layout);
        this.userType = userType;
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorBean item) {
        ItemMainDoctorLayoutBinding binding = DataBindingUtil.bind(helper.itemView);
        binding.setBean(item);

        binding.setAverage(average);
        binding.setListener(handler == null ? this : handler);
        binding.executePendingBindings();
    }

    private ClickEventHandler<DoctorBean> handler;

    public void setHandler(ClickEventHandler<DoctorBean> handler) {
        this.handler = handler;
    }

    @Override
    public void handleClick(View view, DoctorBean bean) {

        if (userType == 2) {
            DoctorHomePageActivity.startDoctor(view.getContext(), bean.getId(), bean.getPromotion_id());

        } else if (userType == 3) {
            DoctorHomePageActivity.startCounselor(view.getContext(), bean.getId(), bean.getPromotion_id());
        }
    }

}