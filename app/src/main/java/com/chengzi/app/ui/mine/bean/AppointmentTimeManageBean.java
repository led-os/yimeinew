package com.chengzi.app.ui.mine.bean;

import com.google.gson.annotations.JsonAdapter;
import com.chengzi.app.utils.HDDateTypeAdapter;

public class AppointmentTimeManageBean {


    /**
     * time : 1561132800
     * am : 1
     * pm : 0
     */

    @JsonAdapter(value = HDDateTypeAdapter.class)
    private Date time;
    private int am;
    private int pm;

    public Date getDate() {
        return time;
    }

    public void setDate(Date date) {
        this.time = date;
    }

    public int getAm() {
        return am;
    }

    public void setAm(int am) {
        this.am = am;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public static class Date{
        private int year;
        private int month;
        private int date;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }
    }
}
