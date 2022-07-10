package org.tem.calendar.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.FestivalViewPageAdapter;
import org.tem.calendar.databinding.ActivityFestivalBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.FestivalFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FestivalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final List<Integer> yearList = new ArrayList<>();
    private ActivityFestivalBinding binding;
    private int type;
    private int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_festival);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        yearList.addAll(DBHelper.getInstance(this).getFestivalYears());
        type = getIntent().getIntExtra(Constants.EXTRA_TYPE, FestivalFragment.HOLIDAY);

        binding.toolbar.setSubtitle(getSubtitle(type));
    }

    public static int getSubtitle(int type) {
        int subtitle;
        if (type == FestivalFragment.HOLIDAY) {
            subtitle = R.string.govtHolidays;
        } else if (type == FestivalFragment.HINDU_FESTIVALS) {
            subtitle = R.string.hindu_festivals;
        } else if (type == FestivalFragment.CHRIST_FESTIVALS) {
            subtitle = R.string.christian_festivals;
        } else {
            subtitle = R.string.muslim_festivals;
        }
        return subtitle;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dropdown, menu);
        MenuItem item = menu.findItem(R.id.action_bar_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.layout_drop_title, yearList);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(selected, false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(selected != position) {
            loadData(yearList.get(position));
            selected = position;
        }
    }

    private void loadData(int year) {
        binding.viewPager.setAdapter(new FestivalViewPageAdapter(this, year, type));
        LocalDate ld = LocalDate.now();
        if (ld.getYear() == year) {
            binding.viewPager.post(() -> binding.viewPager.setCurrentItem(ld.getMonthValue() - 1, false));
        }
        String[] monthNames = getResources().getStringArray(R.array.en_month_names);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(monthNames[position])).attach();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}