package org.tem.calendar.library;

import java.time.LocalDate;

public class DateModel {

    private LocalDate date;
    private int primeDay;
    private int secondaryDay;
    private int tithi;
    private int fav1;
    private int fav2;

    public void setPrimeDay(int primeDay) {
        this.primeDay = primeDay;
    }

    public void setSecondaryDay(int secondaryDay) {
        this.secondaryDay = secondaryDay;
    }

    public void setTithi(int tithi) {
        this.tithi = tithi;
    }

    public void setFav1(int fav1) {
        this.fav1 = fav1;
    }

    public void setFav2(int fav2) {
        this.fav2 = fav2;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isToday(){
        return date.isEqual(LocalDate.now());
    }

    public int getPrimeDay() {
        return primeDay;
    }

    public int getSecondaryDay() {
        return secondaryDay;
    }

    public int getTithi() {
        return tithi;
    }

    public int getFav1() {
        return fav1;
    }

    public int getFav2() {
        return fav2;
    }

    @Override
    public String toString() {
        return "DateModel{" +
                "date=" + date +
                ", primeDay=" + primeDay +
                ", secondaryDay=" + secondaryDay +
                ", tithi=" + tithi +
                ", fav1=" + fav1 +
                ", fav2=" + fav2 +
                '}';
    }
}
