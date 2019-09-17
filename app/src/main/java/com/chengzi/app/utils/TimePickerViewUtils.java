package com.chengzi.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerViewUtils {

    public static TimePickerView initTimePicker(Context context, OnUtilsTimeSelectListener onUtilsTimeSelectListener) {//选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);

        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int, mouth_int - 1, day_int);

        return new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//
//                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
//                mBinding.elBirthday.setContent(getTime(date));
//
//                String year_str = formatter_year.format(date);
//                year = (int) Double.parseDouble(year_str);
//                String mouth_str = formatter_mouth.format(date);
//                month = (int) Double.parseDouble(mouth_str);
//                String day_str = formatter_day.format(date);
//                day = (int) Double.parseDouble(day_str);
                if (onUtilsTimeSelectListener != null) {
                    String time = getTime(date);
                    String year_str = formatter_year.format(date);
                    int year = (int) Double.parseDouble(year_str);
                    String mouth_str = formatter_mouth.format(date);
                    int month = (int) Double.parseDouble(mouth_str);
                    String day_str = formatter_day.format(date);
                    int day = (int) Double.parseDouble(day_str);

                    String timestamp = String.valueOf(date.getTime() / 1000);

                    onUtilsTimeSelectListener.onTimeSelect(time, year, month, day, timestamp);
                }

            }
        }).setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .setDividerColor(Color.RED)
                .setTextColorCenter(Color.RED)//设置选中项的颜色
                .setTextColorOut(Color.BLACK)//设置没有被选中项的颜色
                .setDate(selectedDate)      //默认时间
                .setRangDate(startDate, endDate)
                .isCyclic(false)//是否循环滚动
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setDecorView(null)
                .setSubCalSize(15)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(21)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();
    }

    public static TimePickerView showTimePicker(Context context, boolean start, OnUtilsTimeSelectListener onUtilsTimeSelectListener) {//选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);

        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String month_str = formatter_mouth.format(curDate);
        int month_int = (int) Double.parseDouble(month_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        if (start) {
            startDate.set(1900, 0, 1);
            endDate.set(year_int, month_int - 1, day_int - 1);
        } else {
            startDate.set(year_int, month_int - 1, day_int);
            endDate.set(year_int, 11, 31);
            endDate.add(Calendar.YEAR, 20);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (onUtilsTimeSelectListener != null) {
                    String time = format.format(date);
                    String year_str = formatter_year.format(date);
                    int year = (int) Double.parseDouble(year_str);
                    String mouth_str = formatter_mouth.format(date);
                    int month = (int) Double.parseDouble(mouth_str);
                    String day_str = formatter_day.format(date);
                    int day = (int) Double.parseDouble(day_str);

                    String timestamp = String.valueOf(date.getTime() / 1000);

                    onUtilsTimeSelectListener.onTimeSelect(time, year, month, day, timestamp);
                }

            }
        }).setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .setDividerColor(Color.RED)
                .setTextColorCenter(Color.RED)//设置选中项的颜色
                .setTextColorOut(Color.BLACK)//设置没有被选中项的颜色
                .setDate(selectedDate)      //默认时间
                .setRangDate(startDate, endDate)
                .isCyclic(false)//是否循环滚动
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setDecorView(null)
                .setSubCalSize(15)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setTitleBgColor(Color.WHITE)//标题背景颜色 Night mode
                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
                .setContentTextSize(21)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .build();
    }

    public static String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM月dd日");
        return format.format(date);
    }

    public interface OnUtilsTimeSelectListener {
        void onTimeSelect(String time, int year, int month, int day, String timestamp);
    }
}
