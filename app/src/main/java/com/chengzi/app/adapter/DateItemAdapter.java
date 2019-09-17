package com.chengzi.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blankj.utilcode.util.ToastUtils;
import com.handong.framework.account.AccountHelper;
import com.handong.framework.api.Api;
import com.handong.framework.base.ResponseBean;
import com.chengzi.app.R;
import com.chengzi.app.constants.Sys;
import com.chengzi.app.databinding.ItemDateTitleLayoutBinding;
import com.chengzi.app.dialog.SelectDialog;
import com.chengzi.app.ui.mine.activity.VisitTimeManagerActivity;
import com.chengzi.app.ui.mine.bean.TimeManageBean;
import com.chengzi.app.ui.mine.model.MineDoctorOrCounselorService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DateItemAdapter extends BaseAdapter {

    private List<DateBean> data = new ArrayList<>();
    private Context context;
    private String user_id;

    //周一-----周日
    //user_id ->如果传过来的user_id等于本地我的user_id        -->权限：查看，编辑
    //          如果是其他医生的user_id(医生主页 不是自己的id)--->权限：查看
    public DateItemAdapter(Context context, List<DateBean> data, String user_id) {
        this.context = context;
        this.data = data == null?new ArrayList<>():new ArrayList<>(data);
        this.user_id = user_id;
    }

    public List<DateBean> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DateBean getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemDateTitleLayoutBinding binding;
        if (convertView == null) {              //item_date_title_layout.xml
            binding = ItemDateTitleLayoutBinding.inflate(LayoutInflater
                    .from(parent.getContext()), parent, false);
        } else {
            binding = (ItemDateTitleLayoutBinding) convertView.getTag(R.id.dataBinding);
        }
        DateBean dateBean = data.get(position);

        binding.text.setText(dateBean.text);
        binding.text.setTag(dateBean);
        String text = dateBean.text;
        if (text.equals("一") || text.equals("二") || text.equals("三") || text.equals("四")
                || text.equals("五") || text.equals("六") || text.equals("日")) {
        } else {
            binding.viewTop.setVisibility(View.VISIBLE);
            binding.viewBottom.setVisibility(View.VISIBLE);
            binding.viewTop.setText(dateBean.isAm() ? "可预约" : "");
            binding.viewTop.setBackgroundResource(dateBean.isAm() ? R.drawable.rect_gray1_bottom_border : R.drawable.rect_gray1_border);
            binding.viewBottom.setText(dateBean.isPm() ? "可预约" : "");
            binding.viewBottom.setBackgroundResource(dateBean.isPm() ? R.drawable.rect_gray1_bottom_border : R.drawable.rect_gray1_border);
        }

        String itemTime = dateBean.getYear() + "-" + (dateBean.getMonth() + 1) + "-" + dateBean.getDay();

        //传过来的user_id是医生自己的user_id才可编辑 否则不可编辑不做处理
        if (TextUtils.equals(AccountHelper.getUserId(), user_id)) {
            //上午
            binding.viewTop.setOnClickListener(v -> {
                if (dateBean.isDisable()) {
                    ToastUtils.showShort("只能设置当前时间往后的时间");
                    return;
                }
                boolean am = dateBean.isAm();
                new SelectDialog(context)
                        .setTvText1("可预约")
                        .setText1Listener(v1 -> {
                            binding.viewTop.setText("可预约");
                            if (!am) {   //如果上午是不可预约
                                setObserver(itemTime, "am");
                            }
                        })
                        .setTvText2("不可预约")
                        .setText2Listener(v1 -> {
                            if (am) {   //如果上午是不可预约
                                setObserver(itemTime, "am");
                            }

                        }).show();
            });
            //下午
            binding.viewBottom.setOnClickListener(v -> {
                if (dateBean.isDisable()) {
                    ToastUtils.showShort("只能设置当前时间往后的时间");
                    return;
                }
                boolean pm = dateBean.isPm();
                new SelectDialog(context)
                        .setTvText1("可预约")
                        .setText1Listener(v1 -> {
                            binding.viewBottom.setText("可预约");
                            if (!pm) {   //如果下午是不可预约
                                setObserver(itemTime, "pm");
                            }

                        })
                        .setTvText2("不可预约")
                        .setText2Listener(v1 -> {
                            if (pm) {
                                setObserver(itemTime, "pm");
                            }
                        })
                        .show();
            });
        }
        return binding.getRoot();
    }

    /**
     * //处理点击事件
     *
     * @param itemTime 点击的当前日期
     * @param amOrpm   上午am/下午pm
     */
    private void setObserver(String itemTime, String amOrpm) {
        String userId = AccountHelper.getUserId();
        Api.getApiService(MineDoctorOrCounselorService.class).AppointmentTimeManage(userId, userId, itemTime, amOrpm)
                .subscribe(new Observer<ResponseBean<TimeManageBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBean<TimeManageBean> bean) {
                        TimeManageBean data = bean.getData();
                        if (bean.getStatus() == Sys.SUCCESS_STATUS && data != null && data.isIs_update().equals("1")) {
                            ToastUtils.showShort("修改成功!");
                            //刷新列表 (每周的同一天的上下午是相同的)
                            VisitTimeManagerActivity.this_a.refresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static class DateBean implements Comparable, Serializable {

        public String text;
        private boolean disable = true;
        private int year;
        private int month;
        private int day;
        private String weekDay; //星期
        private boolean am; //上午
        private boolean pm; //下午

        public DateBean(String text) {
            this.text = text;
        }

        public DateBean(boolean disable, int year, int month, int day, String text, String weekDay, boolean am, boolean pm) {   //
            this.disable = disable;
            this.year = year;
            this.month = month;
            this.day = day;
            this.text = text;
            this.weekDay = weekDay;
            this.am = am;
            this.pm = pm;
        }

        public boolean isDisable() {
            return disable;
        }


        public void setDisable(boolean disable) {
            this.disable = disable;
        }

        public String getText() {
            return text == null ? "" : text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            if (o == null || !(o instanceof DateBean)) {
                return -1;
            }
            DateBean dateBean = (DateBean) o;

            if (year == dateBean.year && month == dateBean.month && day == dateBean.day) {
                return 0;
            }
            if (year == dateBean.year && month == dateBean.month) {
                return day - dateBean.day;
            }
            if (year == dateBean.year) {
                return month - dateBean.month;
            }
            return year - dateBean.year;

        }

        public int compare(int year, int month, int day) {
            if (this.year == year && this.month == month && this.day == day) {
                return 0;
            }
            if (this.year == year && this.month == month) {
                return this.day - day;
            }
            if (this.year == year) {
                return this.month - month;
            }
            return this.year - year;
        }

        public String getWeekDay() {
            return weekDay;
        }

        public void setWeekDay(String weekDay) {
            this.weekDay = weekDay;
        }

        public boolean isAm() {
            return am;
        }

        public void setAm(boolean am) {
            this.am = am;
        }

        public boolean isPm() {
            return pm;
        }

        public void setPm(boolean pm) {
            this.pm = pm;
        }
    }
}