package org.tem.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.MonthMuhurthamRecyclerAdapter;
import org.tem.calendar.custom.ActivitySwipeDetector;
import org.tem.calendar.custom.CalendarNavigationListener;
import org.tem.calendar.custom.DateModel;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.SwipeInterface;
import org.tem.calendar.databinding.ActivityMonthBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.MuhurthamData;
import org.tem.calendar.model.VirathamData;
import org.tem.calendar.model.VirathamMonthData;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MonthActivity extends AppCompatActivity implements SwipeInterface {

    final List<Integer> tamilMonth = new ArrayList<>();
    ActivityMonthBinding binding;
    LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month);

        if (null != getIntent() && null != getIntent().getExtras() && getIntent().getExtras().containsKey(Constants.EXTRA_DATE_SELECTED)) {
            selectedDate = (LocalDate) getIntent().getSerializableExtra(Constants.EXTRA_DATE_SELECTED);
        } else {
            selectedDate = LocalDate.now().withDayOfMonth(1);
        }

        CalendarNavigationListener listener = new CalendarNavigationListener() {
            @Override
            public void previousClicked() {
                previousMonth();
            }

            @Override
            public void nextClicked() {
                nextMonth();
            }
        };
        binding.monthlyCalendar.setNavigationListener(listener);
        binding.monthlyCalendar.setDayOnClickListener(clickedDate -> {
            finish();
            overridePendingTransition(0, 0);
            Intent intent = new Intent(MonthActivity.this, DailyActivity.class);
            intent.putExtra(Constants.EXTRA_DATE_SELECTED, clickedDate);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        loadCalendar(selectedDate);

        binding.rootView.setOnTouchListener(new ActivitySwipeDetector(this, this));
        //binding.muhurthamLayout.setOnTouchListener(new ActivitySwipeDetector(this, this));


    }

    private void nextMonth() {
        finish();
        Intent intent = getIntent();
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, selectedDate.plusMonths(1));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);

    }

    private void previousMonth() {
        finish();
        Intent intent = getIntent();
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, selectedDate.minusMonths(1));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void loadMuhurtham(Map<LocalDate, MuhurthamData> muhurthamDataMap) {
        if (muhurthamDataMap.isEmpty()) {
            binding.muhurthamLayout.muhurthamLinearLayout.setVisibility(View.GONE);
            binding.muhurthamLayout.muhurthamEmptyTxt.setVisibility(View.VISIBLE);
            binding.muhurthamLayout.muhurthamEmptyTxt.setText(getString(R.string.empty_muhurtham_msg, getResources().getStringArray(R.array.en_month_names)[selectedDate.getMonthValue() - 1]));
        } else {
            binding.muhurthamLayout.muhurthamLinearLayout.setVisibility(View.VISIBLE);
            binding.muhurthamLayout.muhurthamEmptyTxt.setVisibility(View.GONE);
            Map<LocalDate, MuhurthamData> mdList = new TreeMap<>(muhurthamDataMap);
            binding.muhurthamLayout.muhurthamRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            MonthMuhurthamRecyclerAdapter adapter = new MonthMuhurthamRecyclerAdapter(this, new ArrayList<>(mdList.values()));
            binding.muhurthamLayout.muhurthamRecyclerView.setAdapter(adapter);
            binding.muhurthamLayout.muhurthamRecyclerView.suppressLayout(true);
        }
    }


    private void loadCalendar(LocalDate date) {
        binding.monthlyCalendar.setDates(generateDates(date));
        binding.monthlyCalendar.setPrimeYearTxt(date.getYear() + "");
        binding.monthlyCalendar.setPrimeMonthTxt(date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH));
        String[] monthNames = getResources().getStringArray(R.array.tamizh_month_names);
        if (!tamilMonth.isEmpty()) {
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
        if (dataList.isEmpty()) {
            for (int i = 0; i < localDate.lengthOfMonth(); i++) {
                LocalDate date = localDate.plusDays(i);
                DateModel model = new DateModel();
                model.setDate(date);
                model.setPrimeDay(date.getDayOfMonth());
                dates.add(model);
            }
        } else {
            //TODO get Viratham List
            List<VirathamMonthData> vdList = DBHelper.getInstance(this).getVirathamList(selectedDate.getYear(), selectedDate.getMonthValue());
            Map<LocalDate, List<VirathamMonthData>> vdMap = new HashMap<>();
            for (VirathamMonthData vmd : vdList) {
                LocalDate ld = DateUtil.ofLocalDate(vmd.getDate());
                if (!vdMap.containsKey(ld)) {
                    vdMap.put(ld, new ArrayList<>());
                }

                Objects.requireNonNull(vdMap.get(ld)).add(vmd);
            }

            // TODO Holiday/Festival Day List
            Map<LocalDate, String> hindhuFestivalMap = DBHelper.getInstance(this).getHinduFestivalDays(selectedDate.getYear(), selectedDate.getMonthValue());

            List<String> festivals = new ArrayList<>();
            String[] weekShortNames = getResources().getStringArray(R.array.weekday_short_names);
            for (Map.Entry<LocalDate, String> entry : hindhuFestivalMap.entrySet()) {
                festivals.add(
                        entry.getKey().getDayOfMonth() + " " + weekShortNames[entry.getKey().getDayOfWeek().getValue() - 1]
                                + " - " + entry.getValue()
                );
            }
            if(festivals.isEmpty()){
                festivals.add("          -          ");
            }
            ArrayAdapter<String> festivalAdapter = new ArrayAdapter<>(this, R.layout.simple_textview_item, R.id.text, festivals);
            binding.hinduFestivalLayout.listView.setAdapter(festivalAdapter);
            binding.hinduFestivalLayout.headerTitle.setText(getString(R.string.hindu_festivals));

            Map<LocalDate, String> holidayMap = DBHelper.getInstance(this).getHolidays(selectedDate.getYear(), selectedDate.getMonthValue());
            List<String> holidays = new ArrayList<>();
            for (Map.Entry<LocalDate, String> entry : holidayMap.entrySet()) {
                holidays.add(
                        entry.getKey().getDayOfMonth() + " " + weekShortNames[entry.getKey().getDayOfWeek().getValue() - 1]
                                + " - " + entry.getValue()
                );
            }
            if(holidays.isEmpty()){
                holidays.add("          -          ");
            }
            ArrayAdapter<String> holidayAdapter = new ArrayAdapter<>(this, R.layout.simple_textview_item, R.id.text, festivals);
            binding.govtHolidayLayout.listView.setAdapter(holidayAdapter);
            binding.govtHolidayLayout.headerTitle.setText(getString(R.string.govtHolidays));

            // Muhurtham List
            final Map<LocalDate, MuhurthamData> muhurthamDataMap = DBHelper.getInstance(this)
                    .getMuhurthamList(selectedDate.getYear(), selectedDate.getMonthValue())
                    .stream().collect(Collectors.toMap(MuhurthamData::getDate, e -> e));
            for (MonthData md : dataList) {
                DateModel dateModel = new DateModel();
                dateModel.setDate(LocalDate.of(md.getYear(), md.getMonth(), md.getDay()));
                dateModel.setPrimeDay(md.getDay());
                dateModel.setSecondaryDay(md.getTday());
                if (!tamilMonth.contains(md.getTmonth())) {
                    tamilMonth.add(md.getTmonth());
                }

                if (vdMap.containsKey(dateModel.getDate())) {
                    List<VirathamMonthData> vmdList = vdMap.get(dateModel.getDate());
                    assert vmdList != null;
                    for (VirathamData vd : vmdList) {
                        switch (vd.getViratham()) {
                            case 0:
                                dateModel.setTithi(R.drawable.new_moon);
                                break;
                            case 1:
                                dateModel.setTithi(R.drawable.full_moon);
                                break;
                            case 2:
                                dateModel.setStar(R.drawable.star);
                                break;
                            case 3:
                            case 7:
                                dateModel.setTithi(R.drawable.sivarathri);
                                break;
                            case 4:
                                dateModel.setTithi(R.drawable.sathurthasi);
                                break;
                            case 5:
                                dateModel.setStar(R.drawable.thiruvonam);
                                break;
                            case 6:
                                dateModel.setTithi(R.drawable.shasti);
                                break;
                            case 8:
                                dateModel.setTithi(R.drawable.astami);
                                break;
                            case 9:
                                dateModel.setTithi(R.drawable.navami);
                                break;
                            case 12:
                                dateModel.setTithi(R.drawable.ekadeshi);
                                break;
                            case 13:
                                dateModel.setTithi(R.drawable.pradhosam);
                                break;
                        }
                    }
                }

                if (muhurthamDataMap.containsKey(dateModel.getDate())) {
                    dateModel.setMuhurtham(R.drawable.wedding);
                }
                dates.add(dateModel);
            }
            loadMuhurtham(muhurthamDataMap);
        }
        return dates;
    }

    @Override
    public void bottom2top(View v) {

    }

    @Override
    public void left2right(View v) {
        previousMonth();
    }

    @Override
    public void right2left(View v) {
        nextMonth();
    }

    @Override
    public void top2bottom(View v) {

    }
}