package com.chengzi.app.widget.datepickwheelview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日期选择
 * Created by ycuwq on 17-12-28.
 */
public class DayPicker extends WheelPicker<Integer> {

    private int mMinDay, mMaxDay;

    private int mSelectedDay;

    private final int curYear;
    private final int curMonth;
    private final int curDay;
    private int mYear, mMonth;
    private long mMaxDate, mMinDate;

    private OnDaySelectedListener mOnDaySelectedListener;

    public DayPicker(Context context) {
        this(context, null);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
	    setItemMaximumWidthText("00");
	    NumberFormat numberFormat = NumberFormat.getNumberInstance();
	    numberFormat.setMinimumIntegerDigits(2);
	    setDataFormat(numberFormat);

        Calendar calendar = Calendar.getInstance();
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH) + 1;
        curDay = calendar.get(Calendar.DAY_OF_MONTH);

        mMaxDay = calendar.getActualMaximum(Calendar.DATE);
        mMinDay = curDay;

        updateDay();

        mSelectedDay = calendar.get(Calendar.DATE);
        setSelectedDay(mSelectedDay, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
	        @Override
	        public void onWheelSelected(Integer item, int position) {
	        	mSelectedDay = item;
		        if (mOnDaySelectedListener != null) {
		        	mOnDaySelectedListener.onDaySelected(item);
		        }
	        }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public void setMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();

        if (year == curYear && month == curMonth) {
            mMinDay = curDay;
            mMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }else{
            mMinDay = 1;
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            mMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

//        calendar.setTimeInMillis(mMaxDate);
//        int maxYear = calendar.get(Calendar.YEAR);
//        int maxMonth = calendar.get(Calendar.MONTH) + 1;
//        int maxDay = calendar.get(Calendar.DAY_OF_MONTH);
//        if (maxYear == year && maxMonth == month) {
//            mMaxDay = maxDay;
//        } else {
//            calendar.set(year, month - 1, 1);
//            mMaxDay = calendar.getActualMaximum(Calendar.DATE);
//        }
//        calendar.setTimeInMillis(mMinDate);
//        int minYear = calendar.get(Calendar.YEAR);
//        int minMonth = calendar.get(Calendar.MONTH) + 1;
//        int minDay = calendar.get(Calendar.DAY_OF_MONTH);
//        if (minYear == year && minMonth == month) {
//            mMinDay = minDay;
//        } else {
//            mMinDay = 1;
//        }

        updateDay();
        if (mSelectedDay < mMinDay) {
            setSelectedDay(mMinDay, false);
        } else if (mSelectedDay > mMaxDay) {
            setSelectedDay(mMaxDay, false);
        } else {
            setSelectedDay(mSelectedDay, false);
        }
    }

    public int getSelectedDay() {
        return mSelectedDay;
    }

    public void setSelectedDay(int selectedDay) {
        setSelectedDay(selectedDay, true);
    }

    public void setSelectedDay(int selectedDay, boolean smoothScroll) {
        setCurrentPosition(selectedDay - mMinDay, smoothScroll);
    }

    public void setMaxDate(long date) {
        mMaxDate = date;
    }

    public void setMinDate(long date) {
        mMinDate = date;
    }

	public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
		mOnDaySelectedListener = onDaySelectedListener;
	}

	private void updateDay() {
        List<Integer> list = new ArrayList<>();
        for (int i = mMinDay; i <= mMaxDay; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public interface OnDaySelectedListener {
    	void onDaySelected(int day);
    }
}
