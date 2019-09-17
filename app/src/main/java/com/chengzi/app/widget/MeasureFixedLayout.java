package com.chengzi.app.widget;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.Utils;

public class MeasureFixedLayout extends FrameLayout {
    public MeasureFixedLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MeasureFixedLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MeasureFixedLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MeasureFixedLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private int screenHeight;

    private void init() {
        WindowManager wm = (WindowManager) Utils.getApp().getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            screenHeight = getContext().getApplicationContext().getResources().getDisplayMetrics().heightPixels;
        } else {
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                wm.getDefaultDisplay().getRealSize(point);
            } else {
                wm.getDefaultDisplay().getSize(point);
            }
            screenHeight = point.y;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredHeight = getMeasuredHeight();

        if (measuredHeight > screenHeight / 2) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(screenHeight / 2, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
