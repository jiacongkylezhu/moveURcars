package com.kylezhudev.moveurcars;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_DAY;


public class AlarmTask implements Runnable {

    private final Calendar notificationDate;
    private final AlarmManager alarmManager;
    private final Context context;
    private final String ALARM_ID = "AlarmId";
    private final String DELETE_FLAG = "isDeleted";
    private boolean isDeleted = false;
    private int id;

    public AlarmTask(Context context, Calendar notificationDate, int id, boolean isDeleted) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.notificationDate = notificationDate;
        this.id = id;
       this.isDeleted = isDeleted;

    }

    @Override
    public void run() {

//        Intent intent = new Intent(context, NotificationService.class);
        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.putExtra(NotificationService.INTENT_NOTIFY, true);
        intent.putExtra(ALARM_ID, this.id);
        intent.putExtra(DELETE_FLAG, this.isDeleted);
        //TODO replace the pending with the one .getbroadcast, then create a new receiver class.
        //TODO 2.


//        PendingIntent pendingIntent = PendingIntent.getService(context, this.id, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millisMonth = INTERVAL_DAY * 28;
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), millisMonth, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);

    }
}
