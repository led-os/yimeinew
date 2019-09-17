package com.chengzi.app.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.chengzi.app.ui.mine.bean.AppointmentTimeManageBean;

import java.io.IOException;
import java.util.Calendar;

public class HDDateTypeAdapter extends TypeAdapter<AppointmentTimeManageBean.Date> {

    @Override
    public void write(JsonWriter out, AppointmentTimeManageBean.Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.set(value.getYear(),value.getMonth(),value.getDate());
            out.value(calendar.getTimeInMillis());
        }
    }

    @Override
    public AppointmentTimeManageBean.Date read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case NULL:
                return null;
            case NUMBER:
                long l = in.nextLong();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(l * 1000);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DAY_OF_MONTH);
                AppointmentTimeManageBean.Date d = new AppointmentTimeManageBean.Date();
                d.setYear(year);
                d.setMonth(month);
                d.setDate(date);
                return d;
        }
        return null;
    }
}
