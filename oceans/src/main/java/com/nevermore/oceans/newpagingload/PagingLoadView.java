package com.nevermore.oceans.newpagingload;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nevermore.oceans.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.List;

/**
 * Created by Administrator on 2018/1/29 0029.
 */

public class PagingLoadView extends FrameLayout implements IPagingLoadView {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;

    private int currentPage = 1;
    private int pageSize = 20;

    private IPagingLoadListener mPagingLoadListener;
    private ProgressBar progressBar;
    private ImageView ivNoData;

    public PagingLoadView(@NonNull Context context) {
        this(context, null);
    }

    public PagingLoadView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.paging_load_view, this);
        progressBar = findViewById(R.id.progress_bar);
        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ivNoData = findViewById(R.id.iv_no_data);
        smartRefreshLayout.setDisableContentWhenRefresh(true);
    }

    public void showNoDataImage(boolean vis) {
        ivNoData.setVisibility(vis ? VISIBLE : GONE);
    }

    public void syncNoData(final RecyclerView.Adapter adapter) {
        if (adapter != null) {
            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    showNoDataImage(adapter.getItemCount() == 0);
                }
            });
        }
    }


    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void showNetError(CharSequence message) {

    }

    @Override
    public void showNoData(CharSequence message) {

    }

    @Override
    public void showContent() {

    }


    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPagingLoadListener(@NonNull final IPagingLoadListener listener) {

        mPagingLoadListener = listener;
        if (isRefreshEnable()) {
            smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    currentPage = 1;
                    listener.onPageChange(currentPage, pageSize);
                }
            });
        }

        if (isLoadMoreEnable()) {
            smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    currentPage++;
                    listener.onPageChange(currentPage, pageSize);

                }
            });
        }

    }

    @Override
    public void refresh() {
        if (mPagingLoadListener != null) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void finish() {
        progressBar.setVisibility(GONE);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadmore();
    }

    @Override
    public void finish(List dataList) {
        if (dataList == null) {
            finish();
        } else {
            if (currentPage == 1) {
                smartRefreshLayout.finishRefresh();
                if ((dataList.size() < pageSize)) {
                    smartRefreshLayout.setEnableLoadmore(false);
                    smartRefreshLayout.finishLoadmoreWithNoMoreData();
                }else{
                    smartRefreshLayout.resetNoMoreData();
                }
            } else {
                if (dataList.size() < pageSize) {
                    smartRefreshLayout.finishLoadmoreWithNoMoreData();
                } else {
                    smartRefreshLayout.finishLoadmore(100);
                }
            }
        }
    }


    @Override
    public void setRefreshEnable(boolean enable) {
        smartRefreshLayout.setEnableRefresh(enable);
    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        smartRefreshLayout.setEnableLoadmore(enable);
    }

    @Override
    public boolean isRefreshEnable() {
        return smartRefreshLayout.isEnableRefresh();
    }

    @Override
    public boolean isLoadMoreEnable() {
        return smartRefreshLayout.isEnableLoadmore();
    }
}
