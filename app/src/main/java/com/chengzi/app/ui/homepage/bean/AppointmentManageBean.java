package com.chengzi.app.ui.homepage.bean;

import java.io.Serializable;

public class AppointmentManageBean implements Serializable {


    private DayBean today;
    private DayBean tomorrow;
    private DayBean the_day_after_tomorrow;

    public DayBean getToday() {
        return today == null ? (today = new DayBean()) : today;
    }

    public void setToday(DayBean today) {
        this.today = today;
    }

    public DayBean getTomorrow() {
        return tomorrow == null ? (tomorrow = new DayBean()) : tomorrow;
    }

    public void setTomorrow(DayBean tomorrow) {
        this.tomorrow = tomorrow;
    }

    public DayBean getThe_day_after_tomorrow() {
        return the_day_after_tomorrow == null ? (the_day_after_tomorrow = new DayBean()) : the_day_after_tomorrow;
    }

    public void setThe_day_after_tomorrow(DayBean the_day_after_tomorrow) {
        this.the_day_after_tomorrow = the_day_after_tomorrow;
    }

    public static class DayBean implements Serializable{

        private int am;
        private int pm;

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

        public boolean isAm() {
            return getAm() == 1;
        }

        public boolean isPm() {
            return getPm() == 1;
        }
    }


}
