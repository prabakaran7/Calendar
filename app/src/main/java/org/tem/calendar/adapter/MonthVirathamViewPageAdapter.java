package org.tem.calendar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tem.calendar.fragment.MonthVirathamFragment;

import java.time.LocalDate;

public class MonthVirathamViewPageAdapter extends FragmentStateAdapter {

    private final int type;
    private final int year;

    public MonthVirathamViewPageAdapter(FragmentActivity fragmentActivity, int year) {
        super(fragmentActivity);
        this.year = year;
        this.type = MonthVirathamFragment.SUBA_VIRATHAM;
    }

    public MonthVirathamViewPageAdapter(FragmentActivity fragmentActivity, int year, int type) {
        super(fragmentActivity);
        this.year = year;
        this.type = type;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MonthVirathamFragment.newInstance(LocalDate.of(year, position + 1, 1), type);
    }

    @Override
    public int getItemCount() {
        return LocalDate.MAX.getMonthValue();
    }
}
