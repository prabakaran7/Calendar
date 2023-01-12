package org.tem.calendar.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import org.tem.calendar.R;
import org.tem.calendar.custom.CalendarDayOnClickListener;
import org.tem.calendar.custom.DateModel;
import org.tem.calendar.databinding.MonthDayCellBinding;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

public class MonthCalendarView extends LinearLayoutCompat {

    private static final int startOfWeek = DayOfWeek.SUNDAY.getValue();
    private final String[] weekNames = getResources().getStringArray(R.array.weekday_names);

    private CalendarDayOnClickListener listener;

    public MonthCalendarView(@NonNull Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public MonthCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public MonthCalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setListener(CalendarDayOnClickListener listener) {
        this.listener = listener;
    }

    private void setHeaders() {
        LinearLayoutCompat header = new LinearLayoutCompat(getContext());
        header.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.setMargins(2, 2, 2,2);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        addView(header, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int index = 0; index < 7; index++) {
            int wd = startOfWeek + index;
            if (wd > 7) {
                wd -= 7;
            }
            TextView tv = new TextView(getContext());

            tv.setText(weekNames[wd - 1]);
            tv.setLayoutParams(params);
            TypedValue typedValue = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.bgSubTitle, typedValue, true);
            tv.setBackgroundColor(ContextCompat.getColor(getContext(), typedValue.resourceId));

            getContext().getTheme().resolveAttribute(R.attr.tcSubTitle, typedValue, true);

            tv.setTextColor(ContextCompat.getColor(getContext(), typedValue.resourceId));
            tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
            tv.setPadding(5, 15, 5, 15);
            tv.setMaxLines(1);
            tv.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            header.addView(tv, params);
        }
    }

    public void refreshData(List<DateModel> monthDayDataList) {
        if (null == monthDayDataList || monthDayDataList.isEmpty()) {
            return;
        }
        removeAllViews();
        setHeaders();
        List<DateModel> normalized = new ArrayList<>();
        int firstDayOfMonth = monthDayDataList.get(0).getDate().getDayOfWeek().getValue();
        for (int index = 0; index < 7; index++) {
            int wd = index + startOfWeek;
            if(wd > 7) {
                wd-=7;
            }
            if (firstDayOfMonth == wd) {
                break;
            } else {
                normalized.add(null);
            }

        }

        normalized.addAll(monthDayDataList);

        int lastDayOfMonth = monthDayDataList.get(monthDayDataList.size() - 1).getDate().getDayOfWeek().getValue();
        int endOfWeek = startOfWeek + 6;
        if (endOfWeek > 7) {
            endOfWeek -= 7;
        }

        for (int index = 0; index < 6; index++) {
            if (lastDayOfMonth == endOfWeek) {
                break;
            } else {
                normalized.add(null);
                lastDayOfMonth++;
            }
        }

        List<List<DateModel>> weekList = new ArrayList<>();
        int startIndex = 0;
        int endIndex = 7;
        do {
            weekList.add(normalized.subList(startIndex, endIndex));
            startIndex = endIndex;
            endIndex += 7;
        } while (endIndex <= normalized.size());

        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 2, 2, 2);
        params.weight = 1.0f;

        for (List<DateModel> weekData : weekList) {
            LinearLayoutCompat weekLayout = new LinearLayoutCompat(getContext());
            weekLayout.setOrientation(HORIZONTAL);
            weekLayout.setWeightSum(7);
            addView(weekLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for (DateModel dm : weekData) {
                MonthDayCellBinding mdc = MonthDayCellBinding.inflate(
                        LayoutInflater.from(getContext()));

                CardView cv = new CardView(getContext());
                cv.setPadding(5, 5, 5, 5);
                cv.addView(mdc.getRoot());
                cv.setCardElevation(5);
                cv.setRadius(5);
                cv.setLayoutParams(params);
                if (null != dm) {
                    LocalDate ld = dm.getDate();
                    mdc.mainDayTxt.setText(String.format(Locale.getDefault(), "%d", dm.getDate().getDayOfMonth()));
                    if(ld.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                        mdc.mainDayTxt.setTextColor(Color.RED);
                    }

                    mdc.secDayTxt.setText(String.format(Locale.getDefault(), "%d", dm.getSecondaryDay()));

                    if(dm.getTithi() > -1) {
                        mdc.image1.setImageResource(dm.getTithi());
                    }

                    if(dm.getStar() > -1){
                        mdc.image3.setImageResource(dm.getStar());
                    }

                    if(dm.getMuhurtham() > -1){
                        mdc.image2.setImageResource(dm.getMuhurtham());
                    }

                    if(dm.isHoliday()) {
                        TypedValue typedValue = new TypedValue();
                        getContext().getTheme().resolveAttribute(R.attr.bgHoliday, typedValue, true);
                        mdc.getRoot().setBackgroundResource(typedValue.resourceId);

                        getContext().getTheme().resolveAttribute(R.attr.tcHoliday, typedValue, true);
                        mdc.mainDayTxt.setTextColor(ContextCompat.getColor(getContext(), typedValue.resourceId));
                        mdc.secDayTxt.setTextColor(ContextCompat.getColor(getContext(), typedValue.resourceId));
                    }

                    if(dm.isToday()) {
                        TypedValue typedValue = new TypedValue();
                        getContext().getTheme().resolveAttribute(R.attr.bgToday, typedValue, true);
                        mdc.getRoot().setBackgroundResource(typedValue.resourceId);

                        getContext().getTheme().resolveAttribute(R.attr.tcToday, typedValue, true);
                        mdc.mainDayTxt.setTextColor(ContextCompat.getColor(getContext(), typedValue.resourceId));
                        mdc.secDayTxt.setTextColor(ContextCompat.getColor(getContext(), typedValue.resourceId));
                    }

                    if(null != listener) {
                        cv.setOnClickListener(view -> listener.onClick(ld));
                    }

                } else {
                    mdc.getRoot().setVisibility(INVISIBLE);
                }

                weekLayout.addView(cv, params);
            }


        }
    }
}
