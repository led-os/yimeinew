package com.chengzi.app.widget.datepickwheelview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 年份选择器
 * Created by ycuwq on 17-12-27.
 */
@SuppressWarnings("unused")
public class YearPicker extends WheelPicker<Integer> {

    private int mStartYear, mEndYear;
    private int mSelectedYear;
    private OnYearSelectedListener mOnYearSelectedListener;

    public YearPicker(Context context) {
        this(context, null);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        setItemMaximumWidthText("0000");

        mStartYear = Calendar.getInstance().get(Calendar.YEAR);
        mEndYear = mStartYear + 20;
        updateYear();
        setSelectedYear(mSelectedYear, false);
        setOnWheelChangeListener(new OnWheelChangeListener<Integer>() {
            @Override
            public void onWheelSelected(Integer item, int position) {
                mSelectedYear = item;
                if (mOnYearSelectedListener != null) {
                    mOnYearSelectedListener.onYearSelected(item);
                }
            }
        });
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        mSelectedYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    private void updateYear() {
        List<Integer> list = new ArrayList<>();
        for (int i = mStartYear; i <= mEndYear; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public void setStartYear(int startYear) {
        mStartYear = startYear;
        updateYear();
        if (mStartYear > mSelectedYear) {
            setSelectedYear(mStartYear, false);
        } else {
            setSelectedYear(mSelectedYear, false);
        }
    }

    public void setEndYear(int endYear) {
        mEndYear = endYear;
        updateYear();
        if (mSelectedYear > endYear) {
            setSelectedYear(mEndYear, false);
        } else {
            setSelectedYear(mSelectedYear, false);
        }
    }

    public void setYear(int startYear, int endYear) {
        setStartYear(startYear);
        setEndYear(endYear);
    }

    public void setSelectedYear(int selectedYear) {
        setSelectedYear(selectedYear, true);
    }

    public void setSelectedYear(int selectedYear, boolean smoothScroll) {
        setCurrentPosition(selectedYear - mStartYear, smoothScroll);
    }

    public int getSelectedYear() {
        return mSelectedYear;
    }

    public void setOnYearSelectedListener(OnYearSelectedListener onYearSelectedListener) {
        mOnYearSelectedListener = onYearSelectedListener;
    }

    public interface OnYearSelectedListener {
        void onYearSelected(int year);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }
}
