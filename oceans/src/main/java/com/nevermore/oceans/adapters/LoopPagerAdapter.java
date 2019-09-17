package com.nevermore.oceans.adapters;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/27 0027.
 */

public abstract class LoopPagerAdapter<T> extends PagerAdapter implements PagerCreator<T> {
    protected List<T> mList;

    public LoopPagerAdapter() {
        this(null);
    }

    public LoopPagerAdapter(List<T> dataList) {
        this.mList = dataList == null ? new ArrayList<T>() : dataList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return  view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View  view = getView(LayoutInflater.from(container.getContext()));
        T t = mList.get(position%mList.size());
        bindView(t,view);
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
