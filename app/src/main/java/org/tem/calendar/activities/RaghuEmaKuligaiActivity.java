package org.tem.calendar.activities;

import android.os.Bundle;
import android.util.Pair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.R;
import org.tem.calendar.adapter.RaghuViewPageAdapter;
import org.tem.calendar.databinding.ActivityRaghuEmaKuligaiBinding;

import java.time.LocalDate;
import java.util.List;

public class RaghuEmaKuligaiActivity extends AppCompatActivity {

    ActivityRaghuEmaKuligaiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_raghu_ema_kuligai);

        binding.viewPager.setAdapter(new RaghuViewPageAdapter(getSupportFragmentManager(), getLifecycle()));

        List<Pair<Integer, String>> weeks = CalendarApp.getWeekDayNameList();
        setCurrentDayIndex(weeks);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle(getString(R.string.raghu_ema_kuligai_title));
        }

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(weeks.get(position).second)).attach();


    }

    private void setCurrentDayIndex(List<Pair<Integer, String>> weekNames) {
        int weekDay = LocalDate.now().getDayOfWeek().getValue();
        for (int index = 0; index < weekNames.size(); index++) {
            if(weekNames.get(index).first == weekDay){
                binding.viewPager.setCurrentItem(index);
                break;
            }
        }
    }

}