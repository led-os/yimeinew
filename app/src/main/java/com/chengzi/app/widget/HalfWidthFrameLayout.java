package com.chengzi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class HalfWidthFrameLayout extends FrameLayout {
    public HalfWidthFrameLayout(@NonNull Context context) {
        super(context);
    }

    public HalfWidthFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfWidthFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HalfWidthFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(((int) (widthSize / 2f + 0.5f)), widthMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (action != null) {
            action.run();
//            action = null;
        }
    }

    private Runnable action;

    public boolean postOnAttach(Runnable action){
        this.action = action;
        return true;
    }
}
