package org.tem.calendar.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomCalendar extends LinearLayout {
    private final List<DateModel> dates = new ArrayList<>();
    private final int weekStartIndex = 7;
    private final List<Integer> weekEndList = Arrays.asList(6, 7);
    private final List<Integer> weekEnds = new ArrayList<>();
    private String[] weeks = new String[]{"MON", "TUE", "WED", "THU", "SAT", "SUN"};
    private Context mContext;
    private View root;
    private AppCompatImageView previousMonthButton;
    private AppCompatImageView nextMonthButton;
    private CalendarNavigationListener listener;
    private RecyclerView calendarView;
    private DateHolder dateHolder;
    private CalendarDayOnClickListener dayOnClickListener;

    public CustomCalendar(Context context) {
        super(context, null);
        init();
    }

    public CustomCalendar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        this.root = inflate(mContext, R.layout.custom_calendar_layout, this);
        previousMonthButton = root.findViewById(R.id.prevBtn);
        nextMonthButton = root.findViewById(R.id.nextBtn);
        calendarView = root.findViewById(R.id.calendarView);
        refresh();
    }

    public void setNavigationListener(CalendarNavigationListener listener) {
        this.listener = listener;
    }

    public void setWeeks(String[] weeks) {
        this.weeks = weeks;
    }

    public void setDates(List<DateModel> dateList) {
        this.dates.clear();
        this.dates.addAll(dateList);

        int l = dates.get(0).getDate().getDayOfWeek().getValue() - weekStartIndex;
        dateHolder = new DateHolder(dates.get(0).getDate());
        if (l < 0) {
            l += 7;
        }

        for (int i = 0; i < l; i++) {
            dates.add(i, null);
        }

        int x = (int) Math.ceil(dates.size() / 7.0f);
        int size = dates.size();
        for (int i = 0; i < (x * 7 - size); i++) {
            dates.add(size + i, null);
        }
    }

    public void refresh() {

        if (null == listener) {
            listener = new CalendarNavigationListener() {
                @Override
                public void previousClicked(DateHolder dateHolder) {
                    LocalDate localDate = dateHolder.getDate().minusMonths(1);
                    generateDates(localDate);
                    setPrimeMonthTxt(localDate.getMonth().toString());
                    setPrimeYearTxt(localDate.getYear() + "");
                    refresh();
                }

                @Override
                public void nextClicked(DateHolder dateHolder) {
                    LocalDate localDate = dateHolder.getDate().plusMonths(1);
                    generateDates(localDate);
                    setPrimeMonthTxt(localDate.getMonth().toString());
                    setPrimeYearTxt(localDate.getYear() + "");
                    refresh();
                }
            };
        }
        previousMonthButton.setOnClickListener(view -> listener.previousClicked(dateHolder));
        nextMonthButton.setOnClickListener(view -> listener.nextClicked(dateHolder));

        List<Pair<Integer, String>> weeks = DateUtil.getNormalizedWeeks(weekStartIndex, this.weeks);

        weekEnds.clear();
        weekEnds.addAll(DateUtil.getWeekEnds(weekStartIndex, weekEndList));

        for (int index = 0; index < weeks.size(); index++) {
            AppCompatTextView txt = root.findViewWithTag("weekHeader" + index);
            txt.setText(weeks.get(index).second);
        }

        if (dates.isEmpty()) {
            LocalDate localDate = LocalDate.now().withDayOfMonth(1);
            generateDates(localDate);
            setPrimeMonthTxt(localDate.getMonth().toString());
            setPrimeYearTxt(localDate.getYear() + "");
            setSecondaryMonthTxt("");
        }


        calendarView.setLayoutManager(new GridLayoutManager(mContext, 7));
        DayRecyclerAdapter adapter = new DayRecyclerAdapter(mContext, dates, weekEnds, dayOnClickListener, new SwipeInterface() {
            @Override
            public void bottom2top(View v) {

            }

            @Override
            public void left2right(View v) {
                listener.previousClicked(dateHolder);
            }

            @Override
            public void right2left(View v) {
                listener.nextClicked(dateHolder);
            }

            @Override
            public void top2bottom(View v) {

            }
        });
        calendarView.setAdapter(adapter);
    }

    private void generateDates(LocalDate localDate) {
        List<DateModel> dates = new ArrayList<>();
        for (int i = 0; i < localDate.lengthOfMonth(); i++) {
            DateModel dm = new DateModel();
            dm.setDate(localDate.plusDays(i));
            dm.setPrimeDay(dm.getDate().getDayOfMonth());
            dm.setSecondaryDay(dm.getDate().getMonthValue());
            dates.add(dm);
        }
        setDates(dates);
    }

    public void setPrimeMonthTxt(String monthTxt) {
        if (null != root) {
            AppCompatTextView txt = findViewById(R.id.primeMonthTxt);
            if (null != txt) {
                txt.setText(monthTxt);
            }
        }
    }

    public void setPrimeYearTxt(String yearTxt) {
        if (null != root) {
            AppCompatTextView txt = findViewById(R.id.primeYearTxt);
            if (null != txt) {
                txt.setText(yearTxt);
            }
        }
    }

    public void setSecondaryMonthTxt(String monthTxt) {
        if (null != root) {
            AppCompatTextView txt = findViewById(R.id.secondaryMonthText);
            if (null != txt) {
                if (StringUtils.isBlank(monthTxt)) {
                    txt.setVisibility(GONE);
                } else {
                    txt.setText(monthTxt);
                    txt.setVisibility(VISIBLE);
                }

            }
        }
    }

    public void setDayOnClickListener(CalendarDayOnClickListener calendarDayOnClickListener) {
        this.dayOnClickListener = calendarDayOnClickListener;
    }
}
