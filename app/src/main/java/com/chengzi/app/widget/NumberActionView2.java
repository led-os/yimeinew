package com.chengzi.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengzi.app.R;


public class NumberActionView2 extends LinearLayout implements View.OnClickListener {


    private TextView btnDecrease;
    private TextView tvNumber;
    private TextView btnIncrease;

    public NumberActionView2(Context context) {
        super(context);
        init(context, null);
    }

    public NumberActionView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberActionView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NumberActionView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        setOrientation(HORIZONTAL);

        LayoutInflater.from(context).inflate(R.layout.number_action_view_layout2, this);

        btnDecrease = findViewById(R.id.btn_decrease);
        tvNumber = findViewById(R.id.tv_number);
        btnIncrease = findViewById(R.id.btn_increase);

        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);

        tvNumber.setText(String.valueOf(number));
    }

    public final void setText(CharSequence text) {
        try {
            number = Integer.parseInt(text.toString());
            tvNumber.setText(text);
        } catch (NumberFormatException e) {
            number = 1;
            tvNumber.setText("1");
        }
    }

    public void setNumber(int number) {
        setText(String.valueOf(number));
    }

    public CharSequence getText() {
        return tvNumber.getText();
    }

    private int number = 1;

    private OnClickListener decreaseListener;
    private OnClickListener increaseListener;

    public final void setBtnDecrease(OnClickListener listener) {
        this.decreaseListener = listener;
    }

    public final void setBtnIncrease(OnClickListener listener1) {
        this.increaseListener = listener1;
    }

    public interface OnClickListener {
        void onClick(int number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_decrease:
                if (number <= 1) {
                    return;
                }
                int num = number - 1;
                if (decreaseListener != null) {
                    decreaseListener.onClick(num);
                }
                break;
            case R.id.btn_increase:
                num = number + 1;
                if (increaseListener != null) {
                    increaseListener.onClick(num);
                }
                break;
        }
    }
}
