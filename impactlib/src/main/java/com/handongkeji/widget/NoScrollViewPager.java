package com.handongkeji.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class NoScrollViewPager extends ViewPager {

    private boolean noScroll = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, !noScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (true) {
            return;
        }
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int currentItem = getCurrentItem();
        if (getChildCount() <= 0) {
        } else {
            View childAt = getChildAt(currentItem);
            childAt.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST));
            int measuredHeight = childAt.getMeasuredHeight();
            int measuredWidth = childAt.getMeasuredWidth();
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
        Log.d("aaa", "onMeasure: currentItem = " + currentItem);
    }
}
