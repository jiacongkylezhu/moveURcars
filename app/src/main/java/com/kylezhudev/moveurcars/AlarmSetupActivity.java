package com.kylezhudev.moveurcars;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kylezhudev.moveurcars.model.DatabaseHelper;
import com.kylezhudev.moveurcars.model.SelectedDates;
import com.kylezhudev.moveurcars.recyclerView.AlarmFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class AlarmSetupActivity extends AppCompatActivity {

    Button btnNotification;
    NotificationCompat.Builder builder;
    int notifyID = 1;
    NotificationManager notificationManager;
    CalendarView calendarView;
    static SelectedDates selectedDates;
    private AlarmFragment alarmFragment;
    private RecyclerView recyclerView;
    private int dayOfWeekInMonth;
    private int dayOfWeek;
    static String DOW_VALUE_KEY = "Selected Day Of Week";
    static String DOWIM_VALUE_KEY = "Selected Day Of Week In Month";
    int year1, month1, day1, hour, minute;
    String strDate;

    int DIALOG_ID = 2;
    Calendar selectedCalendar;
    private ScheduleClient scheduleClient;

    private static AtomicInteger alarmIds = new AtomicInteger(0);
    private String alarmId;
    private static int intAlarmId = 0;
    private final static String ALARM_ID = "spAlarmId";
    private static String RESULT_CODE = "101";


    private final static String SP_YEAR_KEY = "Year";
    private final static String SP_MONTH_KEY = "Month";
    private final static String SP_DAY_KEY = "Day";
    private final static String SP_HOUR_KEY = "Hour";
    private final static String SP_DOW_KEY = "DayOfWeek";
    private final static String SP_DOWIM_KEY = "DayOfWeekInMonth";
    private final static String SP_MINUTE_KEY = "Minute";
    private final static String SP_ALARM_ID = "AlarmId";

    private static final String ID_KEY = "ID";
    private DatabaseHelper dbHelper;
    private static int itemIndex;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setup);
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();
        dbHelper = DatabaseHelper.getInstance(this);


        calendarView = (CalendarView) findViewById(R.id.cv_calendar);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                selectedDates = new SelectedDates(year, month, dayOfMonth);
                selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(year, month, dayOfMonth);
                year1 = year;
                month1 = month;
                day1 = dayOfMonth;

                ///
                dayOfWeekInMonth = selectedCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                selectedDates = new SelectedDates(year, month, dayOfMonth);
                strDate = new SimpleDateFormat("MM/dd/yyyy").format(selectedDates.getTime());

                Calendar testCal = Calendar.getInstance();
                testCal.set(Calendar.YEAR, year);
                testCal.set(Calendar.MONTH, month);
                testCal.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
                testCal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                Date testDate = testCal.getTime();
                String testString = new SimpleDateFormat("MM/dd/yyyy").format(testDate);


                Toast.makeText(AlarmSetupActivity.this, "DWIM: " + dayOfWeekInMonth
                        + " DoW: " + dayOfWeek, Toast.LENGTH_SHORT).show();
//                Toast.makeText(AlarmSetupActivity.this, "Selected date " + testString, Toast.LENGTH_SHORT).show();

                ////

//                Toast.makeText(AlarmSetupActivity.this, strDate + " Week Of Month: "
//                        + selectedDates.getWeekOfMonth()
//                        + " Day Of Week " + selectedDates.getDayOfWeek(), Toast.LENGTH_SHORT).show();
                showDialog(DIALOG_ID);
            }
        });

        btnNotification = (Button) findViewById(R.id.btn_notification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedDates == null) {
                    Toast.makeText(AlarmSetupActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                } else {

//                    selectedCalendar = Calendar.getInstance();
                    if (hour < 12) {
                        selectedCalendar.set(Calendar.AM_PM, 0);
                    } else {
                        selectedCalendar.set(Calendar.AM_PM, 1);
                    }
                    selectedCalendar.set(year1, month1, day1, hour, minute);

                    Log.i("SetCalendar HourCheck", "Hour = " + hour + "Calendar = " + selectedCalendar);
//                    setupSelectedCal();
                    saveCalendar(selectedCalendar);

                    /**
                     * Alarm service starts here
                     */
                    boolean deleteFlag = false;

                    scheduleClient.setAlarmForNotification(selectedCalendar, itemIndex, deleteFlag);

                    Toast.makeText(AlarmSetupActivity.this, "Street cleaning notification is set for: " + hour + ":" + minute
                            + " on " + strDate, Toast.LENGTH_SHORT).show();

                    //TODO if Alarm service not working properly check the unbind service below

                    MainActivity.getAlarmFragment().updateSelectedDates();

                    scheduleClient.doUnBindService();
                    setResult(RESULT_OK);
                    startActivity(new Intent(AlarmSetupActivity.this, MainActivity.class));
                    dbHelper.closeDB();
                    dbHelper.close();
                    finish();

                }
            }
        });


    }


    private void saveCalendar(Calendar calendar) {
        //TODO 4/15/2017 get itemIndex from database and check if the last itemIndex in db and then ++
        //TODO 4/23/2017 keep an eye on the first time itemIndex when there is no record in db.

        itemIndex = getIntent().getIntExtra(ID_KEY, -1);
        Log.i("check inAlarmId", "intAlarmId = " + itemIndex);

        if(itemIndex != -1){
            dbHelper.updateCal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH),
                   calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), itemIndex);

//            boolean isInserted = dbHelper.insertData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH),
//                    calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), null, null, itemIndex);
//            if (isInserted) {
//                Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT);
//            } else {
//                Toast.makeText(this, "Data does not Inserted", Toast.LENGTH_SHORT);
//
//            }
//        }else{
//            Log.i("Item Index Issue", "itemIndex = -1, check streetSetup ID");
//            Toast.makeText(this, "itemIndex = -1", Toast.LENGTH_SHORT);
        }




        //TODO 4/15/2017 delete old saveCalender below if the above SQLite works

