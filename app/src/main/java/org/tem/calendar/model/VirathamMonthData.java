package org.tem.calendar.model;

public class VirathamMonthData extends VirathamData{
    private int tmonth;
    private int tday;

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

    @Override
    public String toString() {
        return "VirathamMonthData{" +
                "tmonth=" + tmonth +
                ", tday=" + tday +
                "} " + super.toString();
    }
}
