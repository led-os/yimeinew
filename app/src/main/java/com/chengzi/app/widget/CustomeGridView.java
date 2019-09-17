package com.chengzi.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 自定义上下不滚动的GridView
 *
 * @ClassName:CustomeGridView
 * @PackageName:com.yimei.app.widget
 * @Create On 2019/4/9 0009   17:19
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/9 0009 handongkeji All rights reserved.
 */

public class CustomeGridView extends GridView {
    public CustomeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置上下不滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;//true:禁止滚动
        }

        return super.dispatchTouchEvent(ev);
    }
}
