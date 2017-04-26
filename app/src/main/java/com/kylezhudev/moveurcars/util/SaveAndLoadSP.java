package com.kylezhudev.moveurcars.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by k.zhu on 2/6/2017.
 */

public class SaveAndLoadSP {
    private SharedPreferences sharedPreferences;
    private Context context;
    private static AtomicInteger alarmIds = new AtomicInteger(0);
    private String alarmId;
    private static int intAlarmId;
    private final static String ALARM_ID = "spAlarmId";
    private static String RESULT_CODE = "101";



    private final static String SP_YEAR_KEY = "Year";
    private final static String SP_MONTH_KEY= "Month";
    private final static String SP_DAY_KEY= "Day";
    private final static String SP_HOUR_KEY= "Hour";
    private final static String SP_DOW_KEY= "DayOfWeek";
    private final static String SP_DOWIM_KEY= "DayOfWeekInMonth";
    private final static String SP_MINUTE_KEY= "Minute";
    private final static String SP_ALARM_ID = "AlarmId";
    SharedPreferences spAlarmIds;
    SharedPreferences sharedPref;

    public SaveAndLoadSP(Context context) {
        this.spAlarmIds = context.getSharedPreferences(ALARM_ID, MODE_PRIVATE);
        this.sharedPref = context.getSharedPreferences(alarmId, MODE_PRIVATE);
    }

    private void saveCalendar(Calendar calendar) {
        //TODO if alarms in recycler view is not correct, check the alarm id below.


        if(spAlarmIds.getInt(SP_ALARM_ID, -1) == -1){


        intAlarmId = alarmIds.incrementAndGet();
        alarmId = Integer.toString(intAlarmId);

        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(SP_YEAR_KEY, calendar.get(Calendar.YEAR));
//        editor.putInt(SP_MONTH_KEY, calendar.get(Calendar.MONTH));
//        editor.putInt(SP_DAY_KEY, calendar.get(Calendar.DAY_OF_MONTH));
        editor.putInt(SP_ALARM_ID, intAlarmId);
        editor.putInt(SP_DOWIM_KEY, calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        editor.putInt(SP_DOW_KEY, calendar.get(Calendar.DAY_OF_WEEK));
        editor.putInt(SP_HOUR_KEY, calendar.get(Calendar.HOUR));
        editor.putInt(SP_MINUTE_KEY, calendar.get(Calendar.MINUTE));
        editor.commit();


        //Save alarm id into Shared Preference
        spAlarmIds = context.getSharedPreferences(ALARM_ID, MODE_PRIVATE);
        SharedPreferences.Editor idEditor = spAlarmIds.edit();
        idEditor.putInt(SP_ALARM_ID, alarmIds.get());
        idEditor.commit();
        }else{

        }


    }



}
