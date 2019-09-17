package com.nevermore.oceans.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nevermore.oceans.R;
import com.nevermore.oceans.pagingload.BasePagingLoadDelegate;

import java.util.ArrayList;
import java.util.List;

public class SwipeRefreshView extends BasePagingLoadDelegate {

    private RecyclerView recyclerView;
    //    private SwipeRefreshLayout swipeRefreshLayout;
    private BaseQuickAdapter adapter;
    private View emptyView;
    private boolean clipToPadding;
    private boolean nestedScrollingEnabled;
    private boolean loadMoreViewGone;

    public SwipeRefreshView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SwipeRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.swiperefresh_view, this, true);
        recyclerView = findViewById(R.id.recyclerView);
        setColorSchemeResources(R.color.colorAccent);
        setOnRefreshListener(() -> {
            if (mEvent != null) {
                mEvent.onPulldown();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setClipToPadding(clipToPadding);
        recyclerView.setNestedScrollingEnabled(nestedScrollingEnabled);
        recyclerView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setPadding(0, 0, 0, 0);
        emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, recyclerView, false);
    }

    public void setAdapter(BaseQuickAdapter adapter) {

        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        this.adapter.setOnLoadMoreListener(() -> {
            if (mEvent != null) {
                mEvent.onPullup();
            }
        }, recyclerView);
    }

    public void swapAdapter(BaseQuickAdapter adapter, boolean removeAndRecycleExistingViews) {

        this.adapter = adapter;
        recyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
        this.adapter.setOnLoadMoreListener(() -> {
            if (mEvent != null) {
                mEvent.onPullup();
            }
        }, recyclerView);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void setEmptyView(@LayoutRes int layoutRes) {
        this.emptyView = LayoutInflater.from(recyclerView.getContext()).inflate(layoutRes, recyclerView, false);
    }

    public void setLoadMoreViewGone(boolean loadMoreViewGone) {
        this.loadMoreViewGone = loadMoreViewGone;
    }

    @Override
    public void onRefreshComplete(List<?> list) {
        if (isEnabled()) {
            postDelayed(() -> setRefreshing(false), 400);
        }
        adapter.setNewData(new ArrayList(list));
        if (adapter.getData().isEmpty() && emptyView != null) {
            if (emptyView.getParent() != null) {
                ((ViewGroup) emptyView.getParent()).removeView(emptyView);
            }
            adapter.setEmptyView(emptyView);
        }
    }

    @Override
    public void onLoadComplete(List<?> list) {
        adapter.addData(list);
    }

    @Override
    public void setPulldownEnable(boolean enable) {
        setEnabled(enable);
    }

    @Override
    public void setPullupEnable(boolean enable) {
        adapter.setOnLoadMoreListener(null, recyclerView);
        adapter.setEnableLoadMore(enable);
    }

    @Override
    public void setHasMore(boolean hasMore) {
        if (hasMore) {
            adapter.loadMoreComplete();
        } else {
            adapter.loadMoreEnd(loadMoreViewGone);
        }
    }

    @Override
    public void setRefreshAndLoadMoreListener() {

    }

    @Override
    public void onStart() {
//        swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        if (isEnabled()) {
            post(() -> setRefreshing(true));
        }
    }

    @Override
    public void setClipToPadding(boolean clipToPadding) {
//        super.setClipToPadding(clipToPadding);
        this.clipToPadding = clipToPadding;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
//        super.setNestedScrollingEnabled(enabled);
        nestedScrollingEnabled = enabled;

    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor, int index) {
        recyclerView.addItemDecoration(decor, index);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }


}
