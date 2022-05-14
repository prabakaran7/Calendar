package org.tem.calendar.model;

public class StarData {
    private final String date;
    private String time1;
    private String time2;
    private int star1;
    private int star2;
    private int star3;
    private int star;
    private int nokku;
    private String chandrastamam;

    public StarData(String date) {
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

    public int getStar1() {
        return star1;
    }

    public void setStar1(int star1) {
        this.star1 = star1;
    }

    public int getStar2() {
        return star2;
    }

    public void setStar2(int star2) {
        this.star2 = star2;
    }

    public int getStar3() {
        return star3;
    }

    public void setStar3(int star3) {
        this.star3 = star3;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getNokku() {
        return nokku;
    }

    public void setNokku(int nokku) {
        this.nokku = nokku;
    }

    public String getChandrastamam() {
        return chandrastamam;
    }

    public void setChandrastamam(String chandrastamam) {
        this.chandrastamam = chandrastamam;
    }
}
