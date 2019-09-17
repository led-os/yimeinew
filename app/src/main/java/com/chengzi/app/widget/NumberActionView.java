package com.chengzi.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengzi.app.R;

public class NumberActionView extends LinearLayout implements View.OnClickListener {


    private RelativeLayout btnDecrease, btnIncrease;
    private TextView tvNumber;

    public NumberActionView(Context context) {
        super(context);
        init(context, null);
    }

    public NumberActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberActionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public NumberActionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.number_action_view_layout, this);

        btnDecrease = findViewById(R.id.btn_decrease);
        tvNumber = findViewById(R.id.tv_number);
        btnIncrease = findViewById(R.id.btn_increase);



        btnDecrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(number);
            }
        });
        btnIncrease.setOnClickListener(v -> {
            if (listener1 != null) {
                listener1.onClick(number);
            }
        });
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

    public CharSequence getText() {
        return tvNumber.getText();
    }

    private int number;

    private OnClickListener listener;
    private OnClickListener listener1;

    public final void setBtnDecrease(OnClickListener listener) {
        this.listener = listener;
    }

    public final void setBtnIncrease(OnClickListener listener1) {
        this.listener1 = listener1;
    }

    public interface OnClickListener {
        void onClick(int number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_decrease:
                number = Math.max(1, --number);
                tvNumber.setText(String.valueOf(number));
                break;
            case R.id.btn_increase:
                tvNumber.setText(String.valueOf(++number));
                break;
        }
    }
}
