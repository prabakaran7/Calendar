package org.tem.calendar;

import android.app.Application;
import android.util.Pair;

import net.sqlcipher.database.SQLiteDatabase;

import org.tem.calendar.db.DBHelper;
import org.tem.calendar.library.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class CalendarApp extends Application {

    private static final int weekStartIndex = 7;
    private static final List<Pair<Integer, String>> weekDayNameList = new ArrayList<>();
    private static final List<Pair<Integer, String>> weekDayShortNameList = new ArrayList<>();

    public static List<Pair<Integer, String>> getWeekDayNameList() {
        return weekDayNameList;
    }

    public static List<Pair<Integer, String>> getWeekDayShortNameList() {
        return weekDayShortNameList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase.loadLibs(this);
        DBHelper.getInstance(this);
        if (weekDayNameList.isEmpty()) {
            weekDayNameList.addAll(DateUtil.getNormalizedWeeks(weekStartIndex, getResources().getStringArray(R.array.weekday_names)));
        }

        if (weekDayShortNameList.isEmpty()) {
            weekDayShortNameList.addAll(DateUtil.getNormalizedWeeks(weekStartIndex, getResources().getStringArray(R.array.weekday_short_names)));
        }

    }

    //GOWRI TYPE

//     0 - ரோகம்
//     1 - சோரம்
//     2 - விஷம்
//     3 - லாபம்
//     4 - தனம்
//     5 - சுகம்
//     6 - உத்தி
//     7 - அமிர்த

    @Override
    public void onTerminate() {
        DBHelper.getInstance(this).close();
        super.onTerminate();
    }
}
