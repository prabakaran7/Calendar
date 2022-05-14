package org.tem.calendar.model;

public class RasiData {
    private final String date;
    private int mesham;
    private int rishabam;
    private int mithunam;
    private int kadagam;
    private int simmam;
    private int kanni;
    private int thulam;
    private int viruchagam;
    private int dhanusu;
    private int magaram;
    private int kumbam;
    private int meenam;

    public RasiData(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public int getMesham() {
        return mesham;
    }

    public void setMesham(int mesham) {
        this.mesham = mesham;
    }

    public int getRishabam() {
        return rishabam;
    }

    public void setRishabam(int rishabam) {
        this.rishabam = rishabam;
    }

    public int getMithunam() {
        return mithunam;
    }

    public void setMithunam(int mithunam) {
        this.mithunam = mithunam;
    }

    public int getKadagam() {
        return kadagam;
    }

    public void setKadagam(int kadagam) {
        this.kadagam = kadagam;
    }

    public int getSimmam() {
        return simmam;
    }

    public void setSimmam(int simmam) {
        this.simmam = simmam;
    }

    public int getKanni() {
        return kanni;
    }

    public void setKanni(int kanni) {
        this.kanni = kanni;
    }

    public int getThulam() {
        return thulam;
    }

    public void setThulam(int thulam) {
        this.thulam = thulam;
    }

    public int getViruchagam() {
        return viruchagam;
    }

    public void setViruchagam(int viruchagam) {
        this.viruchagam = viruchagam;
    }

    public int getDhanusu() {
        return dhanusu;
    }

    public void setDhanusu(int dhanusu) {
        this.dhanusu = dhanusu;
    }

    public int getMagaram() {
        return magaram;
    }

    public void setMagaram(int magaram) {
        this.magaram = magaram;
    }

    public int getKumbam() {
        return kumbam;
    }

    public void setKumbam(int kumbam) {
        this.kumbam = kumbam;
    }

    public int getMeenam() {
        return meenam;
    }

    public void setMeenam(int meenam) {
        this.meenam = meenam;
    }

    @Override
    public String toString() {
        return "RasiData{" +
                "date='" + date + '\'' +
                ", mesham=" + mesham +
                ", rishabam=" + rishabam +
                ", mithunam=" + mithunam +
                ", kadagam=" + kadagam +
                ", simmam=" + simmam +
                ", kanni=" + kanni +
                ", thulam=" + thulam +
                ", viruchagam=" + viruchagam +
                ", dhanusu=" + dhanusu +
                ", magaram=" + magaram +
                ", kumbam=" + kumbam +
                ", meenam=" + meenam +
                '}';
    }
}
