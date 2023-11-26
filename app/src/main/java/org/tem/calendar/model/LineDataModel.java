package org.tem.calendar.model;

import androidx.annotation.NonNull;

public class LineDataModel {
    public static final LineDataModel EMPTY = new LineDataModel("", "", "", "");
    private final String fontStyle;

    private final String weight;
    private final String alignment;
    private final String text;

    private LineDataModel(String fontStyle, String weight, String alignment, String text) {
        this.fontStyle = fontStyle;
        this.weight = weight;
        this.alignment = alignment;
        this.text = text;
    }

    @NonNull
    public static LineDataModel of(String alignment, String fontStyle, String weight, String text) {
        return new LineDataModel(fontStyle, weight, alignment, text);
    }

    @NonNull
    public static LineDataModel of(String line) {
        return new LineDataModel("", "", "", line);
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public String getWeight() {
        return weight;
    }

    public String getAlignment() {
        return alignment;
    }

    public String getText() {
        return text;
    }

    @NonNull
    @Override
    public String toString() {
        return "LineDataModel{" +
                ", fontStyle=" + fontStyle +
                ", weight=" + weight +
                ", alignment=" + alignment +
                ", text='" + text + '\'' +
                '}';
    }
}
