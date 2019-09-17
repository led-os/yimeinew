package com.chengzi.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;

public class VideoTrimmerView extends View{

    private Paint mPaint;
    private Rect mRect;

    public VideoTrimmerView(Context context) {
        super(context);
        init();
    }

    public VideoTrimmerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoTrimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public VideoTrimmerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DEV_KERN_TEXT_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(SizeUtils.dp2px(1));
        mRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        mRect.set(0,0,measuredWidth,measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mRect,mPaint);
    }
}
