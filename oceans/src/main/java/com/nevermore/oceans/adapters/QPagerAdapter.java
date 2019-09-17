package com.nevermore.oceans.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18 0018.
 */

public abstract class QPagerAdapter<T> extends PagerAdapter implements PagerCreator<T> {
    protected List<T> mList;



    public QPagerAdapter() {
        this(null);
    }

    public QPagerAdapter(List<T> dataList) {
        this.mList = dataList == null ? new ArrayList<T>() : dataList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View  view = getView(LayoutInflater.from(container.getContext()));
        T t = mList.get(position);
        bindView(t,view);
        container.addView(view);
        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
