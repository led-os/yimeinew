package com.chengzi.app.ui.home;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    int margin = SizeUtils.dp2px(10);
    int spanSize;

    public GridDividerItemDecoration(int spanSize) {
        this.spanSize = spanSize;
        if (spanSize <= 0) {
            throw new IllegalArgumentException(String.format("spanSize <= %d", spanSize));
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        outRect.set(margin, 0, 0, margin);
    }
}
