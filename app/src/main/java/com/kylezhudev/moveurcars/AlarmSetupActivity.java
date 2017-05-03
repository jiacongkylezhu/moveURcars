package com.kylezhudev.moveurcars;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kylezhudev.moveurcars.model.DatabaseHelper;
import com.kylezhudev.moveurcars.model.SelectedDates;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmSetupActivity extends AppCompatActivity {

    Button btnNotification;
    CalendarView calendarView;
    static SelectedDates selectedDates;
    private int dayOfWeekInMonth;
    private int dayOfWeek;
    int year1, month1, day1, hour, minute;
    String strDate;
    int DIALOG_ID = 2;
    Calendar selectedCalendar;
    private ScheduleClient scheduleClient;
    private static int intAlarmId = 0;
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


                dayOfWeekInMonth = selectedCalendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                dayOfWeek = selectedCalendar.get(Calendar.DAY_OF_WEEK);

                selectedDates = new SelectedDates(year, month, dayOfMonth);
                strDate = new SimpleDateFormat("MM/dd/yyyy").format(selectedCalendar.getTime());

                Calendar testCal = Calendar.getInstance();
                testCal.set(Calendar.YEAR, year);
                testCal.set(Calendar.MONTH, month);
                testCal.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
                testCal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                Date testDate = testCal.getTime();
                String testString = new SimpleDateFormat("MM/dd/yyyy").format(testDate);


                Toast.makeText(AlarmSetupActivity.this, "DOWIM: " + dayOfWeekInMonth
                        + " DoW: " + dayOfWeek, Toast.LENGTH_SHORT).show();

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


                    if (hour < 12) {
                        selectedCalendar.set(Calendar.AM_PM, 0);
                    } else {
                        selectedCalendar.set(Calendar.AM_PM, 1);
                    }
                    selectedCalendar.set(year1, month1, day1, hour, minute);

                    Log.i("SetCalendar HourCheck", "Hour = " + hour + "Calendar = " + selectedCalendar);
                    saveCalendar(selectedCalendar);

                    /**
                     * Alarm service starts here
                     */


//                    scheduleClient.setAlarmForNotification(selectedCalendar, itemIndex, deleteFlag);
                    scheduleClient.setAlarmForNotification(selectedCalendar, intAlarmId);
                    strDate = new SimpleDateFormat("MM/dd/yyyy").format(selectedCalendar.getTime());
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

        if (itemIndex != -1) {
            dbHelper.updateCal(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH),
                    calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), itemIndex);
            /**
             * use itemIndex to capture id in db and pass it to ScheduleClient
             */
            intAlarmId = dbHelper.getID(itemIndex);
        }
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

//    @Override
//    protected void onStop() {
//        if(scheduleClient != null){
//            scheduleClient.doUnBindService();
//            super.onStop();
//        }
//
//    }

}
