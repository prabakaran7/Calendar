package org.tem.calendar.util;

import java.util.ArrayList;
import java.util.List;

public class TextMessageBuilder {
    List<String> lines = new ArrayList<>();


    public void reset() {
        lines.clear();
    }

    public boolean append(String line) {
        return lines.add(line);
    }

    public void append(int index, String line) {

        lines.add(index, line);
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
