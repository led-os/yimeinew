package com.chengzi.app.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handong.framework.dialog.BottomDialog;
import com.chengzi.app.R;
import com.chengzi.app.databinding.HandDialogPickerLayoutBinding;
import com.chengzi.app.widget.datepickwheelview.DayPicker;
import com.chengzi.app.widget.datepickwheelview.HourPickerView;
import com.chengzi.app.widget.datepickwheelview.MonthPicker;
import com.chengzi.app.widget.datepickwheelview.YearPicker;

import java.util.Calendar;

public class TestDriveTimeDialog extends BottomDialog implements YearPicker.OnYearSelectedListener,
        MonthPicker.OnMonthSelectedListener,
        DayPicker.OnDaySelectedListener,
        HourPickerView.OnHourSelectedListener,View.OnClickListener {

    private HandDialogPickerLayoutBinding mPickerBinding;

    private int mSelectYear = 2018, mSelectMonth = 12, mSelectDay = 12, mSelectHour = 12;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mPickerBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.hand_dialog_picker_layout, container, false);
        return mPickerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPickerBinding.setListener(this);
        mPickerBinding.yearPicker.setOnYearSelectedListener(this);
        mPickerBinding.monthPicker.setOnMonthSelectedListener(this);
        mPickerBinding.dayPicker.setOnDaySelectedListener(this);
        mPickerBinding.hourPicker.setOnHourSelectedListener(this);

        mSelectYear = mPickerBinding.yearPicker.getSelectedYear();
        mSelectMonth = mPickerBinding.monthPicker.getSelectedMonth();
        mSelectDay = mPickerBinding.dayPicker.getSelectedDay();
        mSelectHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        mPickerBinding.hourPicker.setSelectedHour(mSelectHour + getString(R.string.hand_point_str));
    }

    @Override
    public void onYearSelected(int year) {
        mSelectYear = year;
        mPickerBinding.monthPicker.setYear(year);
        mPickerBinding.dayPicker.setMonth(year, mSelectMonth);
        mPickerBinding.hourPicker.setYearMonthDay(mSelectYear,mSelectMonth,mSelectDay);
    }

    @Override
    public void onMonthSelected(int month) {
        mSelectMonth = month;
        mPickerBinding.dayPicker.setMonth(mSelectYear, month);
        mPickerBinding.hourPicker.setYearMonthDay(mSelectYear,mSelectMonth,mSelectDay);
    }


    @Override
    public void onDaySelected(int day) {
        mSelectDay = day;
        mPickerBinding.hourPicker.setYearMonthDay(mSelectYear,mSelectMonth,mSelectDay);
    }

    @Override
    public void onHourSelected(String hour) {
        mPickerBinding.hourPicker.setYearMonthDay(mSelectYear,mSelectMonth,mSelectDay);
        mSelectHour = Integer.valueOf(hour.replace(getString(R.string.hand_point_str), ""));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancelBtn) {
            dismissAllowingStateLoss();
        }else if (v.getId() == R.id.fixBtn){
            String selectMonth = (mSelectMonth + "").length() == 1 ? ("0" + mSelectMonth) : mSelectMonth + "";
            String selectDay = (mSelectDay + "").length() == 1 ? ("0" + mSelectDay) : mSelectDay + "";
            String selectHour = (mSelectHour + "").length() == 1 ? ("0" + mSelectHour) : mSelectHour + "";


            String mSelectedDate = mSelectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":00:00";
            if (listener != null) {
                listener.onDateSelected(Integer.valueOf(mSelectYear),Integer.valueOf(selectMonth),Integer.valueOf(selectDay),Integer.valueOf(selectHour));
            }
            dismissAllowingStateLoss();
        }
    }

    private OnDateSelectedListener listener;

    public void setListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnDateSelectedListener{
        void onDateSelected(int year, int month, int day, int hour);
    }
}
