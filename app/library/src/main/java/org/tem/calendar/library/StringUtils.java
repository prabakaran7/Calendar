package org.tem.calendar.library;

public class StringUtils {
    public static boolean isBlank(String txt){
        return null == txt || txt.trim().length() == 0;
    }
}
