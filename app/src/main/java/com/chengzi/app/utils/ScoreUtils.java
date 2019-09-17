package com.chengzi.app.utils;

import android.text.TextUtils;

public class ScoreUtils {

    public static String processScore(String score){
        if (TextUtils.isEmpty(score)) {
            return "0.0";
        }
        double s = Double.valueOf(score);
        if (s >= 10.0d) {
            return String.format("%1$1.0f",s);
        }
        return score;
    }

    public static String processScores(String score) {
        if (TextUtils.isEmpty(score)) {
            return "0.0";
        }
        double s = Double.valueOf(score);
        if (s >= 10.0d) {
            return String.format("%1$1.0f", s);
        } else {
            return String.format("%.1f", s);
//          return String.format("%.2f",num);
        }
    }
}
