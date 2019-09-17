package com.chengzi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MainDynamicFrameLayout extends FrameLayout {
    public MainDynamicFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MainDynamicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MainDynamicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MainDynamicFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Runnable runnable;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (runnable != null) {
            runnable.run();
        }
    }

    public void postOnAttach(Runnable runnable){
        this.runnable = runnable;
    }
}
