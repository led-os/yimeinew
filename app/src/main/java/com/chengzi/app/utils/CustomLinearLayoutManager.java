package com.chengzi.app.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * RecylerView禁止滑动
 *
 * @ClassName:CustomLinearLayoutManager
 * @PackageName:com.yimei.app.utils
 * @Create On 2019/4/4 0004   17:08
 * @Site:http://www.handongkeji.com
 * @author:pengjingjing
 * @Copyrights 2019/4/4 0004 handongkeji All rights reserved.
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}