package com.chengzi.app.databindingadapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chengzi.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateBindingAdapters {

    private static SimpleDateFormat MMdd = new SimpleDateFormat("MM月dd日");
    private static SimpleDateFormat MD = new SimpleDateFormat("MM月dd日");
    private static SimpleDateFormat YMD = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyy.MM.dd HH:mm");


    private static SimpleDateFormat MDHM = new SimpleDateFormat("MM月dd日 HH:mm");
    private static SimpleDateFormat HM = new SimpleDateFormat("HH:mm");

    /**
     * @param textView
     * @param format       日期格式化格式：eg:yyyy-MM-dd HH:mm:ss
     * @param timeInMilles 时间毫秒值
     */
    @BindingAdapter(value = {"dateAndTime", "dateFormat"}, requireAll = false)
    public static void setDateAndTime(TextView textView, long timeInMilles, String format) {
        SimpleDateFormat sdf = null;
        if (TextUtils.isEmpty(format)) {
            sdf = YMD;
        } else {
            sdf = new SimpleDateFormat(format);
        }
        textView.setText(sdf.format(new Date(timeInMilles * 1000)));
    }

    /**
     * @param textView
     * @param format       日期格式化格式：eg:yyyy-MM-dd HH:mm:ss
     * @param timeInMilles 时间毫秒值
     */
    @BindingAdapter(value = {"dateAndTime", "dateFormat"}, requireAll = false)
    public static void setDateAndTime(TextView textView, String timeInMilles, String format) {
        if (TextUtils.isEmpty(timeInMilles)) {
            return;
        }
        SimpleDateFormat sdf = null;
        if (TextUtils.isEmpty(format)) {
            sdf = YMD;
        } else {
            sdf = new SimpleDateFormat(format);
        }
        textView.setText(sdf.format(new Date(Long.valueOf(timeInMilles) * 1000)));
    }

    @BindingAdapter(value = {"yyyyMMddHHmm"}, requireAll = false)
    public static void yyyyMMddHHmm(TextView textView, long timeInMillis) {
        if (timeInMillis == 0) {
            textView.setText("");
        } else {
            String format = yyyyMMddHHmm.format(new Date(timeInMillis));
            textView.setText(format);
        }
    }

    @BindingAdapter(value = {"infoListDate"}, requireAll = false)
    public static void infoListDate(TextView textView, long timeInMillis) {
        textView.setText(MMdd.format(new Date(timeInMillis)));
    }

    @BindingAdapter(value = {"infoListDate"}, requireAll = false)
    public static void infoListDate(TextView textView, String timeInMillis) {
        textView.setText(timeInMillis);
    }

    @BindingAdapter(value = "startTime", requireAll = false)
    public static void startTime(TextView textView, String startTimeInMillis) {
        if (!TextUtils.isEmpty(startTimeInMillis)) {
            try {
                long millis = Long.valueOf(startTimeInMillis);
                String format = MMdd.format(new Date());
                textView.setText(textView.getContext().getString(R.string.hand_active_start_time, format));
            } catch (Exception e) {
            }

        }
    }

    //    时间显示
//   1）当天发布：x秒前，x分钟前，x小时前
//   2）昨天发布：昨天
//   3）昨天之前发布：x天前，x月前，x年前
    @BindingAdapter(value = {"forumDateTime"}, requireAll = false)
    public static void forumDateTime(TextView textView, long timeInMillis) {
        Context context = textView.getContext();
        String text = "";
        long curTime = System.currentTimeMillis();
        long l = curTime - timeInMillis;
        if (l < 60000L) {  //  1分钟内发布，显示刚刚

            text = context.getString(R.string.hand_just_now);

        } else if (l >= 60_000L && l < 3600_000L) {  //  1分钟以上发布，显示多少分钟前，比如6分钟前

            text = (l / 60_000L) + context.getString(R.string.hand_minutes_ago);

        } else if (l >= 3600_000L && l < 24 * 3600_000L) {  // 1小时以上，显示多少小时前

            text = (l / 3600_000L) + context.getString(R.string.hand_hours_ago);

        } else if (l >= 24 * 3600_000L && l < 48 * 3600_000L) {

            text = context.getString(R.string.hand_yesterday);

        } else if (l >= 48 * 3600_000L && l < 30 * 24 * 3600_000L) {

            text = (l / (24 * 3600_000L)) + context.getString(R.string.hand_days_ago);

        } else if (l >= 30 * 24 * 3600_000L && l < 12 * 30 * 24 * 3600_000L) {
            text = (l / (30 * 24 * 3600_000L)) + context.getString(R.string.hand_months_ago);
        } else {
            text = (l / (12 * 30 * 24 * 3600_000L)) + context.getString(R.string.hand_years_ago);
        }
        textView.setText(text);
    }


//    @BindingAdapter(value = {"dateAndTime"}, requireAll = false)
//    public static void dateAndTime(TextView textView, String timeInMillis) {
//        textView.setVisibility(TextUtils.isEmpty(timeInMillis) ? View.GONE : View.VISIBLE);
//        if (!TextUtils.isEmpty(timeInMillis)) {
//            textView.setText(getTimeStr(textView.getContext(), Long.valueOf(timeInMillis)));
//        }
//    }
//
//    @BindingAdapter(value = {"dateAndTime"}, requireAll = false)
//    public static void dateAndTime(TextView textView, long timeInMillis) {
//        textView.setVisibility(timeInMillis == 0 ? View.GONE : View.VISIBLE);
//        if (timeInMillis > 0) {
//            textView.setText(getTimeStr(textView.getContext(), timeInMillis));
//        }
//    }

    @BindingAdapter(value = {"chatTime"}, requireAll = false)
    public static void chatTime(TextView textView, long timeInMillis) {
        textView.setVisibility(timeInMillis == 0 ? View.GONE : View.VISIBLE);
        if (timeInMillis > 0) {
            textView.setText(getTime(textView.getContext(), timeInMillis));
        }
    }

    private static String getTimeStr(Context context, long timeInMillis) {
        String text = null;
        long curTime = System.currentTimeMillis();
        long l = curTime - timeInMillis;
        if (l < 60000L) {  //  1分钟内发布，显示刚刚

            text = context.getString(R.string.hand_just_now);

        } else if (l >= 60000L && l < 3600000L) {  //  1分钟以上发布，显示多少分钟前，比如6分钟前

            text = (l / 60000L) + context.getString(R.string.hand_minutes_ago);

        } else if (l >= 3600000L && l < 24 * 3600000L) {  // 1小时以上，显示多少小时前
            text = (l / 3600000L) + context.getString(R.string.hand_hours_ago);
        } else {
            Calendar calendar = Calendar.getInstance();
            int curYear = calendar.get(Calendar.YEAR);
            calendar.setTimeInMillis(timeInMillis);
            int year = calendar.get(Calendar.YEAR);
            if (curYear == year) {
                text = MD.format(new Date(timeInMillis));
            } else {
                text = YMD.format(new Date(timeInMillis));
            }
        }
        return text;
    }

    //  对话的时间
    private static String getTime(Context context, long timeInMillis) {
        String text = "";
        long curTime = System.currentTimeMillis();
        // 今天的 直接显示时间 -- 几点几分
        // 昨天的 显示昨天
        // 前天的(或之前的) 显示 年月日
        try {
            // 当前凌晨 毫秒
            long dayEarly = YMD.parse(YMD.format(curTime)).getTime();
            if (timeInMillis > dayEarly) {
                // 当天
                text = HM.format(new Date(timeInMillis));

            } else {
                // 昨天之前
                long dayAfter = dayEarly - 86400000L;
                if (timeInMillis > dayAfter) {
                    text = context.getResources().getString(R.string.hand_yesterday) + HM.format(new Date(timeInMillis));
                } else {
                    text = MDHM.format(new Date(timeInMillis));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return text;
    }

    @BindingAdapter(value = {"letterTimeStamp"}, requireAll = false)
    public static void letterTimeStamp(TextView textView, long timeInMillis) {
        if (timeInMillis <= 0) {
//            textView.setText("");
            return;
        }
        timeInMillis *= 1000;
        Context context = textView.getContext();
        String text = "";

        DateStamp stamp = new DateStamp(timeInMillis);
        if (stamp.isToday()) {
            if (stamp.inOneMinute()) {
                text = context.getString(R.string.hand_just_now);
            } else if (stamp.inOneHour()) {
                text = stamp.inOneHourString(context);
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String format = sdf.format(new Date(stamp.timeMillis));

                if (stamp.hourOfDay() >= 0 && stamp.hourOfDay() <= 6) {
                    text = context.getString(R.string.hand_before_dawn) + " " + format;
                } else if (stamp.hourOfDay() > 11 && stamp.hourOfDay() <= 17) {
                    text = context.getString(R.string.hand_afternoon) + " " + format;
                } else if (stamp.hourOfDay() > 17) {
                    text = context.getString(R.string.hand_night) + " " + format;
                } else {
                    text = context.getString(R.string.hand_morning) + " " + format;
                }
            }
        } else if (stamp.isYesterday()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String format = sdf.format(new Date(stamp.timeMillis));
            text = context.getString(R.string.hand_yesterday) + " " + format;
        } else {


            if (stamp.inOneMonth() || stamp.inOneYear()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                text = sdf.format(new Date(stamp.timeMillis));

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                text = sdf.format(new Date(stamp.timeMillis));
            }
        }

        textView.setText(text);

    }

    public static String letterTimeStamp(long timeInMillis) {
        if (timeInMillis <= 0) {
            return "";
        }
        timeInMillis *= 1000;
        DateStamp stamp = new DateStamp(timeInMillis);

        String text = "";

        if (stamp.isToday()) {
            if (stamp.inOneMinute()) {
                text = "刚刚";
            } else if (stamp.inOneHour()) {
                text = stamp.inOneHourString();
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String format = sdf.format(new Date(stamp.timeMillis));

                if (stamp.hourOfDay() >= 0 && stamp.hourOfDay() <= 6) {
                    text = "凌晨 " + format;
                } else if (stamp.hourOfDay() > 11 && stamp.hourOfDay() <= 17) {
                    text = "下午 " + format;
                } else if (stamp.hourOfDay() > 17) {
                    text = "晚上 " + format;
                } else {
                    text = "上午 " + format;
                }
            }
        } else if (stamp.isYesterday()) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String format = sdf.format(new Date(stamp.timeMillis));
            text = "昨天 " + format;
        } else {


            if (stamp.inOneMonth() || stamp.inOneYear()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                text = sdf.format(new Date(stamp.timeMillis));

            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                text = sdf.format(new Date(stamp.timeMillis));
            }
        }
        return text;
    }

    //  计算剩余时间
    @BindingAdapter(value = {"surplusTime"}, requireAll = false)
    public static void surplusTime(TextView textView, long endTime) {
        if (endTime <= 0) {
            return;
        }
        endTime *= 1000;
        Context context = textView.getContext();
        long currentTime = System.currentTimeMillis();
        if (endTime <= currentTime) {
            textView.setText(context.getString(R.string.surplus_time, 0, 0, 0));
            return;
        }
        long dest = endTime - currentTime;

        int days = (int) (dest / (24 * 60 * 60 * 1000));
        dest -= days * (24 * 60 * 60 * 1000);
        int hours = (int) (dest / (60 * 60 * 1000));
        dest -= hours * 60 * 60 * 1000;
        int minute = (int) (dest / (60 * 1000));
        textView.setText(context.getString(R.string.surplus_time, days, hours, minute));

    }

    private static class DateStamp {

        private long timeMillis;
        private int year;
        private int month;
        private int date;
        private int hour;
        private int minute;

        private long curTimeMillis;
        private int curYear;
        private int curMonth;
        private int curDate;
        private int curHour;
        private int curMinute;


        public DateStamp(long timeMillis) {
            this.timeMillis = timeMillis;
            Calendar calendar = Calendar.getInstance();
            curTimeMillis = calendar.getTimeInMillis();
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH);
            curDate = calendar.get(Calendar.DAY_OF_MONTH);
            curHour = calendar.get(Calendar.HOUR_OF_DAY);
            curMinute = calendar.get(Calendar.MINUTE);

            calendar.setTimeInMillis(timeMillis);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            date = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        public boolean isToday() {
            return curYear == year && curMonth == month && curDate == date;
        }

        public boolean isYesterday() {
            return curYear == year && curMonth == month && curDate == date + 1;
        }

        public boolean inOneMinute() {
            return (curTimeMillis - timeMillis) < 60 * 1000;
        }

        public boolean inOneHour() {
            return (curTimeMillis - timeMillis) < 60 * 60 * 1000;
        }

        public boolean inOneMonth() {
            return curYear == year && curMonth == month && curDate > date + 1;
        }

        public boolean inOneYear() {
            return curYear == year && curMonth > month;
        }

        public int hourOfDay() {
            return hour;
        }

        public String inOneHourString(Context context) {
            return ((curTimeMillis - timeMillis) / 60_000L) + context.getString(R.string.hand_minutes_ago);
        }

        public String inOneHourString() {
            return ((curTimeMillis - timeMillis) / 60_000L) + "分钟前";
        }
    }
}
