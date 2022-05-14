package org.tem.calendar.model;

public class NallaNeramData {
    private final String date;
    private String nallaNeramM;
    private String nallaNeramE;
    private String gowriM;
    private String gowriE;
    private int laknam;
    private String laknamTime;
    private String sunRise;

    public NallaNeramData(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getNallaNeramM() {
        return nallaNeramM;
    }

    public void setNallaNeramM(String nallaNeramM) {
        this.nallaNeramM = nallaNeramM;
    }

    public String getNallaNeramE() {
        return nallaNeramE;
    }

    public void setNallaNeramE(String nallaNeramE) {
        this.nallaNeramE = nallaNeramE;
    }

    public String getGowriM() {
        return gowriM;
    }

    public void setGowriM(String gowriM) {
        this.gowriM = gowriM;
    }

    public String getGowriE() {
        return gowriE;
    }

    public void setGowriE(String gowriE) {
        this.gowriE = gowriE;
    }

    public int getLaknam() {
        return laknam;
    }

    public void setLaknam(int laknam) {
        this.laknam = laknam;
    }

    public String getLaknamTime() {
        return laknamTime;
    }

    public void setLaknamTime(String laknamTime) {
        this.laknamTime = laknamTime;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    @Override
    public String toString() {
        return "NallaNeramData{" +
                "date='" + date + '\'' +
                ", nallaNeramM='" + nallaNeramM + '\'' +
                ", nallaNeramE='" + nallaNeramE + '\'' +
                ", gowriM='" + gowriM + '\'' +
                ", gowriE='" + gowriE + '\'' +
                ", laknam=" + laknam +
                ", laknamTime='" + laknamTime + '\'' +
                ", sunRise='" + sunRise + '\'' +
                '}';
    }
}
