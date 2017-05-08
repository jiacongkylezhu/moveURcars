package com.kylezhudev.moveurcars;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Kyle on 2/5/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private static final int notificationID = 001;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private Context context;
    private int alarmId;
    private final String ALARM_ID = "AlarmId";
    private final String DELETE_FLAG = "isDeleted";
    private boolean isDeleted = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         show notification based on the alarm calendar
         */

        this.context = context;
        Log.i("AlarmReceiver", "onCreate();");
        this.notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        alarmId = intent.getIntExtra(ALARM_ID, -1);
        isDeleted = intent.getBooleanExtra(DELETE_FLAG, false);
        showNotification();

        /**
         After showing notification, do the following to start a new service to
         set up the next alarm.
         */

        Intent receiverIntent = new Intent(context, ReceiverIntentService.class);
        receiverIntent.putExtra(ALARM_ID, alarmId);
        intent.putExtra(DELETE_FLAG, this.isDeleted);
//        receiverIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(receiverIntent);

    }


    private void showNotification() {

        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle("Street Cleaning")
                .setSmallIcon(R.drawable.ic_car_notification)
                .setContentText("Alarm ID: " + alarmId + ". Street Cleaning soon. Don't forget to move your car")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);


        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, alarmId, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        notificationManager.notify(notificationID, notification);


//        stopSelf();


    }


}
