package com.kylezhudev.moveurcars;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;


public class ScheduleClient {
    private ScheduleService boundService;
    private boolean isBound;
    private Context context;


    public ScheduleClient(Context context) {
        this.context = context;
    }

    public void doBindService() {
        this.context.bindService(new Intent(this.context, ScheduleService.class), connection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            boundService = ((ScheduleService.ServiceBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            boundService = null;
        }
    };

    /**
     * use the following method passing reference to ScheduleService and set up alarm
     *
     * @param notificationCal
     */

    public void setAlarmForNotification(Calendar notificationCal, int id) {
        Log.i("Ref.NullChecker", "calendar " + notificationCal.getTime() + " id: " + id + "boundSerice =" + boundService);
        if (boundService == null) {
            doBindService();
        }
        this.boundService.setAlarm(notificationCal, id);
        Log.i("NextAlarmService", "Sent " + notificationCal.getTime() + " id: " + id + "boundSerice =" + boundService);
    }


    public void doUnBindService() {
        if (this.isBound) {
            this.context.unbindService(this.connection);
            isBound = false;
        }
    }


}



