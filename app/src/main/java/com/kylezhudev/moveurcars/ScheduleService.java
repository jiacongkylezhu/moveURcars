package com.kylezhudev.moveurcars;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class ScheduleService extends Service {
    private static int notifyID = 1;
    NotificationCompat.Builder builder;
    Notification notification;
    NotificationManager notificationManager;
    Date selectedDate;


    private final IBinder binder = new ServiceBinder();

    public class ServiceBinder extends Binder{

        ScheduleService getService(){
        return ScheduleService.this;
    }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService","Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    /**
     * Pass reference to AlarmTask
     */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setAlarm(Calendar calendar, int id){
        new AlarmTask(this, calendar, id).run();
    }

    public void cancelAlarm(int id, boolean deleteFlag){
        new AlarmTask(this, id, deleteFlag).stop();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setupNotification(){

        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_car_notification)
                .setContentTitle("Street Cleaning Soon")
                .setContentText("Street Cleaning ")
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("This is a Ticker")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);


        Intent resultIntent = new Intent(this, NotificationDetails.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        this.notification = builder.build();


        this.notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
