package org.tem.calendar.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MuhurthamData {

    private final LocalDate date;
    private int tday;
    private int tmonth;
    private int thiti;
    private int star;
    private int yogam;
    private String time;
    private int laknam;

    public MuhurthamData(String date) {
        this.date = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTday() {
        return tday;
    }

    public void setTday(int tday) {
        this.tday = tday;
    }

    public int getTmonth() {
        return tmonth;
    }

    public void setTmonth(int tmonth) {
        this.tmonth = tmonth;
    }

    public int getThiti() {
        return thiti;
    }

    public void setThiti(int thiti) {
        this.thiti = thiti;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getYogam() {
        return yogam;
    }

    public void setYogam(int yogam) {
        this.yogam = yogam;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLaknam() {
        return laknam;
    }

    public void setLaknam(int laknam) {
        this.laknam = laknam;
    }

    public boolean isValarpirai() {
        return thiti < 16;
    }

    @Override
    public String toString() {
        return "MuhurthamData{" +
                "date=" + date +
                ", tday=" + tday +
                ", tmonth=" + tmonth +
                ", thiti=" + thiti +
                ", star=" + star +
                ", yogam=" + yogam +
                ", time='" + time + '\'' +
                ", laknam=" + laknam +
                '}';
    }
}
