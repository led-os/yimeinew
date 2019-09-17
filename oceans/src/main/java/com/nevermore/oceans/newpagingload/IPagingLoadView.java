package com.nevermore.oceans.newpagingload;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public interface IPagingLoadView extends IStateChangeListener{

    void setAdapter(RecyclerView.Adapter adapter);

    /**
     * 设置刷新和加载更多监听
     * @param listener
     */
    void setPagingLoadListener(IPagingLoadListener listener);

    /**
     * 刷新
     */
    void refresh();

    void finish();

    void finish(List dataList);

    /**
     * 设置刷新功能
     * @param enable
     */
    void setRefreshEnable(boolean enable);

    /**
     * 设置加载更多功能
     * @param enable
     */
    void setLoadMoreEnable(boolean enable);


    boolean isRefreshEnable();

    boolean isLoadMoreEnable();
}
