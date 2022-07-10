package org.tem.calendar.model;

import androidx.annotation.NonNull;

public class KaranamData {
    private final String date;
    private String time1;
    private String time2;
    private String time3;
    private int kara1;
    private int kara2;
    private int kara3;
    private int kara4;

    public KaranamData(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public int getKara1() {
        return kara1;
    }

    public void setKara1(int kara1) {
        this.kara1 = kara1;
    }

    public int getKara2() {
        return kara2;
    }

    public void setKara2(int kara2) {
        this.kara2 = kara2;
    }

    public int getKara3() {
        return kara3;
    }

    public void setKara3(int kara3) {
        this.kara3 = kara3;
    }

    public int getKara4() {
        return kara4;
    }

    public void setKara4(int kara4) {
        this.kara4 = kara4;
    }

    @NonNull
    @Override
    public String toString() {
        return "KaranamData{" +
                "date='" + date + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", time3='" + time3 + '\'' +
                ", kara1='" + kara1 + '\'' +
                ", kara2='" + kara2 + '\'' +
                ", kara3='" + kara3 + '\'' +
                ", kara4='" + kara4 + '\'' +
                '}';
    }
}
