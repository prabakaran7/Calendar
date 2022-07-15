package org.tem.calendar;

import android.app.Application;
import android.content.Context;
import android.util.Pair;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import net.sqlcipher.database.SQLiteDatabase;

import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class CalendarApp extends Application {

    private static final int weekStartIndex = 7;
    private static final List<Pair<Integer, String>> weekDayNameList = new ArrayList<>();
    private static final List<Pair<Integer, String>> weekDayShortNameList = new ArrayList<>();
    private static int MAX_QUOTE_NUMBER = -1;
    private FirebaseAnalytics mFirebaseAnalytics;

    public static List<Pair<Integer, String>> getWeekDayNameList() {
        return weekDayNameList;
    }

    public static List<Pair<Integer, String>> getWeekDayShortNameList() {
        return weekDayShortNameList;
    }

    public static int getMaxQuoteNumber(Context context) {
        if (MAX_QUOTE_NUMBER < 0) {
            MAX_QUOTE_NUMBER = DBHelper.getInstance(context).getQuoteMaxNumber();
        }
        return MAX_QUOTE_NUMBER;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //FirebaseMessaging.getInstance().subscribeToTopic("calendar");
        //FirebaseMessaging.getInstance().unsubscribeFromTopic("calendar");

        SQLiteDatabase.loadLibs(this);
        DBHelper.getInstance(this);
        // Obtain the FirebaseAnalytics instance.
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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
