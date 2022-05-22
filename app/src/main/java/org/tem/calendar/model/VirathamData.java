package org.tem.calendar.model;

public class VirathamData {
    private final String date;
    private int viratham;
    private String timing;

    public VirathamData(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getViratham() {
        return viratham;
    }

    public void setViratham(int viratham) {
        this.viratham = viratham;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    @Override
    public String toString() {
        return "VirathamData{" +
                "date='" + date + '\'' +
                ", type=" + viratham +
                ", timing='" + timing + '\'' +
                '}';
    }
}
