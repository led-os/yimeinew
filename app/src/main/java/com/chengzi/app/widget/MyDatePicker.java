package com.chengzi.app.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import com.chengzi.app.R;
import com.chengzi.app.utils.MyDateUtils;

import java.util.Calendar;

/**
 * Created by whf on 2018/2/26 0026.
 * 日期时间选择器 年月日时分秒
 */

public class MyDatePicker implements NumberPicker.OnValueChangeListener {
    private Context mContext;
    private Dialog mDialog;
    private PopupWindow mPopupWindow;
    private TimePickerInterface timePickerDialogInterface;
    private int mTag = 0;
    private int mYear, mDay, mMonth, mHour, mMinute;

    private NumberPicker npYear;
    private NumberPicker npMonth;
    private NumberPicker npDay;
    private NumberPicker npHour;
    private NumberPicker npMint;
    private LinearLayout llContent;

    //时间最小值
    int yearMin = 2018;
    int monthMin = 1;
    int dayMin = 1;
    int hourMin = 1;
    int minuteMin = 0;
    //时间最大值
    int yearMax = 2018;
    int monthMax = 1;
    int dayMax = 1;
    int hourMax = 1;
    int minuteMax = 0;

    long time;
    int dataNum;

    /**
     * 实例化控件
     *
     * @param context
     */
    public MyDatePicker(Context context) {
        mContext = context;
        timePickerDialogInterface = (TimePickerInterface) context;
    }

    /**
     * 初始化控件
     *
     * @param width
     * @return
     */
    private View initDateAndTimePicker(int width) {
        View inflate = LayoutInflater.from(mContext).inflate(
                R.layout.component_date, null);
        npYear = inflate.findViewById(R.id.np_year);
        npMonth = inflate.findViewById(R.id.np_month);
        npDay = inflate.findViewById(R.id.np_day);
        npHour = inflate.findViewById(R.id.np_hour);
        npMint = inflate.findViewById(R.id.np_mint);
        llContent = inflate.findViewById(R.id.ll_content);
        llContent.setLayoutParams(new LinearLayout.LayoutParams(width - 200, ViewGroup.LayoutParams.WRAP_CONTENT));

        npYear.setOnValueChangedListener(this);
        npMonth.setOnValueChangedListener(this);
        npDay.setOnValueChangedListener(this);
        npHour.setOnValueChangedListener(this);
        npMint.setOnValueChangedListener(this);

        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npMint.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入

        npYear.setMinValue(1900);//起始年份
        npYear.setMaxValue(2118);//结束年份
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npMint.setMinValue(0);
        npMint.setMaxValue(59);

        npYear.setWrapSelectorWheel(false); //设置循环滚动
        npMonth.setWrapSelectorWheel(false); //设置循环滚动
        npDay.setWrapSelectorWheel(false); //设置循环滚动
        npHour.setWrapSelectorWheel(false); //设置循环滚动
        npMint.setWrapSelectorWheel(false); //设置循环滚动

        Calendar c = Calendar.getInstance();
        yearMin = c.get(Calendar.YEAR);
        monthMin = c.get(Calendar.MONTH) + 1;
        dayMin = c.get(Calendar.DAY_OF_MONTH);
        hourMin = c.get(Calendar.HOUR_OF_DAY);
        minuteMin = c.get(Calendar.MINUTE);

        npYear.setValue(yearMin);
        npMonth.setValue(monthMin);
        npDay.setValue(dayMin);
        npHour.setValue(hourMin);
        npMint.setValue(minuteMin);

        mYear = yearMin;
        mDay = dayMin;
        mMonth = monthMin;
        mHour = hourMin;
        mMinute = minuteMin;

        npDay.setMaxValue(MyDateUtils.getNumberofDaysofMonth(
                Long.valueOf(MyDateUtils.dataOne(mYear + "-" + mMonth + "-01-01-01-00"))));

        return inflate;
    }

