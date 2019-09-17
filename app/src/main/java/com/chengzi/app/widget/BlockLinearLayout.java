package com.chengzi.app.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class BlockLinearLayout extends LinearLayout {

    public BlockLinearLayout(Context context) {
        super(context);
    }

    public BlockLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BlockLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BlockLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);
        return true;
    }
}
