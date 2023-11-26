package org.tem.calendar.model;

import org.tem.calendar.CalendarApp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DayViewModel {

    private static final int SIZE = 5;
    private final List<LocalDate> list = new ArrayList<>();

    public DayViewModel(LocalDate date) {
        for (int st = -2; st <= 2; st++) {
            list.add(date.plusDays(st));
        }
    }

    public boolean shiftForward() {
        LocalDate date = list.get(list.size() - 1);
        if (date.isAfter(CalendarApp.MAX_DATE)) {
            return false;
        }
        list.clear();
        for (int index = 0; index < SIZE; index++) {
            LocalDate ld = date.plusDays(index);
            if (!ld.isAfter(CalendarApp.MAX_DATE)) {
                list.add(ld);
            }
        }
        return true;
    }

    public boolean shiftBackward() {
        LocalDate date = list.get(0);
        if (date.isBefore(CalendarApp.MIN_DATE)) {
            return false;
        }
        list.clear();
        for (int index = SIZE - 1; index >= 0; index--) {
            LocalDate ld = date.minusDays(index);
            if (!ld.isBefore(CalendarApp.MIN_DATE)) {
                list.add(ld);
            }
        }

        return true;
    }

    public List<LocalDate> getList() {
        return list;
    }
}
