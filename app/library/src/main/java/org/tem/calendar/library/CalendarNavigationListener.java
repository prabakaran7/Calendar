package org.tem.calendar.library;

import java.time.LocalDate;

public interface CalendarNavigationListener {

    void previousClicked(DateHolder dateHolder);
    void nextClicked(DateHolder dateHolder);
}
