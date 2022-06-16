package org.tem.calendar.model;

public class VasthuData extends MonthData{
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "VasthuData{" +
                "time='" + time + '\'' +
                "} " + super.toString();
    }
}
