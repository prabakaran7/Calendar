package org.tem.calendar.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isBlank(String txt) {
        return null == txt || txt.trim().length() == 0;
    }

    public static String join(Collection<?> collection, String delimiter) {
        StringBuilder sb = new StringBuilder();
        List<?> objectList = new ArrayList<>(new HashSet<>(collection));
        for (int index = 0; index < objectList.size(); index++) {
            sb.append(objectList.get(index).toString());
            if (index != objectList.size() - 1) {
                sb.append(delimiter);
            }
        }

        return sb.toString();
    }

    public static boolean isNotBlank(String txt) {
        return !isBlank(txt);
    }
}