    /**
     * 初始化控件
     * 有最小值
     *
     * @param width   默认时间
     * @param Year    默认时间
     * @param Month   默认时间
     * @param Day     默认时间
     * @param Hour    默认时间
     * @param Mint    默认时间
     * @param minTime 最小值时间戳
     * @return
     */
    private View initDateAndTimePicker(int width, int Year,
                                       int Month,
                                       int Day,
                                       int Hour,
                                       int Mint, String minTime) {
        View inflate = LayoutInflater.from(mContext).inflate(
                R.layout.component_date, null);
        npYear = inflate.findViewById(R.id.np_year);
        npMonth = inflate.findViewById(R.id.np_month);
        npDay = inflate.findViewById(R.id.np_day);
        npHour = inflate.findViewById(R.id.np_hour);
        npMint = inflate.findViewById(R.id.np_mint);
        llContent = inflate.findViewById(R.id.ll_content);
        llContent.setLayoutParams(new LinearLayout.LayoutParams(width - 200, ViewGroup.LayoutParams.WRAP_CONTENT));

        npYear.setOnValueChangedListener(vclMin);
        npMonth.setOnValueChangedListener(vclMin);
        npDay.setOnValueChangedListener(vclMin);
        npHour.setOnValueChangedListener(vclMin);
        npMint.setOnValueChangedListener(vclMin);

        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npMint.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入

        npYear.setMinValue(1900);//起始年份
        npYear.setMaxValue(2118);//结束年份
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npMint.setMinValue(0);
        npMint.setMaxValue(59);

        String time = MyDateUtils.timesOne(minTime);
        yearMin = Integer.valueOf(time.substring(0, 4));
        monthMin = Integer.valueOf(time.substring(5, 7));
        dayMin = Integer.valueOf(time.substring(8, 10));
        hourMin = Integer.valueOf(time.substring(11, 13));
        minuteMin = Integer.valueOf(time.substring(14, 16));

        if (Year != 0) {
            npYear.setValue(Year);
            npMonth.setValue(Month);
            npDay.setValue(Day);
            npHour.setValue(Hour);
            npMint.setValue(Mint);

            mYear = Year;
            mMonth = Month;
            mDay = Day;
            mHour = Hour;
            mMinute = Mint;
        } else {
            npYear.setValue(yearMin);
            npMonth.setValue(monthMin);
            npDay.setValue(dayMin);
            npHour.setValue(hourMin);
            npMint.setValue(minuteMin);

            mYear = yearMin;
            mMonth = monthMin;
            mDay = dayMin;
            mHour = hourMin;
            mMinute = minuteMin;
        }

        npYear.setMinValue(yearMin);

        npYear.setWrapSelectorWheel(false); //设置循环滚动
        npMonth.setWrapSelectorWheel(false); //设置循环滚动
        npDay.setWrapSelectorWheel(false); //设置循环滚动
        npHour.setWrapSelectorWheel(false); //设置循环滚动
        npMint.setWrapSelectorWheel(false); //设置循环滚动

        return inflate;
    }

    /**
     * 有最大值
     *
     * @param width   默认时间
     * @param Year    默认时间
     * @param Month   默认时间
     * @param Day     默认时间
     * @param Hour    默认时间
     * @param Mint    默认时间
     * @param minTime 最小值时间戳
     * @param maxTime 最大值时间戳
     * @return
     */
    private View initDateAndTimePicker(int width, int Year,
                                       int Month,
                                       int Day,
                                       int Hour,
                                       int Mint, String minTime, String maxTime) {
        View inflate = LayoutInflater.from(mContext).inflate(
                R.layout.component_date, null);
        npYear = inflate.findViewById(R.id.np_year);
        npMonth = inflate.findViewById(R.id.np_month);
        npDay = inflate.findViewById(R.id.np_day);
        npHour = inflate.findViewById(R.id.np_hour);
        npMint = inflate.findViewById(R.id.np_mint);
        llContent = inflate.findViewById(R.id.ll_content);
        llContent.setLayoutParams(new LinearLayout.LayoutParams(width - 200, ViewGroup.LayoutParams.WRAP_CONTENT));

        npYear.setOnValueChangedListener(vclMax);
        npMonth.setOnValueChangedListener(vclMax);
        npDay.setOnValueChangedListener(vclMax);
        npHour.setOnValueChangedListener(vclMax);
        npMint.setOnValueChangedListener(vclMax);

        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入
        npMint.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //禁止输入

        npYear.setMinValue(1900);//起始年份
        npYear.setMaxValue(2118);//结束年份
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npDay.setMinValue(1);
        npDay.setMaxValue(31);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        npMint.setMinValue(0);
        npMint.setMaxValue(59);

        //最小值
        String time = MyDateUtils.timesOne(minTime);
        yearMin = Integer.valueOf(time.substring(0, 4));
        monthMin = Integer.valueOf(time.substring(5, 7));
        dayMin = Integer.valueOf(time.substring(8, 10));
        hourMin = Integer.valueOf(time.substring(11, 13));
        minuteMin = Integer.valueOf(time.substring(14, 16));

        //最大值
        String timeMax = MyDateUtils.timesOne(maxTime);
        yearMax = Integer.valueOf(timeMax.substring(0, 4));
        monthMax = Integer.valueOf(timeMax.substring(5, 7));
        dayMax = Integer.valueOf(timeMax.substring(8, 10));
        hourMax = Integer.valueOf(timeMax.substring(11, 13));
        minuteMax = Integer.valueOf(timeMax.substring(14, 16));

        //设置默认值
        if (Year != 0) {
            npYear.setValue(Year);
            npMonth.setValue(Month);
            npDay.setValue(Day);
            npHour.setValue(Hour);
            npMint.setValue(Mint);

            mYear = Year;
            mMonth = Month;
            mDay = Day;
            mHour = Hour;
            mMinute = Mint;
        } else {
            npYear.setValue(yearMin);
            npMonth.setValue(monthMin);
            npDay.setValue(dayMin);
            npHour.setValue(hourMin);
            npMint.setValue(minuteMin);

            mYear = yearMin;
            mMonth = monthMin;
            mDay = dayMin;
            mHour = hourMin;
            mMinute = minuteMin;
        }

        npYear.setMinValue(yearMin);
        npYear.setMaxValue(yearMax);

        npYear.setWrapSelectorWheel(false); //设置循环滚动
        npMonth.setWrapSelectorWheel(false); //设置循环滚动
        npDay.setWrapSelectorWheel(false); //设置循环滚动
        npHour.setWrapSelectorWheel(false); //设置循环滚动
        npMint.setWrapSelectorWheel(false); //设置循环滚动

        return inflate;
    }

