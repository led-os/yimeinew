package com.chengzi.app.widget;

import android.content.Context;
import android.graphics.Outline;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

public class CircleBorderFrameLayout extends FrameLayout {
    public CircleBorderFrameLayout(@NonNull Context context) {
        this(context,null);
    }

    public CircleBorderFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public CircleBorderFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,-1);
    }

    public CircleBorderFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setClipToOutline(true);
        ViewOutlineProvider outlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            }
        };
        setOutlineProvider(outlineProvider);
    }

}
