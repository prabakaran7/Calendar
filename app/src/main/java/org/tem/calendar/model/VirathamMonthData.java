package org.tem.calendar.model;

public class VirathamMonthData extends VirathamData{
    private int tmonth;
    private int tday;
    private int pirai;


    public VirathamMonthData(String date) {
        super(date);
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

    public int getPirai() {
        return pirai;
    }

    public void setPirai(int pirai) {
        this.pirai = pirai;
    }

    @Override
    public String toString() {
        return "VirathamMonthData{" +
                "tmonth=" + tmonth +
                ", tday=" + tday +
                ", pirai=" + pirai +
                "} " + super.toString();
    }
}
