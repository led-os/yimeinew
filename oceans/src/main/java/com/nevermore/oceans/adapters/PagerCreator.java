package com.nevermore.oceans.adapters;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Administrator on 2018/2/27 0027.
 */

public interface PagerCreator<T> {

      View getView(LayoutInflater inflater);

      void bindView(T t, View itemView);
}
