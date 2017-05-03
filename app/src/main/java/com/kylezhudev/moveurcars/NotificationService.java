package com.kylezhudev.moveurcars;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends Service{

    private static final int notificationID = 001;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private final IBinder binder = new ServiceBinder();
    public static String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    private final String ALARM_ID = "AlarmId";


    public class ServiceBinder extends Binder{
        NotificationService getService(){
            return NotificationService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.i("NotificationService", "onCreate();");
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " +intent);

        if(intent.getBooleanExtra(INTENT_NOTIFY, false)){
            showNotification(intent);

        }

        return START_NOT_STICKY;

    }

    private void showNotification(Intent alarmIdIntent) {
        int alarmId = alarmIdIntent.getIntExtra(ALARM_ID, -1);
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setContentTitle("Street Cleaning")
                .setSmallIcon(R.drawable.ic_car_notification)
                .setContentText("Alarm ID: "+ alarmId + ". Street Cleaning soon. Don't forget to move your car")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);



        Intent intent = new Intent(this, NotificationDetails.class);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, alarmId, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
//        notificationManager.notify(notificationID, notification);
        notificationManager.notify(notificationID, notification);

//        stopSelf();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
