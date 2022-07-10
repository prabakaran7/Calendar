package org.tem.calendar.model;

import androidx.annotation.NonNull;

public class MonthData {
    private String date;
    private int year;
    private int month;
    private int day;
    private int tyear;
    private int tmonth;
    private int tday;
    private int weekday;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTyear() {
        return tyear;
    }

    public void setTyear(int tyear) {
        this.tyear = tyear;
    }

    public int getTmonth() {
        return tmonth;
    }

    public void setTmonth(int tmonth) {
        this.tmonth = tmonth;
    }

    public int getTday() {
        return tday;
    }

    public void setTday(int tday) {
        this.tday = tday;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    @NonNull
    @Override
    public String toString() {
        return "MonthData{" +
                "date='" + date + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", tyear=" + tyear +
                ", tmonth=" + tmonth +
                ", tday=" + tday +
                ", weekday=" + weekday +
                '}';
    }
}
