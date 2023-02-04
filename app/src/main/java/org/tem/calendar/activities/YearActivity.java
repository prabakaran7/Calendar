package org.tem.calendar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.YearMonthRecyclerAdapter;
import org.tem.calendar.custom.CalendarDayOnClickListener;
import org.tem.calendar.custom.DateModel;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.databinding.ActivityYearBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.MuhurthamData;
import org.tem.calendar.model.VirathamMonthData;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class YearActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, CalendarDayOnClickListener {

    private ActivityYearBinding binding;
    private final List<Integer> yearList = new ArrayList<>();

    private final List<List<DateModel>> yearDataList = new ArrayList<>();

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Thread(() -> {
            yearList.addAll(DBHelper.getInstance(this).getMasterYearList());
            Collections.sort(yearList);
            Collections.reverse(yearList);
        }).start();
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_year);

        binding.adView.loadAd(new AdRequest.Builder().build());

        binding.toolbar.setTitle(R.string.year_calendar);
        setSupportActionBar(binding.toolbar);


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new YearMonthRecyclerAdapter(this, yearDataList));

    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadMonthView(Integer year) {
        yearDataList.clear();

        for (Month month : Month.values()) {
            yearDataList.add(generateMonthData(year, month.getValue()));
        }

        Objects.requireNonNull(binding.recyclerView.getAdapter()).notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dropdown, menu);
        MenuItem item = menu.findItem(R.id.action_bar_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.layout_drop_title, yearList);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        binding.progressBar.setVisibility(View.VISIBLE);
        loadMonthView(yearList.get(position));
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private List<DateModel> generateMonthData(int year, int month) {
        List<DateModel> dates = new ArrayList<>();
        List<MonthData> dataList = DBHelper.getInstance(this).getDates(year, month);
        LocalDate selectedDate = LocalDate.of(year, month, 1);
        if (dataList.isEmpty()) {
            for (int i = 0; i < selectedDate.lengthOfMonth(); i++) {
                LocalDate date = selectedDate.plusDays(i);
                DateModel model = new DateModel();
                model.setDate(date);
                model.setPrimeDay(date.getDayOfMonth());
                dates.add(model);
            }
        } else {
            dataList.sort(Comparator.comparing(o -> LocalDate.of(o.getYear(), o.getMonth(), o.getDay())));
            // Muhurtham List
            final Map<LocalDate, MuhurthamData> muhurthamDataMap = DBHelper.getInstance(this)
                    .getMuhurthamList(selectedDate.getYear(), selectedDate.getMonthValue())
                    .stream().collect(Collectors.toMap(MuhurthamData::getDate, e -> e));

            Map<LocalDate, String> holidayMap = DBHelper.getInstance(this).getHolidays(selectedDate.getYear(), selectedDate.getMonthValue());

            //get Viratham List
            List<VirathamMonthData> vdList = DBHelper.getInstance(this).getVirathamList(selectedDate.getYear(), selectedDate.getMonthValue());
            Map<LocalDate, List<VirathamMonthData>> vdMap = new TreeMap<>();
            for (VirathamMonthData vmd : vdList) {
                LocalDate ld = DateUtil.ofLocalDate(vmd.getDate());
                if (!vdMap.containsKey(ld)) {
                    vdMap.put(ld, new ArrayList<>());
                }

                Objects.requireNonNull(vdMap.get(ld)).add(vmd);
            }
            for (MonthData md : dataList) {
                DateModel dateModel = new DateModel();
                dateModel.setDate(LocalDate.of(md.getYear(), md.getMonth(), md.getDay()));
                dateModel.setPrimeDay(md.getDay());
                dateModel.setSecondaryDay(md.getTday());

                if (holidayMap.containsKey(dateModel.getDate())) {
                    dateModel.setHoliday(true);
                }

                if (vdMap.containsKey(dateModel.getDate())) {
                    List<VirathamMonthData> vmdList = vdMap.get(dateModel.getDate());
                    assert vmdList != null;
                    for (VirathamMonthData vd : vmdList) {
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
                                dateModel.setTithi(R.drawable.chathurthi);
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
    public void onClick(LocalDate clickedDate) {
        Intent intent = new Intent(YearActivity.this, DayActivity.class);
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, clickedDate);
        startActivity(intent);
    }

    public void monthClick(LocalDate clickedDate) {
        Intent intent = new Intent(YearActivity.this, MonthActivity.class);
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, clickedDate);
        startActivity(intent);
    }
}