    NumberPicker.OnValueChangeListener vclMin = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            time = Long.valueOf(MyDateUtils.dataOne(mYear + "-" + mMonth + "-01-01-01-00"));
            dataNum = MyDateUtils.getNumberofDaysofMonth(time);
            switch (numberPicker.getId()) {
                case R.id.np_year:
                    mYear = i1;
                    break;
                case R.id.np_month:
                    mMonth = i1;
                    break;
                case R.id.np_day:
                    mDay = i1;
                    break;
                case R.id.np_hour:
                    mHour = i1;
                    break;
                case R.id.np_mint:
                    mMinute = i1;
                    break;
            }
            if (mYear <= yearMin) {
                npMonth.setMinValue(monthMin);
                npMonth.setMaxValue(12);
                if (mMonth <= monthMin) {
                    npDay.setMinValue(dayMin);
                    npDay.setMaxValue(dataNum);
                    if (mDay <= dayMin) {
                        npHour.setMinValue(hourMin);
                        npHour.setMaxValue(23);
                        if (mHour <= hourMin) {
                            npMint.setMinValue(minuteMin);
                            npMint.setMaxValue(59);
                        } else {
                            npMint.setMinValue(0);
                            npMint.setMaxValue(59);
                        }
                    } else {
                        npHour.setMinValue(0);
                        npHour.setMaxValue(23);
                        npMint.setMinValue(0);
                        npMint.setMaxValue(59);
                    }
                } else {
                    npDay.setMinValue(1);
                    npDay.setMaxValue(dataNum);
                    npHour.setMinValue(0);
                    npHour.setMaxValue(23);
                    npMint.setMinValue(0);
                    npMint.setMaxValue(59);
                }
            } else {
                npMonth.setMinValue(1);
                npMonth.setMaxValue(12);
                npDay.setMinValue(1);
                npDay.setMaxValue(dataNum);
                npHour.setMinValue(0);
                npHour.setMaxValue(23);
                npMint.setMinValue(0);
                npMint.setMaxValue(59);
            }