//        SharedPreferences spAlarmIds = getSharedPreferences(ALARM_ID, MODE_PRIVATE);
//        if (spAlarmIds != null) {
//            intAlarmId = spAlarmIds.getInt(SP_ALARM_ID, -1);
//        }
//
//        if (intAlarmId == -1) {
//            intAlarmId = 1;
//        } else {
//            intAlarmId++;
//        }
//        Log.i("check inAlarmId", "intAlarmId = " + intAlarmId);
//
//        //Save alarm id into Shared Preference
//
//        SharedPreferences.Editor idEditor = spAlarmIds.edit();
//        idEditor.putInt(SP_ALARM_ID, intAlarmId);
//        idEditor.commit();
//
//        alarmId = Integer.toString(intAlarmId);
//        SharedPreferences sharedPref = getSharedPreferences(alarmId, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(SP_ALARM_ID, intAlarmId);
//        editor.putInt(SP_YEAR_KEY, calendar.get(Calendar.YEAR));
//        editor.putInt(SP_MONTH_KEY, calendar.get(Calendar.MONTH));
//        editor.putInt(SP_DAY_KEY, calendar.get(Calendar.DAY_OF_MONTH));
//        editor.putInt(SP_HOUR_KEY, calendar.get(Calendar.HOUR_OF_DAY));
//        editor.putInt(SP_MINUTE_KEY, calendar.get(Calendar.MINUTE));
//        editor.putInt(SP_DOWIM_KEY, calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
//        editor.putInt(SP_DOW_KEY, calendar.get(Calendar.DAY_OF_WEEK));
//        editor.commit();


    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {

            return new TimePickerDialog(AlarmSetupActivity.this, timePickerListener, hour, minute, false);

        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker timePicker, int newHour, int newMinute) {
                    hour = newHour;
                    minute = newMinute;
                    Toast.makeText(AlarmSetupActivity.this, "Street cleaning at " + hour + ":" + minute
                            + " on " + strDate, Toast.LENGTH_SHORT).show();
                }

            };


//        public void notify(View view) {
//        Intent dismissIntent = new Intent(this, NotificationDetails.class);
//        PendingIntent piDismiss = PendingIntent.getActivity(this, 0, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        PendingIntent piSnooze = PendingIntent.getActivity(this, 0, dismissIntent, PendingIntent.FLAG_ONE_SHOT);


//        Calendar sysCalendar = Calendar.getInstance();
//        int sysWeekOfMonth = sysCalendar.get(Calendar.WEEK_OF_MONTH);
//
//        int sysDayOfWeek = sysCalendar.get(Calendar.DAY_OF_WEEK);
//        int sysDayOfWeekInMonth = sysCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
//
//
//
//        String  sSavedSelectedDOW;
//        String  sSavedSelectedDOWIM;
//
//
//        if(sysDayOfWeekInMonth == selectedDates.getDateOfWeekInMonth() && sysDayOfWeek == selectedDates.getDayOfWeek()) {
//            saveSelectedDate(selectedDates.getDayOfWeek(), selectedDates.getDateOfWeekInMonth(), this);
//            sSavedSelectedDOW = Integer.toString(getSavedSelectedDOW(this));
//            sSavedSelectedDOWIM = Integer.toString(getSavedSelectedDOWIM(this));
//            Toast.makeText(MainActivity.this,  sSavedSelectedDOW + sSavedSelectedDOWIM, Toast.LENGTH_SHORT).show();
//            setupNotification();
//            showDialog(DIALOG_ID);
//
//        }else{
//                Toast.makeText(MainActivity.this, "Error",Toast.LENGTH_SHORT).show();
//            }
//
//
//    }

    public void setupSelectedCal() {
        selectedCalendar.set(year1, month1, day1, hour, minute);
        selectedCalendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
        selectedCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
//        selectedCalendar.getTime();

    }


    public void setupNotification() {
        String strDate = new SimpleDateFormat("MM/dd/yyyy").format(this.selectedDates.getSelectedDate());

        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_car_notification)
                .setContentTitle("Street Cleaning Soon")
                .setContentText("Street Cleaning " + strDate)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("This is a Ticker")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);


        Intent resultIntent = new Intent(this, NotificationDetails.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        Notification notification = builder.build();


        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyID, notification);


        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //TODO(1) repeat the alarm
        long repeatingMillis = 1000 * 60 * 60 * 24;
        long millisMonth = repeatingMillis * 28;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, selectedCalendar.getTimeInMillis(), millisMonth, resultPendingIntent);
    }

//    @Override
//    protected void onStop() {
//        if(scheduleClient != null){
//            scheduleClient.doUnBindService();
//            super.onStop();
//        }
//
//    }

    public static void saveSelectedDate(int selectedDOW, int selectedDOWIN, Context context) {
        SharedPreferences selectedDate = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = selectedDate.edit();
        editor.putInt(DOW_VALUE_KEY, selectedDOW);
        editor.putInt(DOWIM_VALUE_KEY, selectedDOWIN);
        editor.commit();

    }

    public static int getSavedSelectedDOW(Context context) {
        SharedPreferences savedSelectedDOW = PreferenceManager.getDefaultSharedPreferences(context);
        return savedSelectedDOW.getInt(DOW_VALUE_KEY, 0);
    }

    public static int getSavedSelectedDOWIM(Context context) {
        SharedPreferences savedSelectedDOWIM = PreferenceManager.getDefaultSharedPreferences(context);

        return savedSelectedDOWIM.getInt(DOWIM_VALUE_KEY, 0);

    }
}
