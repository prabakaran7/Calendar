package org.tem.calendar.util;

public class RasiViewData {

    private int imageResourceId;
    private String label;
    private String value;

    public RasiViewData(int imageResourceId, String label, String value) {
        this.imageResourceId = imageResourceId;
        this.label = label;
        this.value = value;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString(String character) {
        return label + character + " " + value;
    }
}
