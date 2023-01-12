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
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.R;
import org.tem.calendar.adapter.MuhurthamViewPageAdapter;
import org.tem.calendar.databinding.ActivityMuhurthamBinding;
import org.tem.calendar.db.DBHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MuhurthamActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private final List<Integer> yearList = new ArrayList<>();
    private ActivityMuhurthamBinding binding;
    private int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_muhurtham);

        binding.toolbar.setSubtitle(R.string.muhurtham_days);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        yearList.addAll(DBHelper.getInstance(this).getMuhurthamYearList());
        Collections.sort(yearList);
        Collections.reverse(yearList);
    }

    private void loadData(int year) {
        binding.viewPager.setAdapter(new MuhurthamViewPageAdapter(this, year));
        LocalDate ld = LocalDate.now();
        if (ld.getYear() == year) {
            binding.viewPager.setCurrentItem(ld.getMonthValue() - 1, false);
        }
        String[] monthNames = getResources().getStringArray(R.array.en_month_names);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(monthNames[position])).attach();


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
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}