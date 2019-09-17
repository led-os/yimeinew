package com.chengzi.app.databindingadapters;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.TextView;

public class BrowerNumberBindingAdapter {

    @BindingAdapter(value = {"viewNum"}, requireAll = false)
    public static void setBrowerNum(TextView textView, String viewNum) {
        if (TextUtils.isEmpty(viewNum) || viewNum.trim().length() == 0) {
            viewNum = "0";
        }
        long num = Long.valueOf(viewNum);
        if (num < 1000) {
            textView.setText(String.valueOf(num));
        } else {
            textView.setText(String.format("%1$1.2fk", num / 1000));
        }
    }

}
