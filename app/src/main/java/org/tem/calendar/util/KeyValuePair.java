package org.tem.calendar.util;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class KeyValuePair implements Comparable<KeyValuePair> {

    private final LocalDate key;
    private final String value;

    public KeyValuePair(LocalDate key, String value) {
        this.key = key;
        this.value = value;
    }

    public LocalDate getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(KeyValuePair o) {
        return o.key.compareTo(this.key);
    }

    @NonNull
    @Override
    public String toString() {
        return "KeyValuePair{" +
                "key=" + key +
                ", value='" + value + '\'' +
                '}';
    }
}
