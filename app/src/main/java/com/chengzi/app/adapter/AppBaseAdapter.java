package com.chengzi.app.adapter;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public abstract class AppBaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public AppBaseAdapter(int layoutResId) {
        super(layoutResId);
        ObservableArrayList<T> list = new ObservableArrayList<>();
        setNewData(list);
        list.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> sender) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyItemRangeChanged(getHeaderLayoutCount() + positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyItemRangeInserted(getHeaderLayoutCount() + positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
                for (int i = 0; i < itemCount; i++) {
                    notifyItemMoved(getHeaderLayoutCount() + fromPosition + i, getHeaderLayoutCount() + toPosition + i);
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
                notifyItemRangeRemoved(getHeaderLayoutCount() + positionStart, itemCount);
            }
        });
    }

    public void clear(){
        getData().clear();
    }

}
