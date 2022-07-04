package org.tem.calendar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.MonthVirathamViewPageAdapter;
import org.tem.calendar.adapter.MuhurthamViewPageAdapter;
import org.tem.calendar.databinding.ActivityMonthVirathamBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.MonthVirathamFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MonthVirathamActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final List<Integer> yearList = new ArrayList<>();
    private ActivityMonthVirathamBinding binding;
    private int type = MonthVirathamFragment.SUBA_VIRATHAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month_viratham);

        if(getIntent() != null){
            type = getIntent().getIntExtra(Constants.EXTRA_TYPE, MonthVirathamFragment.SUBA_VIRATHAM);
        }
        if(type == MonthVirathamFragment.SUBA_VIRATHAM) {
            binding.toolbar.setSubtitle(R.string.viratha_thinangal);
        } else {
            binding.toolbar.setSubtitle(R.string.asuba_days);
        }
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        yearList.addAll(DBHelper.getInstance(this).getVirathamYearList());
        Collections.sort(yearList);
        Collections.reverse(yearList);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dropdown, menu);
        MenuItem item = menu.findItem(R.id.action_bar_spinner);
        AppCompatSpinner spinner = (AppCompatSpinner) item.getActionView();
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.layout_drop_title, yearList);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        loadData(yearList.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadData(int year){
        binding.viewPager.setAdapter(new MonthVirathamViewPageAdapter(this, year, type));
        LocalDate ld = LocalDate.now();
        if (ld.getYear() == year) {
            binding.viewPager.post(()-> binding.viewPager.setCurrentItem(ld.getMonthValue() - 1));
        }
        String[] monthNames = getResources().getStringArray(R.array.en_month_names);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(monthNames[position])).attach();

    }
}