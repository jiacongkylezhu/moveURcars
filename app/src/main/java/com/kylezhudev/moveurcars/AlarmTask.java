package com.kylezhudev.moveurcars;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import static android.app.AlarmManager.INTERVAL_DAY;


public class AlarmTask implements Runnable {
    //Notified cannot set private final Calendar notificationDate and private AlarmManager, so removed final 4/29/17

    private Calendar notificationDate;
    private AlarmManager alarmManager;
    private Context context;
    private final String ALARM_ID = "AlarmId";
    private final String DELETE_FLAG = "isDeleted";
    private boolean isDeleted = false;
    private int id;

    public AlarmTask(Context context, Calendar notificationDate, int id) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.notificationDate = notificationDate;
        this.id = id;


    }

    public AlarmTask(Context context, int id, boolean isDeleted){
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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



//        PendingIntent pendingIntent = PendingIntent.getService(context, this.id, intent, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, this.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millisMonth = INTERVAL_DAY * 28;
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), millisMonth, pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationDate.getTimeInMillis(), pendingIntent);


    }

    public void stop(){
        if(isDeleted){
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent  pendingIntent = PendingIntent.getBroadcast(context, this.id, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }
}
