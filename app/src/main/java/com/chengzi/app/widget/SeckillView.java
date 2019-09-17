package com.chengzi.app.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengzi.app.R;

public class SeckillView extends LinearLayout {

    //距开始/结束
    private TextView tvTypeText;
    private TextView tvHour;
    private TextView tvMinute;
    private TextView tvSecond;
    private CountDownTimer timer;

    public SeckillView(Context context) {
        super(context);
        init(context);
    }

    public SeckillView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SeckillView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SeckillView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.seckill_view_layout, this);
        tvTypeText = findViewById(R.id.tv_type_text);
        tvHour = findViewById(R.id.tv_hour);
        tvMinute = findViewById(R.id.tv_minute);
        tvSecond = findViewById(R.id.tv_second);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
    }

    private long remainTime;


    public void setRemainTime(String typeText, long remainTime) {
        tvTypeText.setText(typeText);
        setRemainTime(remainTime);
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
        if (remainTime <= 0) {
            remainTime = 0;
            caculate(remainTime * 1000);
            return;
        }
        timer = new CountDownTimer(remainTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                caculate(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (listener != null) {
                    listener.onSeckillFinish();
                }
            }
        };
        timer.start();
    }

    public void setStopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            caculate(0);
        }
    }


    private void caculate(long remainTimeInMillins) {
        int hour = (int) (remainTimeInMillins / (60 * 60 * 1000));
        remainTimeInMillins -= hour * (60 * 60 * 1000);
        int minute = (int) (remainTimeInMillins / (60 * 1000));
        remainTimeInMillins -= minute * (60 * 1000);
        int second = (int) (remainTimeInMillins / 1000);

        tvHour.setText(String.format("%02d", hour));
        tvMinute.setText(String.format("%02d", minute));
        tvSecond.setText(String.format("%02d", second));
    }

    private OnSeckillFinishListener listener;

    public void setListener(OnSeckillFinishListener listener) {
        this.listener = listener;
    }

    public interface OnSeckillFinishListener {
        void onSeckillFinish();
    }
}
