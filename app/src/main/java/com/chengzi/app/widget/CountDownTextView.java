package com.chengzi.app.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.chengzi.app.R;

public class CountDownTextView extends AppCompatTextView {

    public CountDownTextView(Context context) {
        super(context);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private long remainTime;
    private long previousTimes;

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime * 1000;
        previousTimes = System.currentTimeMillis();
        if (isAttachedToWindow()) {
            mHandler.post(runnable);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        long dst = System.currentTimeMillis() - previousTimes;
        remainTime -= dst;
        previousTimes = System.currentTimeMillis();
        mHandler.post(runnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(runnable);
    }

    private Handler mHandler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long dst = System.currentTimeMillis() - previousTimes;
            remainTime -= dst;
            caculate();
//            previousTimes = System.currentTimeMillis();
//            mHandler.postDelayed(this, 30 * 1000);
        }
    };

    private void caculate() {
        long remain = this.remainTime;

        remain = remain <= 0 ? 0 : remain;

        int days = (int) (remain / (24 * 60 * 60 * 1000));
        remain -= days * (24 * 60 * 60 * 1000);
        int hours = (int) (remain / (60 * 60 * 1000));
        remain -= hours * (60 * 60 * 1000);
        int minutes = (int) (remain / (60 * 1000));
        remain -= minutes * (60 * 1000);
        int seconds = (int) (remain / 1000);

        setText("差1人拼成   " + getContext().getString(R.string.count_down_text, days, hours, minutes));
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        CountDownSavedState savedState = new CountDownSavedState(parcelable);
        savedState.remainTime = remainTime;
        savedState.previousTime = previousTimes;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof CountDownSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        CountDownSavedState savedState = (CountDownSavedState) state;
        super.onRestoreInstanceState(((CountDownSavedState) state).getSuperState());
        this.remainTime = savedState.remainTime;
        this.previousTimes = savedState.previousTime;
        if (isAttachedToWindow()) {
            mHandler.post(runnable);
        }
    }

    public static class CountDownSavedState extends BaseSavedState {

        public CountDownSavedState(Parcel source) {
            super(source);
        }

        public CountDownSavedState(Parcelable superState) {
            super(superState);
        }

        public long remainTime;
        public long previousTime;
    }
}
