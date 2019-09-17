package com.chengzi.app.widget;

import android.support.v4.view.ViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager不可滑动
 *
 * @ClassName:ControlScrollViewPager
 * @PackageName:com.yimei.app.widget
 * @Create On 2019/5/28 0028   15:54
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/5/28 0028 handongkeji All rights reserved.
 */

public class ControlScrollViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public ControlScrollViewPager(Context context) {
        super(context);
    }

    public ControlScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
