package com.chengzi.app.utils;

import android.text.TextUtils;

public class NumberShowUtils {

    /**
     * 处理收藏数量/点赞数量/分享数量，小于1000，直接显示数量，大于1000，除以1000后，四舍五入，取整，再加 k 为单位
     *
     * @param numText
     * @return
     */
    public static String processNumber(String numText) {
        if (TextUtils.isEmpty(numText) || numText.trim().length() == 0) {
            return "0";
        }
        long num = Long.valueOf(numText);
        if (num < 1000) {
            return numText;
        } else {
            return String.format("%1$1.2fk", num / 1000);
        }
    }

}
