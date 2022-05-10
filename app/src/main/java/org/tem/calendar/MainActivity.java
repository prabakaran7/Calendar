package org.tem.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.tem.calendar.activities.DailyActivity;
import org.tem.calendar.databinding.ActivityMainBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.library.ActivitySwipeDetector;
import org.tem.calendar.library.CalendarDayOnClickListener;
import org.tem.calendar.library.CalendarNavigationListener;
import org.tem.calendar.library.DateHolder;
import org.tem.calendar.library.DateModel;
import org.tem.calendar.library.SwipeInterface;
import org.tem.calendar.model.MonthData;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SwipeInterface {

    ActivityMainBinding binding;

    List<Integer> tamilMonth = new ArrayList<>();

    DateHolder dateHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        CalendarNavigationListener listener = new CalendarNavigationListener() {
            @Override
            public void previousClicked(DateHolder dateHolder) {
                loadCalendar(dateHolder.getDate().minusMonths(1));
            }

            @Override
            public void nextClicked(DateHolder dateHolder) {
                loadCalendar(dateHolder.getDate().plusMonths(1));
            }
        };
        binding.monthlyCalendar.setNavigationListener(listener);
        binding.monthlyCalendar.setDayOnClickListener(new CalendarDayOnClickListener() {

            @Override
            public void onClick(LocalDate clickedDate) {
                finish();
                overridePendingTransition(0, 0);
                Intent intent = new Intent(MainActivity.this, DailyActivity.class);
                intent.putExtra(Constants.EXTRA_DATE_SELECTED, clickedDate);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        LocalDate date = LocalDate.now();
        loadCalendar(date);

//        findViewById(R.id.rootView).setOnTouchListener(new OnSwipeTouchListener(this) {
//            @Override
//            public void onSwipeRight() {
//                listener.nextClicked(dateHolder);
//            }
//
//            @Override
//            public void onSwipeLeft() {
//                listener.previousClicked(dateHolder);
//            }
//        });

//        binding.rootView.setOnTouchListener(new ActivitySwipeDetector(this));
        binding.rootView.setOnTouchListener(new ActivitySwipeDetector(this, this));
    }



    private void loadCalendar(LocalDate date) {
        this.dateHolder = new DateHolder(date);
        binding.monthlyCalendar.setDates(generateDates(date));
        binding.monthlyCalendar.setPrimeYearTxt(date.getYear() +"") ;
        binding.monthlyCalendar.setPrimeMonthTxt(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        String[] monthNames = getResources().getStringArray(R.array.tamizh_month_names);
        if(!tamilMonth.isEmpty()){
            String sb = monthNames[tamilMonth.get(0) - 1] +
                    "-" +
                    monthNames[tamilMonth.get(1) - 1];
            binding.monthlyCalendar.setSecondaryMonthTxt(sb);
        }
        binding.monthlyCalendar.refresh();
    }

    private List<DateModel> generateDates(LocalDate localDate) {
        List<DateModel> dates = new ArrayList<>();

        List<MonthData> dataList = DBHelper.getInstance(this).getDates(localDate.getYear(), localDate.getMonthValue());
        tamilMonth.clear();
        if(dataList.isEmpty()){
            for( int i = 0; i<localDate.lengthOfMonth() ; i++){
                LocalDate date = localDate.plusDays(i);
                DateModel model = new DateModel();
                model.setDate(date);
                model.setPrimeDay(date.getDayOfMonth());
                dates.add(model);
            }
        } else {
            for (MonthData md : dataList) {
                DateModel dateModel = new DateModel();
                dateModel.setDate(LocalDate.of(md.getYear(), md.getMonth(), md.getDay()));
                dateModel.setPrimeDay(md.getDay());
                dateModel.setSecondaryDay(md.getTday());
                if (!tamilMonth.contains(md.getTmonth())) {
                    tamilMonth.add(md.getTmonth());
                }
                dates.add(dateModel);
            }
        }
        return dates;
    }

    @Override
    public void bottom2top(View v) {

    }

    @Override
    public void left2right(View v) {
        loadCalendar(dateHolder.getDate().minusMonths(1));
    }

    @Override
    public void right2left(View v) {
        loadCalendar(dateHolder.getDate().plusMonths(1));
    }

    @Override
    public void top2bottom(View v) {

    }
}