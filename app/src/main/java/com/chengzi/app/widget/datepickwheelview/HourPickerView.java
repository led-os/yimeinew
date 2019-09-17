package com.chengzi.app.widget.datepickwheelview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 滚轮式 字符串
 *
 * @ClassName:SemesterView
 * @PackageName:com.anxinxiaoyuan.app.widget.datepickwheelview
 * @Create On 2018/8/17 0017   15:29
 * @Site:http://www.handongkeji.com
 * @author:xinghuaiwang
 * @Copyrights 2018/8/17 0017 handongkeji All rights reserved.
 */


public class HourPickerView extends WheelPicker<String> {

    private List<String> mDataList = new ArrayList<String>() {{

        add("9点");
        add("10点");
        add("11点");
        add("12点");
        add("13点");
        add("14点");
        add("15点");
        add("16点");
        add("17点");
    }};

    private final int curYear,curMonth,curDay;

    private final int[] HOUR = new int[]{9, 10, 11, 12, 13, 14, 15, 16, 17};

    private String mSelectedHour = "";
    private int selectedPosition;

    private OnHourSelectedListener mL;

    public HourPickerView(Context context) {
        this(context, null);
    }

    public HourPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HourPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH) + 1;
        curDay = calendar.get(Calendar.DAY_OF_MONTH);

        setItemMaximumWidthText("12点");
        updataList();
        setSelectedHour(mSelectedHour);
        setOnWheelChangeListener(new OnWheelChangeListener<String>() {
            @Override
            public void onWheelSelected(String item, int position) {
                if (position < 0 || position >= 9) {
                    return;
                }

                Log.d("aaa", "onWheelSelected: item = "+item+"  position = "+position);

                selectedPosition = position;
                mSelectedHour = item;
                if (mL != null) {
                    mL.onHourSelected(item);
                }
            }
        });
    }

    public void setYearMonthDay(int year,int month,int day){
        if (selectedPosition < 0 || selectedPosition >= 9) {
            return;
        }
        if (year == curYear && month == curMonth && day == curDay) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour >= 9 && hour <= 17 && hour > HOUR[selectedPosition]) {
                setSelectedHour(mDataList.get(Arrays.binarySearch(HOUR, hour)));
            }
        }
    }

    public void setSelectedHour(String hour) {
        int index = 0;
        if (!TextUtils.isEmpty(hour)) {
            index = mDataList.indexOf(hour);
        }
        setCurrentPosition(index, true);
    }

    public void updataList() {
        List<String> list = new ArrayList<String>();
        list.addAll(mDataList);
        setDataList(list);
    }

    public void setOnHourSelectedListener(OnHourSelectedListener l) {
        mL = l;
    }

    public interface OnHourSelectedListener {
        void onHourSelected(String hour);
    }

}
