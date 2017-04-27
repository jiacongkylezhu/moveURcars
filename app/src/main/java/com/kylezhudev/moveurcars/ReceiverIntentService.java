package com.kylezhudev.moveurcars;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.kylezhudev.moveurcars.model.DatabaseHelper;

import java.util.Calendar;


public class ReceiverIntentService extends IntentService {
    private ScheduleService boundService;
    private Context mContext;
    private AlarmManager alarmManager;
    private Calendar nextAlarmCal;
    private int alarmId;
    public static String INTENT_NOTIFY = "com.blundell.tut.service.INTENT_NOTIFY";
    private final String ALARM_ID = "AlarmId";
    private final static String SP_YEAR_KEY = "Year";
    private final static String SP_MONTH_KEY = "Month";
    private final static String SP_DAY_KEY = "Day";
    private final static String SP_HOUR_KEY = "Hour";
    private final static String SP_DOW_KEY = "DayOfWeek";
    private final static String SP_DOWIM_KEY = "DayOfWeekInMonth";
    private final static String SP_MINUTE_KEY = "Minute";
    private final static String SP_ALARM_ID = "AlarmId";
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
        isDeleted = intent.getBooleanExtra(DELETE_FLAG,isDeleted);
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
        int year = dbHelper.getYear(alarmId);
        int month = dbHelper.getMonth(alarmId);
        int dowim = dbHelper.getDowim(alarmId);
        int dow = dbHelper.getDayOfWeek(alarmId);
        int hour = dbHelper.getHour(alarmId);
        int minute = dbHelper.getMinute(alarmId);




//        SharedPreferences spAlarm = mContext.getSharedPreferences(Integer.toString(alarmId), MODE_PRIVATE);
//        int year = spAlarm.getInt(SP_YEAR_KEY, -1);
//        int month = spAlarm.getInt(SP_MONTH_KEY, -1);
//        int dowim = spAlarm.getInt(SP_DOWIM_KEY, -1);
//        int dow = spAlarm.getInt(SP_DOW_KEY, -1);
//        int hour = spAlarm.getInt(SP_HOUR_KEY, -1);
//        int minute = spAlarm.getInt(SP_MINUTE_KEY, -1);

        nextAlarmCal = Calendar.getInstance();

        if (month == 11) {
            month = 0;
            year += 1;
            nextAlarmCal.set(Calendar.YEAR, year);
            nextAlarmCal.set(Calendar.MONTH, month);
            dbHelper.updateNextCal(year,month,alarmId);





//            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Integer.toString(alarmId), MODE_PRIVATE);
//            if (sharedPreferences != null) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt(SP_MONTH_KEY, month);
//                editor.putInt(SP_YEAR_KEY, year);
//                editor.commit();
//            } else {
//                Log.i(TAG, "Alarm ID is incorrect: " + alarmId);
//                Toast.makeText(mContext, "Alarm ID is incorrect", Toast.LENGTH_SHORT).show();
//            }
        } else {
            nextAlarmCal.add(Calendar.MONTH, 1);
            /**
             * updateNextMonth is set to month + 1
             */
            dbHelper.updateNextMonth(month, alarmId);

//            nextAlarmCal.add(Calendar.MONTH, 1);
//            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Integer.toString(alarmId), MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putInt(SP_MONTH_KEY, month + 1);
//            editor.commit();
        }

        int nextMonthDOW, nextMonthDOWIM;
        nextMonthDOW = nextAlarmCal.get(Calendar.DAY_OF_WEEK);
        nextMonthDOWIM = nextAlarmCal.get(Calendar.DAY_OF_WEEK_IN_MONTH);

        if (nextMonthDOW != dow || nextMonthDOWIM != dowim) {
            nextAlarmCal.set(Calendar.DAY_OF_WEEK, dow);
            nextAlarmCal.set(Calendar.DAY_OF_WEEK_IN_MONTH, dowim);
        }

        nextAlarmCal.set(Calendar.HOUR, hour);
        nextAlarmCal.set(Calendar.MINUTE, minute);

//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,nextAlarmCal.getTimeInMillis(),pendingIntent);

        if (nextAlarmCal == null) {
            Toast.makeText(this, "nextAlarmCal is null", Toast.LENGTH_SHORT).show();
        } else if (alarmId == -1) {
            Toast.makeText(this, "alarmID is not returned correctly", Toast.LENGTH_SHORT).show();
        } else {

            scheduleClient.setAlarmForNotification(nextAlarmCal, alarmId, isDeleted);
            scheduleClient.doUnBindService();
//            MainActivity.getAlarmFragment().updateSelectedDates();

        }

    }
}
