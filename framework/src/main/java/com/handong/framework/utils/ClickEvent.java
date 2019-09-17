package com.handong.framework.utils;

import android.os.SystemClock;
import android.view.View;

import com.handongkeji.framework.R;


public class ClickEvent {

    //  防点击抖动
    public static boolean shouldClick(View view){
        Object tag = view.getTag(R.id.click_event);
        if (tag == null) {
            view.setTag(R.id.click_event,SystemClock.uptimeMillis());
            return true;
        }else{
            long uptimeMillis = SystemClock.uptimeMillis();
            long l = (Long) tag;
            if (uptimeMillis - l >= 800) {
                view.setTag(R.id.click_event,SystemClock.uptimeMillis());
                return true;
            }
        }
        return false;
    }

}
