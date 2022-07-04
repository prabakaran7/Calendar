package org.tem.calendar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tem.calendar.activities.FestivalActivity;
import org.tem.calendar.fragment.FestivalFragment;

import java.time.LocalDate;

public class FestivalViewPageAdapter extends FragmentStateAdapter {
    private final int year;
    private final int type;

    public FestivalViewPageAdapter(FestivalActivity festivalActivity, int year, int type) {
        super(festivalActivity.getSupportFragmentManager(), festivalActivity.getLifecycle());
        this.type = type;
        this.year = year;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FestivalFragment.newInstance(LocalDate.of(year, position + 1, 1), type);
    }

    @Override
    public int getItemCount() {
        return LocalDate.MAX.getMonthValue();
    }
}
