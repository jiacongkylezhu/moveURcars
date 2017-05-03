package com.kylezhudev.moveurcars;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.kylezhudev.moveurcars.model.DatabaseHelper;

import java.util.Calendar;


public class ReceiverIntentService extends IntentService {
    private ScheduleService boundService;
    private Context mContext;
    private AlarmManager alarmManager;
    private Calendar nextAlarmCal;
    private int alarmId = -1;
    public static String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    private final String ALARM_ID = "AlarmId";
    private final String TAG = "ReceiverIntent";
    private ScheduleClient scheduleClient;
    private DatabaseHelper dbHelper;
    private final String DELETE_FLAG = "isDeleted";
    private boolean isDeleted = false;


    public ReceiverIntentService() {
        super("ReceiverIntentService");

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent started");
        mContext = getApplicationContext();
//        mContext.bindService(new Intent(this.mContext, ScheduleService.class), connection,BIND_AUTO_CREATE);
        alarmId = intent.getIntExtra(ALARM_ID, -1);
        isDeleted = intent.getBooleanExtra(DELETE_FLAG, isDeleted);
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
        setNextAlarm();


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


    private void setNextAlarm() {
        dbHelper = DatabaseHelper.getInstance(this);
        int year = dbHelper.getYearById(alarmId);
        int month = dbHelper.getMonthById(alarmId);
        int dowim = dbHelper.getDowimById(alarmId);
        int dow = dbHelper.getDayOfWeekById(alarmId);
        int hour = dbHelper.getHourById(alarmId);
        int minute = dbHelper.getMinuteById(alarmId);

        Log.i("originAlarmSetup", "Year: " + year + " Month " + month + " DOWIM " + dowim
                + " Day of Week " + dow + "  Hour " + hour + "  Minute " + minute);


        nextAlarmCal = Calendar.getInstance();

        if (month == 11) {
            month = 0;
            year += 1;
            nextAlarmCal.set(Calendar.YEAR, year);
            nextAlarmCal.set(Calendar.MONTH, month);
        } else {
            nextAlarmCal.add(Calendar.MONTH, 1);
            month += 1;
            /**
             * updateNextMonth is set to month + 1
             */
        }

        int nextMonthDOW = nextAlarmCal.get(Calendar.DAY_OF_WEEK);
        int nextMonthDOWIM = nextAlarmCal.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        if (nextMonthDOW != dow || nextMonthDOWIM != dowim) {
            nextAlarmCal.set(Calendar.DAY_OF_WEEK, dow);
            nextAlarmCal.set(Calendar.DAY_OF_WEEK_IN_MONTH, dowim);
            Log.i("dow_dowim_checker", "Set = true dow =" + nextAlarmCal.get(Calendar.DAY_OF_WEEK));
        }
        int day = nextAlarmCal.get(Calendar.DAY_OF_MONTH);
        dbHelper.updateNextCal(year, month, day, alarmId);

        nextAlarmCal.set(Calendar.HOUR_OF_DAY, hour);
        nextAlarmCal.set(Calendar.MINUTE, minute);


        if (nextAlarmCal == null) {
            Log.i("nullNextCalendar", "nextAlarmCal is null");
        } else if (alarmId == -1) {
            Log.i("nextAlarmIDNull", "next id is null");

        } else {
            int nextDOW = nextAlarmCal.get(Calendar.DAY_OF_WEEK);
            if (nextDOW != dow) {
                nextAlarmCal.set(Calendar.DAY_OF_WEEK, dbHelper.getDayOfWeek(alarmId));
                Log.i("nextDOW_checker", "next dow checked.");
            }
            /**
             * nextAlarmCal.set Canlendar.HOUR or calendar.HOUR_OF_DAY
             */
            isDeleted = false;

            Log.i("nextAlarmSetup", "Next Year: " + nextAlarmCal.get(Calendar.YEAR) + " Next Month " + nextAlarmCal.get(Calendar.MONTH)
                    + " Next Day of Month " + nextAlarmCal.get(Calendar.DAY_OF_MONTH) + " Next DOWIM " + nextAlarmCal.get(Calendar.DAY_OF_WEEK_IN_MONTH)
                    + " Next Day of Week " + nextAlarmCal.get(Calendar.DAY_OF_WEEK) + " Next Hour " + nextAlarmCal.get(Calendar.HOUR_OF_DAY)
                    + " Next Minute " + nextAlarmCal.get(Calendar.MINUTE));

            scheduleClient.setAlarmForNotification(nextAlarmCal, alarmId);

            scheduleClient.doUnBindService();
//            MainActivity.getAlarmFragment().updateSelectedDates();

        }

    }
}
