package com.kylezhudev.moveurcars.util;

import com.kylezhudev.moveurcars.model.SelectedDates;

import java.util.Calendar;

/**
 * Created by Kyle on 2/5/2017.
 */

public class FirstDayChecker {
    private Calendar calendar;
    private SelectedDates selectedDates;

    public FirstDayChecker(SelectedDates selectedDates) {
        this.calendar = Calendar.getInstance();
        this.selectedDates = selectedDates;
    }

    public Boolean isSunday(){
        System.out.print(calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));

        return true;


    }


}
