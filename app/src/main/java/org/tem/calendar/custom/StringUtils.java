package org.tem.calendar.custom;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isBlank(String txt) {
        return null == txt || txt.isBlank();
    }

    @NonNull
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

    public static boolean isDigits(String input) {
        if(null == input || input.isBlank()) {
            return false;
        }
        for(char c : input.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }

        return true;
    }
}
