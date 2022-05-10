package org.tem.calendar.model;

import java.time.DayOfWeek;

public class PanchangamData implements Comparable<PanchangamData> {
    private final DayOfWeek weekDay;
    private int timeIndex;
    private String timeIndexText;
    private int morningIndex;
    private String morningText;
    private int eveningIndex;
    private String eveningText;

    public PanchangamData(DayOfWeek weekDay) {
        this.weekDay = weekDay;
    }

    public DayOfWeek getWeekDay() {
        return weekDay;
    }

    public int getTimeIndex() {
        return timeIndex;
    }

    public void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }

    public String getTimeIndexText() {
        return timeIndexText;
    }

    public void setTimeIndexText(String timeIndexText) {
        this.timeIndexText = timeIndexText;
    }

    public int getMorningIndex() {
        return morningIndex;
    }

    public void setMorningIndex(int morningIndex) {
        this.morningIndex = morningIndex;
    }

    public String getMorningText() {
        return morningText;
    }

    public void setMorningText(String morningText) {
        this.morningText = morningText;
    }

    public int getEveningIndex() {
        return eveningIndex;
    }

    public void setEveningIndex(int eveningIndex) {
        this.eveningIndex = eveningIndex;
    }

    public String getEveningText() {
        return eveningText;
    }

    public void setEveningText(String eveningText) {
        this.eveningText = eveningText;
    }

    @Override
    public int compareTo(PanchangamData o) {
        return Integer.compare(this.timeIndex, o.timeIndex);
    }
}
