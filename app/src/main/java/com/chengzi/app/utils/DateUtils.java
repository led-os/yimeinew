package com.chengzi.app.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.tool
 * email：893855882@qq.com
 * data：2017/5/25
 */

public class DateUtils {
    private static SimpleDateFormat msFormat = new SimpleDateFormat("mm:ss");

    /**
     * MS turn every minute
     *
     * @param duration Millisecond
     * @return Every minute
     */
    public static String timeParse(long duration) {
        String time = "";
        if (duration > 1000) {
            time = timeParseMinute(duration);
        } else {
            long minute = duration / 60000;
            long seconds = duration % 60000;
            long second = Math.round((float) seconds / 1000);
            if (minute < 10) {
                time += "0";
            }
            time += minute + ":";
            if (second < 10) {
                time += "0";
            }
            time += second;
        }
        return time;
    }

    /**
     * MS turn every minute
     *
     * @param duration Millisecond
     * @return Every minute
     */
    public static String timeParseMinute(long duration) {
        try {
            return msFormat.format(duration);
        } catch (Exception e) {
            e.printStackTrace();
            return "0:00";
        }
    }

    /**
     * 判断两个时间戳相差多少秒
     *
     * @param d
     * @return
     */
    public static int dateDiffer(long d) {
        try {
            long l1 = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(0, 10));
            long interval = l1 - d;
            return (int) Math.abs(interval);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 计算两个时间间隔
     *
     * @param sTime
     * @param eTime
     * @return
     */
    public static String cdTime(long sTime, long eTime) {
        long diff = eTime - sTime;
        return diff > 1000 ? diff / 1000 + "秒" : diff + "毫秒";
    }

    /*随机生成4位验证码*/
    public static String getAuthCode() {
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while (set.size() < 4) {
            int randonInt = random.nextInt(10);
            set.add(randonInt);
        }
        StringBuffer sb = new StringBuffer();
        for (Integer i : set) {
            sb.append("" + i);
        }
        return sb.toString();
    }

    /*
     * 将时间戳转换为时间
     */
    public static String dataTime(String longtime) {
        long time = 0;
        try {
            if (!TextUtils.isEmpty(longtime)) {
                time = Long.parseLong(longtime);
                if (String.valueOf(time).length() <= 10) {
                    time = time * 1000;
                }
            } else {
                time = 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    public static String dataTime(String longtime, String format) {
        long time = 0;
        try {
            if (!TextUtils.isEmpty(longtime)) {
                time = Long.parseLong(longtime);
                if (String.valueOf(time).length() <= 10) {
                    time = time * 1000;
                }
            } else {
                time = 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取本周的第一天
     *
     * @return String
     **/
    public static String getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time = cal.getTime();
//        return new SimpleDateFormat("yyyy-MM-dd").format(time) + " 00:00:00";
        return new SimpleDateFormat("MM月dd日").format(time);
    }

    /**
     * 获取本周的最后一天
     *
     * @return String
     **/
    public static String getWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time = cal.getTime();
//        return new SimpleDateFormat("yyyy-MM-dd").format(time) + " 23:59:59";
        return new SimpleDateFormat("MM月dd日").format(time);
    }
}
