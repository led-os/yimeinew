package com.handong.framework.utils;

import android.view.View;

public interface ClickEventHandler<T> {

    void handleClick(View view, T bean);

}
