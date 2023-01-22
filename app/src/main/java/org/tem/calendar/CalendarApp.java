package org.tem.calendar;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;

import com.google.firebase.messaging.FirebaseMessaging;
import net.sqlcipher.database.SQLiteDatabase;
import org.tem.calendar.activities.DayActivity;
import org.tem.calendar.activities.FestivalIndexActivity;
import org.tem.calendar.activities.ManaiyadiSastharamActivity;
import org.tem.calendar.activities.MonthActivity;
import org.tem.calendar.activities.MonthVirathamActivity;
import org.tem.calendar.activities.MuhurthamActivity;
import org.tem.calendar.activities.PalliPalanActivity;
import org.tem.calendar.activities.PanchangamActivity;
import org.tem.calendar.activities.PoruthamActivity;
import org.tem.calendar.activities.RaghuEmaKuligaiActivity;
import org.tem.calendar.activities.SettingsActivity;
import org.tem.calendar.activities.VasthuActivity;
import org.tem.calendar.activities.YearActivity;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.MonthVirathamFragment;
import org.tem.calendar.fragment.PanchangamFragment;
import org.tem.calendar.model.Dashboard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CalendarApp extends Application {

    public static final LocalDate MAX_DATE = LocalDate.of(2023, 12, 31);
    public static final LocalDate MIN_DATE = LocalDate.of(2022, 1, 1);
    private static final int weekStartIndex = 7;
    private static final List<Pair<Integer, String>> weekDayNameList = new ArrayList<>();
    private static final List<Pair<Integer, String>> weekDayShortNameList = new ArrayList<>();
    private static final Map<String, List<Dashboard>> DASHBOARD_MAP = new LinkedHashMap<>();
    private static int MAX_QUOTE_NUMBER = -1;
    private static float factor = 1.0f;
    //private FirebaseAnalytics mFirebaseAnalytics;

    public static List<Pair<Integer, String>> getWeekDayNameList() {
        return weekDayNameList;
    }

    public static int getMaxQuoteNumber(Context context) {
        if (MAX_QUOTE_NUMBER < 0) {
            MAX_QUOTE_NUMBER = DBHelper.getInstance(context).getQuoteMaxNumber();
        }
        return MAX_QUOTE_NUMBER;
    }

    public static Map<String, List<Dashboard>> getCategoryMap() {
        return DASHBOARD_MAP;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        factor = getResources().getDisplayMetrics().density;
        // Initialize SQL Net Cipher Libraries
        SQLiteDatabase.loadLibs(this);
        DBHelper.getInstance(this);


        SharedPreferences pref = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        boolean generalSubscribed = pref.getBoolean(Constants.PREF_GENERAL_NOTIFICATIONS, true);
        if (generalSubscribed) {
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_CALENDAR);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_CALENDAR);
        }


        // Obtain the FirebaseAnalytics instance.
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        if (weekDayNameList.isEmpty()) {
            weekDayNameList.addAll(DateUtil.getNormalizedWeeks(weekStartIndex, getResources().getStringArray(R.array.weekday_names)));
        }

        if (weekDayShortNameList.isEmpty()) {
            weekDayShortNameList.addAll(DateUtil.getNormalizedWeeks(weekStartIndex, getResources().getStringArray(R.array.weekday_short_names)));
        }

        DASHBOARD_MAP.clear();
        DASHBOARD_MAP.put("", Arrays.asList(
                Dashboard.of(this, R.string.app_name, R.drawable.daily_calendar, DayActivity.class),
                Dashboard.of(this, R.string.monthly_calendar_txt, R.drawable.monthly_calendar, MonthActivity.class),
                Dashboard.of(this, R.string.year_calendar, R.drawable.year_calendar, YearActivity.class),
                Dashboard.of(this, R.string.viratha_thinangal, R.drawable.viratham_women, MonthVirathamActivity.class),
                Dashboard.of(this, R.string.muhurtham_days, R.drawable.wedding, MuhurthamActivity.class),
                Dashboard.of(this, R.string.holidaysAndFestivals, R.drawable.hindu_festival, FestivalIndexActivity.class),
                Dashboard.of(this, R.string.raaghuEmaKuligaiLabel, R.drawable.raghu, RaghuEmaKuligaiActivity.class),

                Dashboard.of(this, R.string.asuba_days, R.drawable.hotsun, MonthVirathamActivity.class, forBundlePair(Pair.create(Constants.EXTRA_TYPE, MonthVirathamFragment.ASUBA_VIRATHAM))),
                Dashboard.of(this, R.string.gowriPanchangamLabel, R.drawable.homagundam, PanchangamActivity.class, forBundlePair(Pair.create(Constants.EXTRA_PANCHANGAM, PanchangamFragment.GOWRI_PANCHANGAM))),
                Dashboard.of(this, R.string.graha_orai_label, R.drawable.kalasha, PanchangamActivity.class, forBundlePair(Pair.create(Constants.EXTRA_PANCHANGAM, PanchangamFragment.GRAHA_ORAI_PANCHANGAM))),
                Dashboard.of(this, R.string.palli_palan, R.drawable.palli, PalliPalanActivity.class),
                Dashboard.of(this, R.string.marriage_matching, R.drawable.wedding_match, PoruthamActivity.class)
        ));

        DASHBOARD_MAP.put(getString(R.string.vasthu), Arrays.asList(
                Dashboard.of(this, R.string.vasthu_days, R.drawable.vasthu_calendar, VasthuActivity.class),
                Dashboard.of(this, R.string.manaiyadi_label, R.drawable.manaiyadi_sastram, ManaiyadiSastharamActivity.class)
        ));

        DASHBOARD_MAP.put(getString(R.string.miscTxt), Arrays.asList(
                Dashboard.of(this, R.string.settings, android.R.drawable.ic_menu_preferences, SettingsActivity.class),
                Dashboard.of(this, R.string.privacy_policy, android.R.drawable.ic_lock_lock, null),
                Dashboard.of(this, R.string.otherApps, R.drawable.temtech, null),
                Dashboard.of(this, R.string.shareTxt, android.R.drawable.ic_menu_share, null)
        ));
    }

    @SafeVarargs
    private final Bundle forBundlePair(Pair<String, Object>... pairs) {
        Bundle bundle = new Bundle(pairs.length);
        for (Pair<String, Object> pair : pairs) {
            if (pair.second instanceof Integer) {
                bundle.putInt(pair.first, (Integer) pair.second);
            } else {
                bundle.putString(pair.first, pair.second.toString());
            }
        }
        return bundle;
    }

    @Override
    public void onTerminate() {
        DBHelper.getInstance(this).close();
        super.onTerminate();
    }

    public static float getDpFactor(){
        return factor;
    }
}
