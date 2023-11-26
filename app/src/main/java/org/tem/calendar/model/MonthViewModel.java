package org.tem.calendar.model;

import org.tem.calendar.CalendarApp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthViewModel {

    private final List<LocalDate> list = new ArrayList<>();
    private static final int SIZE = 5;

    public MonthViewModel(final LocalDate date){
        LocalDate localDate = date.withDayOfMonth(1);
        for(int st = -2; st <= 2; st++){
            list.add(localDate.plusMonths(st));
        }
    }

    public boolean shiftForward(){
        LocalDate date = list.get(list.size() - 1);
        if(date.getYear() == CalendarApp.MAX_DATE.getYear() && date.getMonthValue() > CalendarApp.MAX_DATE.getMonthValue()) {
            return false;
        }
        list.clear();
        for(int index = 0; index < SIZE; index++){
            LocalDate ld = date.plusMonths(index);
            if(ld.isEqual(CalendarApp.MAX_DATE) || ld.isBefore(CalendarApp.MAX_DATE)) {
                list.add(ld);
            }
        }

        return true;

    }

    public boolean shiftBackward(){
        LocalDate date = list.get(0);
        if(date.getYear() == CalendarApp.MIN_DATE.getYear() && date.getMonthValue() < CalendarApp.MIN_DATE.getMonthValue()) {
            return false;
        }
        list.clear();
        for(int index = SIZE - 1; index >= 0; index--){
            LocalDate ld = date.minusMonths(index);
            if(ld.isEqual(CalendarApp.MIN_DATE) || ld.isAfter(CalendarApp.MIN_DATE)) {
                list.add(ld);
            }
        }
        return true;
    }

    public List<LocalDate> getList(){
        return list;
    }
}
