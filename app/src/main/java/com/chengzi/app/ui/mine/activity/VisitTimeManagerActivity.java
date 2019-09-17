package com.chengzi.app.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.base.BaseActivity;
import com.chengzi.app.R;
import com.chengzi.app.adapter.DateItemAdapter;
import com.chengzi.app.adapter.DateItemAdapter.DateBean;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ActivityVisitTimeManagerBinding;
import com.chengzi.app.ui.mine.bean.AppointmentTimeManageBean;
import com.chengzi.app.ui.mine.viewmodel.VisitTimeManagerViewModel;
import com.chengzi.app.utils.DateBeanFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 坐诊信息管理
 *
 * @ClassName:VisitTimeManagerActivity
 * @PackageName:com.yimei.app.ui.mine.activity
 * @Create On 2019/4/9 0009   11:23
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */

public class VisitTimeManagerActivity extends BaseActivity<ActivityVisitTimeManagerBinding, VisitTimeManagerViewModel> {


    public static VisitTimeManagerActivity this_a;
    private DateItemAdapter dateItemAdapter;

    /**
     * @param context
     * @param targetUserId 查看的医生user_id
     */
    public static void start(Context context, String targetUserId) {
        context.startActivity(new Intent(context, VisitTimeManagerActivity.class)
                .putExtra(Sys.EXTRA_TARGET_ID, targetUserId)
        );
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_visit_time_manager;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {


        this_a = this;
        String targetUserId = getIntent().getStringExtra(Sys.EXTRA_TARGET_ID);
        mViewModel.setTargetUserId(targetUserId);

        // 可设置预约
        if (!TextUtils.isEmpty(targetUserId) && targetUserId.equals(AccountHelper.getUserId())) {
            mBinding.tvIsVisit.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvIsVisit.setVisibility(View.GONE);
        }

        mViewModel.appointmentTimeManageLiveData.observe(this, list -> {
            dismissLoading();
            if (list != null) {
                List<DateBean> beans = dateItemAdapter.getData();
                for (AppointmentTimeManageBean timeManageBean : list) {
                    AppointmentTimeManageBean.Date date = timeManageBean.getDate();
                    for (DateBean bean : beans) {
                        if (date.getYear() == bean.getYear() && date.getMonth() == bean.getMonth() && date.getDate() == bean.getDay()) {
                            bean.setAm(timeManageBean.getAm() == 1);
                            bean.setPm(timeManageBean.getPm() == 1);
                            break;
                        }
                    }
                }
                dateItemAdapter.notifyDataSetChanged();
            }
        });

        setDate();
        setTime();
        setUpAppomentData();

    }

    //上午下午
    private void setTime() {
        TBAdapter adapter1 = new TBAdapter();
        mBinding.recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView1.setAdapter(adapter1);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            strings.add("");
        }
        adapter1.setNewData(strings);
    }

    //日期 and 星期一到星期日
    private void setDate() {

        //星期
        String[] titles = new String[]{"一", "二", "三", "四", "五", "六", "日"};
        List<DateBean> list = new ArrayList<>();
        for (String title : titles) {
            list.add(new DateBean(title));
        }
        //周一到周日-->user_id为空，没用
        mBinding.recyclerViewTitle.setAdapter(new DateItemAdapter(this, list, ""));

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        mBinding.tvMonthYear.setText(year + "年" + DateBeanFactory.MONTHS[month] + "月");


    }

    private void setUpAppomentData() {

        List<DateBean> dateBeans = DateBeanFactory.generateData();
        DateBean startDate = dateBeans.get(0);
        DateBean endDate = dateBeans.get(dateBeans.size() - 1);
        String startTime = startDate.getYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDay();
        String endTime = endDate.getYear() + "-" + (endDate.getMonth() + 1) + "-" + endDate.getDay();

        mViewModel.appointmentTimeManage(startTime, endTime);

        dateItemAdapter = new DateItemAdapter(this, dateBeans, mViewModel.getTargetUserId());
        //日期
        mBinding.recyclerViewTime.setAdapter(dateItemAdapter);
//        mBinding.recyclerViewTime.setAdapter(new DateItemAdapter(this, timeManageEntities));
        mBinding.recyclerViewTime.setOverScrollMode(View.OVER_SCROLL_NEVER);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this_a != null) {
            this_a = null;
        }
    }

    public void refresh() {

        DateBean startDate = dateItemAdapter.getData().get(0);
        DateBean endDate = dateItemAdapter.getData().get(dateItemAdapter.getData().size() - 1);
        String startTime = startDate.getYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDay();
        String endTime = endDate.getYear() + "-" + (endDate.getMonth() + 1) + "-" + endDate.getDay();
        mViewModel.appointmentTimeManage(startTime, endTime);
    }


    //上午下午adapter  -->表格固定死的
    public class TBAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public TBAdapter() {
            super(R.layout.item_tb_layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
        }
    }
}