            npYear.setWrapSelectorWheel(false); //设置循环滚动
            npMonth.setWrapSelectorWheel(false); //设置循环滚动
            npDay.setWrapSelectorWheel(false); //设置循环滚动
            npHour.setWrapSelectorWheel(false); //设置循环滚动
            npMint.setWrapSelectorWheel(false); //设置循环滚动
        }
    };
    NumberPicker.OnValueChangeListener vclMax = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int i, int i1) {
            time = Long.valueOf(MyDateUtils.dataOne(mYear + "-" + mMonth + "-01-01-01-00"));
            dataNum = MyDateUtils.getNumberofDaysofMonth(time);
            switch (numberPicker.getId()) {
                case R.id.np_year:
                    mYear = i1;
                    break;
                case R.id.np_month:
                    mMonth = i1;
                    break;
                case R.id.np_day:
                    mDay = i1;
                    break;
                case R.id.np_hour:
                    mHour = i1;
                    break;
                case R.id.np_mint:
                    mMinute = i1;
                    break;
            }
            if (mYear <= yearMin) {
                npMonth.setMinValue(monthMin);
                if (mMonth <= monthMin) {
                    npDay.setMinValue(dayMin);
                    if (mDay <= dayMin) {
                        npHour.setMinValue(hourMin);
                        if (mHour <= hourMin) {
                            npMint.setMinValue(minuteMin);
                        } else {
                            npMint.setMinValue(0);
                        }
                    } else {
                        npHour.setMinValue(0);
                        npMint.setMinValue(0);
                    }
                } else {
                    npDay.setMinValue(1);
                    npHour.setMinValue(0);
                    npMint.setMinValue(0);
                }
            } else {
                npMonth.setMinValue(1);
                npDay.setMinValue(1);
                npHour.setMinValue(0);
                npMint.setMinValue(0);
            }
            if (mYear == yearMax) {
                npMonth.setMaxValue(monthMax);
                if (mMonth == monthMax) {
                    npDay.setMaxValue(dayMax);
                    if (mDay == dayMax) {
                        npHour.setMaxValue(hourMax);
                        if (mHour == hourMax) {
                            npMint.setMaxValue(minuteMax);
                        } else {
                            npMint.setMaxValue(59);
                        }
                    } else {
                        npHour.setMaxValue(23);
                        npMint.setMaxValue(59);
                    }
                } else {
                    npDay.setMaxValue(dataNum);
                    npHour.setMaxValue(23);
                    npMint.setMaxValue(59);
                }
            } else {
                npMonth.setMaxValue(12);
                npDay.setMaxValue(dataNum);
                npHour.setMaxValue(23);
                npMint.setMaxValue(59);
            }

            npYear.setWrapSelectorWheel(false); //设置循环滚动
            npMonth.setWrapSelectorWheel(false); //设置循环滚动
            npDay.setWrapSelectorWheel(false); //设置循环滚动
            npHour.setWrapSelectorWheel(false); //设置循环滚动
            npMint.setWrapSelectorWheel(false); //设置循环滚动
        }
    };

    /**
     * 创建dialog
     *
     * @param view
     */
    private void initDialog(View view) {
        view.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                timePickerDialogInterface.positiveListener(
                        npYear.getValue(),
                        npMonth.getValue(),
                        npDay.getValue(),
                        npHour.getValue(),
                        npMint.getValue());
            }
        });
        view.findViewById(R.id.tv_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
                timePickerDialogInterface.negativeListener();
            }
        });
        view.findViewById(R.id.ll_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
        view.findViewById(R.id.ll_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mPopupWindow.setContentView(view);
    }

    /**
     * 显示日期选择器
     */
    public void showDateAndTimePickerDialog(int width) {
        mTag = 2;
       /* mDialog = new Dialog(mContext);
        mDialog.setTitle("选择时间");
        View view = initDateAndTimePicker(width);
        initDialog(view);
        mDialog.show();*/
    }

    /**
     * 显示日期选择器
     * 限制开始选择时间  上限minTime
     */
    @SuppressLint("WrongConstant")
    public void showDateAndTimePickerDialog(int width, int Year,
                                            int Month,
                                            int Day,
                                            int Hour,
                                            int Mint, String minTime, View rootview) {
        mTag = 2;
        View view = initDateAndTimePicker(width, Year, Month, Day, Hour, Mint, minTime);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);

        initDialog(view);

        mPopupWindow.setFocusable(true);//这里必须设置为true才能点击区域外或者消失
        mPopupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //显示PopupWindow
        mPopupWindow.setAnimationStyle(R.style.contextMenuAnim);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    }

    /**
     * 显示日期选择器  minTime 下线  maxTime 上限
     * 限制开始选择时间
     * 限制结束选择时间
     */
    @SuppressLint("WrongConstant")
    public void showDateAndTimePickerDialog(int width, int Year,
                                            int Month,
                                            int Day,
                                            int Hour,
                                            int Mint, String minTime, String maxTime, View rootview) {
        mTag = 2;
        View view = initDateAndTimePicker(width, Year, Month, Day, Hour, Mint, minTime,maxTime);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);

        initDialog(view);

        mPopupWindow.setFocusable(true);//这里必须设置为true才能点击区域外或者消失
        mPopupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //显示PopupWindow
        mPopupWindow.setAnimationStyle(R.style.contextMenuAnim);
        mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    }

    public int getYear() {
        return mYear;
    }

    public int getDay() {
        return mDay;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getMinute() {
        return mMinute;
    }

    public int getHour() {
        return mHour;
    }

    public String getTime() {
        return MyDateUtils.dataOne(mYear + "-" + mMonth + "-" + mDay + "-" + mHour + "-" + mMinute + "-00");
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        switch (numberPicker.getId()) {
            case R.id.np_year:
                mYear = i1;
                break;
            case R.id.np_month:
                mMonth = i1;
                long time = Long.valueOf(MyDateUtils.dataOne(mYear + "-" + mMonth + "-01-01-01-00"));
                int dataNum = MyDateUtils.getNumberofDaysofMonth(time);
                npDay.setMaxValue(dataNum);
                npDay.setWrapSelectorWheel(false); //设置循环滚动
                break;
            case R.id.np_day:
                mDay = i1;
                break;
            case R.id.np_hour:
                mHour = i1;
                break;
            case R.id.np_mint:
                mMinute = i1;
                break;
        }
    }

    public interface TimePickerInterface {
        void positiveListener(int Year,
                              int Month,
                              int Day,
                              int Hour,
                              int Mint);

        void negativeListener();
    }
}
