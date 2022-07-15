package org.tem.calendar.custom;

import android.util.Pair;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DateUtil {
    private static final LocalTime midNight = LocalTime.of(0, 0);
    private static final LocalTime adhiKaalai = LocalTime.of(5, 59);
    private static final LocalTime kaalai = LocalTime.of(11, 59);
    private static final LocalTime pirpagal = LocalTime.of(15, 59);
    private static final LocalTime maalai = LocalTime.of(21, 59);
    public static final String DATE_FORMAT = "dd-MM-yyyy";


    public static String expandedTime(String time) {

        LocalTime lt = LocalTime.parse(time.replace(".", ":"), new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("h:mm a").toFormatter());
        String returnValue;
        if (lt.isBefore(adhiKaalai)) {
            returnValue= "அதிகாலை";
        } else if (lt.isBefore(kaalai)) {
            returnValue= "காலை";
        } else if (lt.isBefore(pirpagal)) {
            returnValue = "பிற்பகல்";
        } else if (lt.isBefore(maalai)) {
            returnValue = "மாலை";
        } else {
            returnValue = "இரவு";
        }
        return returnValue+" " + lt.format(DateTimeFormatter.ofPattern("h.mm"));
    }

    public static int[] naazhigaiToHourMin(int naazhigai) {
        int minutes = naazhigai * 24;
        int[] result = new int[2];
        result[0] = minutes / 60;
        result[1] = minutes % 60;
        return result;
    }

    public static List<Pair<Integer, String>> getNormalizedWeeks(int weekStartIndex, String[] weeksRaw) {
        List<Pair<Integer, String>> weeks = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int wi = weekStartIndex + i;
            if (wi > 7) {
                wi -= 7;
            }

            weeks.add(new Pair<>(wi, weeksRaw[wi - 1]));
        }
        return weeks;
    }

    public static Collection<Integer> getWeekEnds(int weekStartIndex, List<Integer> weekEndList) {
        List<Integer> weekEnds = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int wi = weekStartIndex + i;
            if (wi > 7) {
                wi -= 7;
            }
            if (weekEndList.contains(wi)) {
                weekEnds.add(i + 1);
            }

        }
        return weekEnds;
    }

    public static LocalDate ofLocalDate(String date) {
        return ofLocalDate(date, DATE_FORMAT);
    }

    public static LocalDate ofLocalDate(String date, String pattern){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    public static String format(LocalDate date) {
        return format(date, DATE_FORMAT);
    }

    public static String format(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
}
