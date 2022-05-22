package org.tem.calendar.custom;

import java.time.LocalDate;

public class DateHolder {
    private final int year;
    private final int month;
    private boolean isPrime = true;

    public DateHolder(LocalDate date){
        this(date.getYear(), date.getMonthValue());
    }
    public DateHolder(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public boolean isPrime() {
        return isPrime;
    }

    public void setPrime(boolean prime) {
        isPrime = prime;
    }

    public LocalDate getDate(){
        return LocalDate.of(year, month, 1);
    }
}
