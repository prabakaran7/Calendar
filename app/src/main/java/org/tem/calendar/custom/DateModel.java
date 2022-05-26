package org.tem.calendar.custom;

import java.time.LocalDate;

public class DateModel {

    private LocalDate date;
    private int primeDay;
    private int secondaryDay;
    private int tithi = -1;
    private int star = -1;
    private int muhurtham = -1;
    private int special = -1;
    private boolean isHoliday;

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPrimeDay() {
        return primeDay;
    }

    public void setPrimeDay(int primeDay) {
        this.primeDay = primeDay;
    }

    public int getSecondaryDay() {
        return secondaryDay;
    }

    public void setSecondaryDay(int secondaryDay) {
        this.secondaryDay = secondaryDay;
    }

    public int getTithi() {
        return tithi;
    }

    public void setTithi(int tithi) {
        this.tithi = tithi;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getMuhurtham() {
        return muhurtham;
    }

    public void setMuhurtham(int muhurtham) {
        this.muhurtham = muhurtham;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    @Override
    public String toString() {
        return "DateModel{" +
                "date=" + date +
                ", primeDay=" + primeDay +
                ", secondaryDay=" + secondaryDay +
                ", tithi=" + tithi +
                ", star=" + star +
                ", muhurtham=" + muhurtham +
                ", special=" + special +
                '}';
    }

    public boolean isToday() {

        return date.isEqual(LocalDate.now());
    }
}
