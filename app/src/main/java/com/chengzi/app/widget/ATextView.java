package com.chengzi.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class ATextView extends android.support.v7.widget.AppCompatTextView {
    public ATextView(Context context) {
        super(context);
    }

    public ATextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ATextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
