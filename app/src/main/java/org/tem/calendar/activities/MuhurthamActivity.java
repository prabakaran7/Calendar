package org.tem.calendar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.R;
import org.tem.calendar.adapter.MuhurthamViewPageAdapter;
import org.tem.calendar.databinding.ActivityMuhurthamBinding;

import java.time.LocalDate;
import java.util.Arrays;

public class MuhurthamActivity extends AppCompatActivity {

    ActivityMuhurthamBinding binding;

    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_muhurtham);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("2020", "2021", "2022"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yearSpinner.setAdapter(adapter);
        binding.yearSpinner.setSelection(adapter.getCount() - 1);
        year = LocalDate.now().getYear();

        binding.yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = Integer.parseInt(binding.yearSpinner.getSelectedItem().toString());
                loadData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadData();
    }

    private void loadData() {
        binding.viewPager.setAdapter(new MuhurthamViewPageAdapter(this, year));
        if (LocalDate.now().getYear() == year) {
            binding.viewPager.postDelayed(() -> binding.viewPager.setCurrentItem(LocalDate.now().getMonthValue() - 1, false), 100);
        }
        String[] monthNames = getResources().getStringArray(R.array.en_month_names);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(monthNames[position])).attach();
    }
}