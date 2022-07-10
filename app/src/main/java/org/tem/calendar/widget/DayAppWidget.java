package org.tem.calendar.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import org.tem.calendar.R;
import org.tem.calendar.activities.DailyActivity;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.FestivalDayData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.NallaNeramData;
import org.tem.calendar.model.VirathamData;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class DayAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, DailyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        LocalDate selectedDate = LocalDate.now();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.day_app_widget);
        views.setOnClickPendingIntent(R.id.rootView, pendingIntent);
        views.setTextViewText(R.id.dateTxt, DateUtil.format(selectedDate));
        views.setTextViewText(R.id.monthDayTxt, monthText(context, selectedDate));

        MonthData md = DBHelper.getInstance(context).getDate(selectedDate);
        if (null != md) {
            views.setTextViewText(R.id.tamilYearTxt, context.getResources().getStringArray(R.array.tamil_year_names)[md.getTyear() - 1]);
            views.setTextViewText(R.id.tamilMonthTxt, context.getResources().getStringArray(R.array.tamizh_month_names)[md.getTmonth() - 1]);
            views.setTextViewText(R.id.tamilDateTxt, "" + md.getTday());
        }

        NallaNeramData nd = DBHelper.getInstance(context).getNallaNeram(DateUtil.format(selectedDate));
        if (null != nd) {
            views.setTextViewText(R.id.nallaNeramMorning, nd.getNallaNeramM());
            views.setTextViewText(R.id.nallaNeramEvening, nd.getNallaNeramE());
        }

        FestivalDayData fd = DBHelper.getInstance(context).getFestivalDays(DateUtil.format(selectedDate));
        if (null != fd) {

            Set<String> festivals = new HashSet<>();
            if (fd.getHindhu() != null && !fd.getHindhu().equals("-")) {
                festivals.addAll(Arrays.asList(fd.getHindhu().split("[ ]*[,][ ]*")));
            }

            if (null != fd.getGovt() && !fd.getGovt().equals("-")) {
                festivals.addAll(Arrays.asList(fd.getGovt().split("[ ]*[,][ ]*")));
            }

            if (null != fd.getImportant() && !fd.getImportant().equals("-")) {
                festivals.addAll(Arrays.asList(fd.getImportant().split("[ ]*[,][ ]*")));
            }
            if (!festivals.isEmpty()) {
                views.setViewVisibility(R.id.importantDayTxt, View.VISIBLE);
                views.setTextViewText(R.id.importantDayTxt, StringUtils.join(festivals, ","));
            } else {
                views.setViewVisibility(R.id.importantDayTxt, View.GONE);
            }
        }
        StringBuilder sb = new StringBuilder();
        List<VirathamData> virathamDataList = DBHelper.getInstance(context).getVirathamList(DateUtil.format(selectedDate));
        String[] virathams = context.getResources().getStringArray(R.array.viratham_names);
        for (int index = 0; index < virathamDataList.size(); index++) {
            VirathamData vd = virathamDataList.get(index);
            sb.append(virathams[vd.getViratham()]);
            if (vd.getTiming().length() > 1) {
                sb.append("[").append(vd.getTiming()).append("]");
            }

            sb.append(", ");
        }

        if (sb.length() != 0) {
            if (sb.toString().endsWith(", ")) {
                sb.setLength(sb.toString().length() - 2);
            }
            views.setViewVisibility(R.id.virathamTxt, View.VISIBLE);
            views.setTextViewText(R.id.virathamTxt, sb.toString());
        } else {
            views.setViewVisibility(R.id.virathamTxt, View.GONE);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static String monthText(Context context, LocalDate selectedDate) {
        String[] monthNames = context.getResources().getStringArray(R.array.en_month_names);
        String[] weekDayNames = context.getResources().getStringArray(R.array.weekday_names);

        return monthNames[selectedDate.getMonthValue() - 1] + " - " + weekDayNames[selectedDate.getDayOfWeek().getValue() - 1];
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

}