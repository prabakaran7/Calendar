package org.tem.calendar.model;

public class YogamData {
    private final String date;
    private String time1;
    private String time2;
    private int yogam1;
    private int yogam2;
    private int yogam3;
    private int yogam;

    public YogamData(String date) {
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

    public int getYogam1() {
        return yogam1;
    }

    public void setYogam1(int yogam1) {
        this.yogam1 = yogam1;
    }

    public int getYogam2() {
        return yogam2;
    }

    public void setYogam2(int yogam2) {
        this.yogam2 = yogam2;
    }

    public int getYogam3() {
        return yogam3;
    }

    public void setYogam3(int yogam3) {
        this.yogam3 = yogam3;
    }

    public int getYogam() {
        return yogam;
    }

    public void setYogam(int yogam) {
        this.yogam = yogam;
    }

    @Override
    public String toString() {
        return "YogamData{" +
                "date='" + date + '\'' +
                ", time1='" + time1 + '\'' +
                ", time2='" + time2 + '\'' +
                ", yogam1=" + yogam1 +
                ", yogam2=" + yogam2 +
                ", yogam3=" + yogam3 +
                ", yogam=" + yogam +
                '}';
    }
}
