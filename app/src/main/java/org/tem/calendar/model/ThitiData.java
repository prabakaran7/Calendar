package org.tem.calendar.model;

public class ThitiData {
    private final String date;
    private String time1;
    private String time2;
    private int thiti1;
    private int thiti2;
    private int thiti3;
    private int thiti;
    private int pirai;

    public ThitiData(String date) {
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

    public int getThiti1() {
        return thiti1;
    }

    public void setThiti1(int thiti1) {
        this.thiti1 = thiti1;
    }

    public int getThiti2() {
        return thiti2;
    }

    public void setThiti2(int thiti2) {
        this.thiti2 = thiti2;
    }

    public int getThiti3() {
        return thiti3;
    }

    public void setThiti3(int thiti3) {
        this.thiti3 = thiti3;
    }

    public int getThiti() {
        return thiti;
    }

    public void setThiti(int thiti) {
        this.thiti = thiti;
    }

    public int getPirai() {
        return pirai;
    }

    public void setPirai(int pirai) {
        this.pirai = pirai;
    }

    @Override
    public String toString() {
        return "ThitiData{" +
                "date='" + date + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", thiti1=" + thiti1 +
                ", thiti2=" + thiti2 +
                ", thiti3=" + thiti3 +
                ", thiti=" + thiti +
                ", pirai=" + pirai +
                '}';
    }
}
