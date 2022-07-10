package org.tem.calendar.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.tem.calendar.R;
import org.tem.calendar.activities.DailyActivity;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.MonthData;

import java.time.LocalDate;

/**
 * Implementation of App Widget functionality.
 */
public class SimpleDayAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, DailyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        LocalDate selectedDate = LocalDate.now();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.simple_day_app_widget);
        views.setOnClickPendingIntent(R.id.rootView, pendingIntent);
        views.setTextViewText(R.id.dateTxt, DateUtil.format(selectedDate));
        views.setTextViewText(R.id.monthDayTxt, monthText(context, selectedDate));

        MonthData md = DBHelper.getInstance(context).getDate(selectedDate);
        if (null != md) {
            views.setTextViewText(R.id.tamilYearTxt, context.getResources().getStringArray(R.array.tamil_year_names)[md.getTyear() - 1]);
            views.setTextViewText(R.id.tamilMonthTxt, context.getResources().getStringArray(R.array.tamizh_month_names)[md.getTmonth() - 1]);
            views.setTextViewText(R.id.tamilDateTxt, "" + md.getTday());
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static String monthText(Context context, LocalDate selectedDate) {
        String[] monthNames = context.getResources().getStringArray(R.array.en_month_names);
        String[] weekDayNames = context.getResources().getStringArray(R.array.weekday_names);

        return monthNames[selectedDate.getMonthValue() - 1] + " - " + weekDayNames[selectedDate.getDayOfWeek().getValue() - 1];
    }

}