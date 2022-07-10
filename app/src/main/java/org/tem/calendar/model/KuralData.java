package org.tem.calendar.model;

import androidx.annotation.NonNull;

public class KuralData {
    private int id;
    private String iyalkal;
    private String paakal;
    private String athigaram;
    private int athigaramNum;
    private String kural;
    private String muVaUrai;
    private String salamanUrai;
    private String karunaUrai;
    private String parimelUrai;
    private String iyalkalEn;
    private String paakalEn;
    private String athigaramEn;
    private String kuralEn;
    private String explanation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIyalkal() {
        return iyalkal;
    }

    public void setIyalkal(String iyalkal) {
        this.iyalkal = iyalkal;
    }

    public String getPaakal() {
        return paakal;
    }

    public void setPaakal(String paakal) {
        this.paakal = paakal;
    }

    public String getAthigaram() {
        return athigaram;
    }

    public void setAthigaram(String athigaram) {
        this.athigaram = athigaram;
    }

    public int getAthigaramNum() {
        return athigaramNum;
    }

    public void setAthigaramNum(int athigaramNum) {
        this.athigaramNum = athigaramNum;
    }

    public String getKural() {
        return kural;
    }

    public void setKural(String kural) {
        this.kural = kural;
    }

    public String getMuVaUrai() {
        return muVaUrai;
    }

    public void setMuVaUrai(String muVaUrai) {
        this.muVaUrai = muVaUrai;
    }

    public String getSalamanUrai() {
        return salamanUrai;
    }

    public void setSalamanUrai(String salamanUrai) {
        this.salamanUrai = salamanUrai;
    }

    public String getKarunaUrai() {
        return karunaUrai;
    }

    public void setKarunaUrai(String karunaUrai) {
        this.karunaUrai = karunaUrai;
    }

    public String getParimelUrai() {
        return parimelUrai;
    }

    public void setParimelUrai(String parimelUrai) {
        this.parimelUrai = parimelUrai;
    }

    public String getIyalkalEn() {
        return iyalkalEn;
    }

    public void setIyalkalEn(String iyalkalEn) {
        this.iyalkalEn = iyalkalEn;
    }

    public String getPaakalEn() {
        return paakalEn;
    }

    public void setPaakalEn(String paakalEn) {
        this.paakalEn = paakalEn;
    }

    public String getAthigaramEn() {
        return athigaramEn;
    }

    public void setAthigaramEn(String athigaramEn) {
        this.athigaramEn = athigaramEn;
    }

    public String getKuralEn() {
        return kuralEn;
    }

    public void setKuralEn(String kuralEn) {
        this.kuralEn = kuralEn;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @NonNull
    @Override
    public String toString() {
        return "KuralData{" +
                "id=" + id +
                ", iyalkal='" + iyalkal + '\'' +
                ", paakal='" + paakal + '\'' +
                ", athigaram='" + athigaram + '\'' +
                ", athigaramNum=" + athigaramNum +
                ", kural='" + kural + '\'' +
                ", muVaUrai='" + muVaUrai + '\'' +
                ", salamanUrai='" + salamanUrai + '\'' +
                ", karunaUrai='" + karunaUrai + '\'' +
                ", parimelUrai='" + parimelUrai + '\'' +
                ", iyalkalEn='" + iyalkalEn + '\'' +
                ", paakalEn='" + paakalEn + '\'' +
                ", athigaramEn='" + athigaramEn + '\'' +
                ", kuralEn='" + kuralEn + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
