package org.tem.calendar.model;

public class FestivalDayData {
    private final String date;
    private String hindhu;
    private String muslims;
    private String christ;
    private String govt;
    private boolean isLeave;
    private String important;

    public FestivalDayData(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getHindhu() {
        return hindhu;
    }

    public void setHindhu(String hindhu) {
        this.hindhu = hindhu;
    }

    public String getMuslims() {
        return muslims;
    }

    public void setMuslims(String muslims) {
        this.muslims = muslims;
    }

    public String getChrist() {
        return christ;
    }

    public void setChrist(String christ) {
        this.christ = christ;
    }

    public String getGovt() {
        return govt;
    }

    public void setGovt(String govt) {
        this.govt = govt;
    }

    public boolean isLeave() {
        return isLeave;
    }

    public void setLeave(boolean leave) {
        isLeave = leave;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    @Override
    public String toString() {
        return "FestivalDayData{" +
                "date='" + date + '\'' +
                ", hindhu='" + hindhu + '\'' +
                ", muslims='" + muslims + '\'' +
                ", christ='" + christ + '\'' +
                ", govt='" + govt + '\'' +
                ", isLeave=" + isLeave +
                ", important='" + important + '\'' +
                '}';
    }
}
