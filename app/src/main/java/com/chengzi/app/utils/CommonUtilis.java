package com.chengzi.app.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DecimalFormat;
import java.util.List;

public class CommonUtilis {

    public static String doubleToString(double num) {
        //使用0.00不足位补0，#.##仅保留有效位
        return new DecimalFormat("0.00").format(num);
    }

    public static String getTwoDecimal(String money) {
        Double f = Double.parseDouble(money);
        if (f < 1) {
            String s = doubleToString(f);
            return String.valueOf(s);
        } else {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String p = decimalFormat.format(f);//format 返回的是字符串
            return p;
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     */
    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String isEmpty(Object msg) {
        if (TextUtils.isEmpty(String.valueOf(msg)) || msg == null)
            return "";
        else
            return String.valueOf(msg);
    }
}
