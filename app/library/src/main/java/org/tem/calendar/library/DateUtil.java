package org.tem.calendar.library;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DateUtil {

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
}
