package com.chengzi.app.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class MessageHandler {

    private static boolean INIT = false;

    public static void notify(Context context){

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= 26) {
            if (!INIT) {
                INIT = true;
                NotificationChannel var9 = new NotificationChannel("upush_default", "", 3);
                NotificationManager var10 = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (var10 != null) {
                    var10.createNotificationChannel(var9);
                }
            }

            builder = new Notification.Builder(context, "upush_default");
        } else {
            builder = new Notification.Builder(context);
        }
    }

}
