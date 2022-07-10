package org.tem.calendar.model;

import androidx.annotation.NonNull;

import java.time.DayOfWeek;

public class WeekData {
    private DayOfWeek dayOfWeek;
    private String raaghu;
    private String ema;
    private String kuligai;
    private int soolam;
    private int parigaram;
    private int soolamTime;
    private String karanan;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getRaaghu() {
        return raaghu;
    }

    public void setRaaghu(String raaghu) {
        this.raaghu = raaghu;
    }

    public String getEma() {
        return ema;
    }

    public void setEma(String ema) {
        this.ema = ema;
    }

    public String getKuligai() {
        return kuligai;
    }

    public void setKuligai(String kuligai) {
        this.kuligai = kuligai;
    }

    public int getSoolam() {
        return soolam;
    }

    public void setSoolam(int soolam) {
        this.soolam = soolam;
    }

    public int getParigaram() {
        return parigaram;
    }

    public void setParigaram(int parigaram) {
        this.parigaram = parigaram;
    }

    public int getSoolamTime() {
        return soolamTime;
    }

    public void setSoolamTime(int soolamTime) {
        this.soolamTime = soolamTime;
    }

    public String getKaranan() {
        return karanan;
    }

    public void setKaranan(String karanan) {
        this.karanan = karanan;
    }

    @NonNull
    @Override
    public String toString() {
        return "WeekData{" +
                "dayOfWeek=" + dayOfWeek +
                ", raaghu='" + raaghu + '\'' +
                ", ema='" + ema + '\'' +
                ", kuligai='" + kuligai + '\'' +
                ", soolam='" + soolam + '\'' +
                ", parigaram='" + parigaram + '\'' +
                ", soolamTime='" + soolamTime + '\'' +
                ", karanan='" + karanan + '\'' +
                '}';
    }
}
