package org.tem.calendar.activities;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.R;
import org.tem.calendar.adapter.RaghuViewPageAdapter;
import org.tem.calendar.databinding.ActivityRaghuEmaKuligaiBinding;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class RaghuEmaKuligaiActivity extends AppCompatActivity {

    ActivityRaghuEmaKuligaiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_raghu_ema_kuligai);

        binding.viewPager.setAdapter(new RaghuViewPageAdapter(getSupportFragmentManager(), getLifecycle()));

        List<Pair<Integer, String>> weeks = CalendarApp.getWeekDayNameList();
        setCurrentDayIndex(weeks);
        binding.toolbar.setSubtitle(R.string.raghu_ema_kuligai_title);
        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(weeks.get(position).second)).attach();


    }

    private void setCurrentDayIndex(List<Pair<Integer, String>> weekNames) {
        int weekDay = LocalDate.now().getDayOfWeek().getValue();
        for (int index = 0; index < weekNames.size(); index++) {
            if(weekNames.get(index).first == weekDay){
                binding.viewPager.setCurrentItem(index, false);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

}