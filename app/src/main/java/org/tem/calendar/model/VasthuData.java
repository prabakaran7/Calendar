package org.tem.calendar.model;

import androidx.annotation.NonNull;

public class VasthuData extends MonthData{
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "VasthuData{" +
                "time='" + time + '\'' +
                "} " + super.toString();
    }
}
