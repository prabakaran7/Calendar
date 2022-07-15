package org.tem.calendar.activities;

import static org.tem.calendar.fragment.PanchangamFragment.GOWRI_PANCHANGAM;
import static org.tem.calendar.fragment.PanchangamFragment.GRAHA_ORAI_PANCHANGAM;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.PanchangamViewPageAdapter;
import org.tem.calendar.databinding.ActivityPanchangamBinding;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PanchangamActivity extends AppCompatActivity {

    private ActivityPanchangamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String type = GRAHA_ORAI_PANCHANGAM;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_panchangam);


        if (getIntent() != null && null != getIntent().getExtras() && getIntent().getExtras().containsKey(Constants.EXTRA_PANCHANGAM)) {
            type = getIntent().getStringExtra(Constants.EXTRA_PANCHANGAM);
        }

        binding.viewPager.setAdapter(new PanchangamViewPageAdapter(getSupportFragmentManager(), getLifecycle(), type));
        List<Pair<Integer, String>> weeks = CalendarApp.getWeekDayNameList();
        setCurrentDayIndex(weeks);
        if (GOWRI_PANCHANGAM.equals(type)) {
            binding.toolbar.setSubtitle(R.string.gowriPanchangamLabel);
        } else if (GRAHA_ORAI_PANCHANGAM.equals(type)) {
            binding.toolbar.setSubtitle(R.string.graha_orai_label);
        }
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> tab.setText(weeks.get(position).second)).attach();
    }

    private void setCurrentDayIndex(List<Pair<Integer, String>> weekNames) {
        int weekDay = LocalDate.now().getDayOfWeek().getValue();
        for (int index = 0; index < weekNames.size(); index++) {
            if (weekNames.get(index).first == weekDay) {
                binding.viewPager.setCurrentItem(index, false);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }
}