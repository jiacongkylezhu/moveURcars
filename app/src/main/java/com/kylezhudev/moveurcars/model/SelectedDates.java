package com.kylezhudev.moveurcars.model;


import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SelectedDates extends Date{
    private Date selectedDate;
    private int dayOfWeekInMonth;
    private int dayOfWeek;
    private Calendar calendar;
    private static int SelectedDatesId = 0;
    //TODO(9) detele
    private String dummyDate;

    //TODO(2) selectedDate = null needs to be changed, add var hour and minute
//    public SelectedDates(int year, int month, int dayOfMonth) {
//        this.selectedDate = null;
//        this.calendar = Calendar.getInstance();
//        this.calendar.set(Calendar.YEAR, year);
//        this.calendar.set(Calendar.MONTH, month);
//        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        this.selectedDate = calendar.getTime();
//    }

    public SelectedDates(int year, int month, int dayOfMonth, int hour, int minute) {
        this.selectedDate = null;
        this.calendar = Calendar.getInstance();
        this.calendar.set(Calendar.YEAR, year);
        this.calendar.set(Calendar.MONTH, month);
        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        this.calendar.set(Calendar.HOUR, hour);
        this.calendar.set(Calendar.HOUR_OF_DAY, hour);
        this.calendar.set(Calendar.MINUTE, minute);
        this.selectedDate = calendar.getTime();
    }

    public int getDayOfWeekInMonth() {
        return this.dayOfWeekInMonth;
    }
    public int getDayOfWeeek(){
        return this.dayOfWeek;
    }

    public SelectedDates(int dayOfWeekInMonth, int dayOfWeek, int year){
        this.dayOfWeekInMonth = dayOfWeekInMonth;
        this.dayOfWeek = dayOfWeek;
        this.calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
        this.selectedDate = calendar.getTime();

    }


    public Calendar selectedCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, getDateOfWeekInMonth());
        calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek());
        return calendar;
    }



    //TODO(10) delete dummydata
    public SelectedDates(String string){
        dummyDate = string;

    }
    public String getSelectDates(){
        return this.dummyDate;
    }



//
//    public Date getSecondTuesdayOfMonth() {
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
//        calendar.set(Calendar.WEEK_OF_MONTH, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 13);
//        calendar.set(Calendar.MINUTE, 0);
//        this.selectedDate = calendar.getTime();
//
//
//        return this.selectedDate;
//    }

    public Date getSelectedDate(int year, int month, int dayOfMonth) {
        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(Calendar.YEAR, year);
        selectedCalendar.set(Calendar.MONTH, month);
        selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        this.selectedDate = selectedCalendar.getTime();

        return this.selectedDate;
    }



//TODO(8) Might remove getSelecteDate() below
    public Date getSelectedDate() {

        return this.selectedDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getFormattedDate(SelectedDates selectedDates){
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.getDateInstance().format(selectedDates.getSelectedDate());
    }

    public int getWeekOfMonth() {
        return this.calendar.get(Calendar.WEEK_OF_MONTH);

    }

    public int getDayOfWeek() {
        return this.calendar.get(Calendar.DAY_OF_WEEK);
    }

    public Calendar getCalendar() {
        return this.calendar;
    }


    public int getDateOfWeekInMonth() {

        return this.calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    //TODO(3) might need to modify the method
    public static ArrayList<SelectedDates> createCleaningDateList(int numdates, int year, int month, int day) {
        //TODO(4) new ArrayList<SelectedDates>()
        ArrayList<SelectedDates> cleaningDates = new ArrayList<>();
        for (int i = 0; i <= numdates; i++) {
            cleaningDates.add(new SelectedDates(year, month, day));
        }
        return cleaningDates;
    }
}


