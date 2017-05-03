package com.kylezhudev.moveurcars.model;


import java.util.Calendar;
import java.util.Date;

public class SelectedDates extends Date {
    private Date selectedDate;
    private int dayOfWeekInMonth;
    private int dayOfWeek;
    private Calendar calendar;


    //TODO(2) selectedDate = null needs to be changed, add var hour and minute


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

    public int getDayOfWeeek() {
        return this.dayOfWeek;
    }

    public SelectedDates(int dayOfWeekInMonth, int dayOfWeek, int year) {
        this.dayOfWeekInMonth = dayOfWeekInMonth;
        this.dayOfWeek = dayOfWeek;
        this.calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, dayOfWeekInMonth);
        this.selectedDate = calendar.getTime();
    }


    public Calendar selectedCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, getDateOfWeekInMonth());
        calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek());
        return calendar;
    }

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

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public String getFormattedDate(SelectedDates selectedDates){
//        DateFormat dateFormat = DateFormat.getDateInstance();
//        return dateFormat.getDateInstance().format(selectedDates.getSelectedDate());
//    }

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


}


