package com.chengzi.app.utils;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

public class HDLinkMovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(widget,buffer,event);
        }
        return false;
    }

    public static MovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new HDLinkMovementMethod();

        return sInstance;
    }

    private static LinkMovementMethod sInstance;

}
