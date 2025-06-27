package com.example.wanderwell.Helpers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.wanderwell.Activities.MyReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlertHelper {

    public static void setDateAlert(Context context, String dateString, String message, int alertId) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date alertDate = sdf.parse(dateString);
            if (alertDate != null) {
                long triggerTime = alertDate.getTime();

                Intent intent = new Intent(context, MyReceiver.class);
                intent.putExtra("key", message);

                PendingIntent sender = PendingIntent.getBroadcast(
                        context,
                        alertId,
                        intent,
                        PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (alarmManager != null) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, sender);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
