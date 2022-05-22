package org.tem.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.custom.ActivitySwipeDetector;
import org.tem.calendar.custom.CalendarNavigationListener;
import org.tem.calendar.custom.DateHolder;
import org.tem.calendar.custom.DateModel;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.SwipeInterface;
import org.tem.calendar.databinding.ActivityMonthBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.MuhurthamFragment;
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
import java.util.stream.Collectors;

public class MonthActivity extends AppCompatActivity implements SwipeInterface {

    ActivityMonthBinding binding;

    List<Integer> tamilMonth = new ArrayList<>();

    DateHolder dateHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month);

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
        binding.monthlyCalendar.setDayOnClickListener(clickedDate -> {
            finish();
            overridePendingTransition(0, 0);
            Intent intent = new Intent(MonthActivity.this, DailyActivity.class);
            intent.putExtra(Constants.EXTRA_DATE_SELECTED, clickedDate);
            startActivity(intent);
            overridePendingTransition(0, 0);
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
        binding.muhurthamLayout.setOnTouchListener(new ActivitySwipeDetector(this, this));

        loadMuhurtham();
    }

    private void loadMuhurtham() {
       FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
       ft.replace(R.id.muhurtham_layout, new MuhurthamFragment(LocalDate.of(dateHolder.getYear(), dateHolder.getMonth(), 1)));
       ft.commit();
    }


    private void loadCalendar(LocalDate date) {
        this.dateHolder = new DateHolder(date);
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
            List<VirathamMonthData> vdList = DBHelper.getInstance(this).getVirathamList(dateHolder.getYear(), dateHolder.getMonth());
            Map<LocalDate, List<VirathamMonthData>> vdMap = new HashMap<>();
            for(VirathamMonthData vmd:vdList){
                LocalDate ld = DateUtil.ofLocalDate(vmd.getDate());
                if(!vdMap.containsKey(ld)){
                    vdMap.put(ld, new ArrayList<>());
                }

                Objects.requireNonNull(vdMap.get(ld)).add(vmd);
            }

            // TODO Holiday/Special Day List

            // Muhurtham List
            final Map<LocalDate, MuhurthamData> muhurthamDataMap = DBHelper.getInstance(this)
                    .getMuhurthamList(dateHolder.getYear(), dateHolder.getMonth())
                    .stream().collect(Collectors.toMap(MuhurthamData::getDate, e -> e));
            for (MonthData md : dataList) {
                DateModel dateModel = new DateModel();
                dateModel.setDate(LocalDate.of(md.getYear(), md.getMonth(), md.getDay()));
                dateModel.setPrimeDay(md.getDay());
                dateModel.setSecondaryDay(md.getTday());
                if (!tamilMonth.contains(md.getTmonth())) {
                    tamilMonth.add(md.getTmonth());
                }

                if(vdMap.containsKey(dateModel.getDate())){
                    List<VirathamMonthData> vmdList = vdMap.get(dateModel.getDate());
                    System.out.println("Size:::" + vmdList.size());
                    assert vmdList != null;
                    for(VirathamData vd: vmdList){
                        switch (vd.getViratham()){
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