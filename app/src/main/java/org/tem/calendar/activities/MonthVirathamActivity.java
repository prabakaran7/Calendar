package org.tem.calendar.activities;

import android.annotation.SuppressLint;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.MonthVirathamViewPageAdapter;
import org.tem.calendar.databinding.ActivityMonthVirathamBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.MonthVirathamFragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MonthVirathamActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private final List<Integer> yearList = new ArrayList<>();
    private ActivityMonthVirathamBinding binding;
    private int type = MonthVirathamFragment.SUBA_VIRATHAM;
    private int selected = -1;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_month_viratham);
        binding.adView.loadAd(new AdRequest.Builder().build());
        if (getIntent() != null) {
            type = getIntent().getIntExtra(Constants.EXTRA_TYPE, MonthVirathamFragment.SUBA_VIRATHAM);
        }
        if (type == MonthVirathamFragment.SUBA_VIRATHAM) {
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
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_dropdown, menu);
        MenuItem item = menu.findItem(R.id.action_bar_spinner);
        Spinner spinner = (Spinner) item.getActionView();
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, R.layout.layout_drop_title, yearList);
        adapter.setDropDownViewResource(R.layout.layout_drop_list);
        spinner.setAdapter(adapter);
        spinner.setSelection(selected, false);

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (selected != position) {
            loadData(yearList.get(position));
            selected = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadData(int year) {
        binding.viewPager.setAdapter(new MonthVirathamViewPageAdapter(this, year, type));
        LocalDate ld = LocalDate.now();
        String[] monthNames = getResources().getStringArray(R.array.en_month_names);

        if (ld.getYear() == year) {
            binding.viewPager.post(() -> binding.viewPager.setCurrentItem(ld.getMonthValue() - 1, false));
        }
        new TabLayoutMediator(
                binding.tabLayout,
                binding.viewPager,
                (tab, position) -> tab.setText(monthNames[position])
        ).attach();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}