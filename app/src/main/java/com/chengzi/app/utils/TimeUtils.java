package com.chengzi.app.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/12/3 0003.
 */

public class TimeUtils {

    public static String getTimeStr(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }


    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(Long ms) {
        long ss = 1000;
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
   /*     Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;*/
        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        /*if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }*/
        return sb.toString();
    }

    /*
     * 天 时
     */
    public static String formatDayHourTime(Long ms) {
        if (ms <= 0) {
            return "0天0时";
        }
        long ss = 1000;
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
   /*     Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;*/
        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "时");
        }
        return sb.toString();
    }

    public static String formatDayHourMinuteTime(long timeInMillins) {
        int day = (int) (timeInMillins / (24 * 60 * 60));
        timeInMillins -= day * (24 * 60 * 60);
        int hour = (int) (timeInMillins / (60*60));
        timeInMillins -= hour * (60*60);
        int minute = (int) (timeInMillins / (60));
        return day +"天"+hour +"小时"+minute+"分钟";
    }

    /*
     * 天 时 分
     * mss-秒数倒计时
     */
    public static String formatDayHourMinuteTime(String mss) {
        long mi = 1 * 60;
        long hh = mi * 60;
        long dd = hh * 24;
        long ms = !TextUtils.isEmpty(mss) ? Long.parseLong(mss) : 0;
        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
   /*     Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;*/
        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        } else {
            sb.append("0天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        } else {
            sb.append("0小时");
        }
        if (minute > 0) {
            sb.append(minute + "分钟");
        } else {
            sb.append("0分钟");
        }
        return sb.toString();
    }


    /**
     * ?天
     *
     * @param times 结束时间
     * @return
     *///7天倒计时
    public static long inDay(long times) {
        long end_time = times * 1000;
        long thisTime = System.currentTimeMillis();
        long time = end_time - thisTime;
        if (time > 0) {
            return time;
        }
        return 0;
    }

    /*
     * 时 分
     */
    public static String formatHourMinuteTime(Long ms) {
//        long ss = 1000;
//        long mi = ss * 60;
        long mi = 1 * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        StringBuffer sb = new StringBuffer();

        if (hour > 0) {
            sb.append(hour + "时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        return sb.toString();
    }


    /*
     * 时 分  秒
     */
    public static String formatHourMinuteSecondTime(Long ms) {
        if (ms < 0)
            return "00:00:00";
//        long ss = 1000;
//        long mi = ss * 60;
        long mi = 1 * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
//        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long second = (ms - day * dd - hour * hh - minute * mi) / 1;
        StringBuffer sb = new StringBuffer();
        if (hour > 0 && hour <= 9) {
            sb.append("0" + hour + ":");
        } else if (hour > 9) {
            sb.append(hour + ":");
        } else {
            sb.append("00:");
        }
        if (minute > 0 && minute <= 9) {
            sb.append("0" + minute + ":");
        } else if (minute > 9) {
            sb.append(minute + ":");
        } else {
            sb.append("00:");
        }
        if (second > 0 && second <= 9) {
            sb.append("0" + second + "");
        } else if (second > 9) {
            sb.append(second + "");
        } else {
            sb.append("00");
        }
        return sb.toString();
    }

    /*
     * 时 (分  秒)
     */
    public static String formatHourTime(Long ms) {
        if (ms < 0)
            return "00:00:00";
//        long ss = 1000;
//        long mi = ss * 60;
        long mi = 1 * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        StringBuffer sb = new StringBuffer();
        if (hour > 0 && hour <= 9) {
            sb.append("0" + hour);
        } else if (hour > 9) {
            sb.append(hour);
        } else {
            sb.append("00");
        }
        return sb.toString();
    }

    /*
     * (时) 分  (秒)
     */
    public static String formatMinuteTime(Long ms) {
        if (ms < 0)
            return "00";
//        long ss = 1000;
//        long mi = ss * 60;
        long mi = 1 * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        StringBuffer sb = new StringBuffer();
        if (minute > 0 && minute <= 9) {
            sb.append("0" + minute);
        } else if (minute > 9) {
            sb.append(minute);
        } else {
            sb.append("00");
        }
        return sb.toString();
    }

    /*
     * (时 分 ) 秒
     */
    public static String formatSecondTime(Long ms) {
        if (ms < 0)
            return "00";
//        long ss = 1000;
//        long mi = ss * 60;
        long mi = 1 * 60;
        long hh = mi * 60;
        long dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
//        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long second = (ms - day * dd - hour * hh - minute * mi) / 1;
        StringBuffer sb = new StringBuffer();
        if (second > 0 && second <= 9) {
            sb.append("0" + second);
        } else if (second > 9) {
            sb.append(second);
        } else {
            sb.append("00");
        }
        return sb.toString();
    }


    //判断第一位数是否是- 如果是负数 则显示00:00:00
    public static String getTime(long time) {
//        if (time.startsWith("-"))
//            return "00:00:00";
//        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(Long.parseLong(time));
        if (time < 0)
            return "00:00:00";
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(time);
    }


    //半小时倒计时
    public static long inHalfAnHour(long times) {
        //30分钟待付款   下单的30分钟后 time+1800000
        //30分钟内-现在的时间 =剩余的倒计时时间
        long l = times + 1800000;
        Log.i("time", times + "");
        Log.i("time2", l + "");
        Log.i("time3", l - System.currentTimeMillis() + "");
//        return Math.abs(l - System.currentTimeMillis()) + "";
        return l - System.currentTimeMillis();
    }


    //7天倒计时
    public static long inSevenDay(long times) {
        long l = times + (24 * 3600000 * 7);
        Log.i("time", times + "");
        Log.i("time2", l + "");
        Log.i("time3", l - System.currentTimeMillis() + "");
//        return Math.abs(l - System.currentTimeMillis()) + "";
        return l - System.currentTimeMillis();
    }

    public static String stampToDate(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
