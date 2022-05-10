package org.tem.calendar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tem.calendar.fragment.PanchangamFragment;

import java.time.DayOfWeek;

public class PanchangamViewPageAdapter extends FragmentStateAdapter {

    private final String panchangamType;
    private final int displayType;

    public PanchangamViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String panchangamType) {
        this(fragmentManager, lifecycle, PanchangamFragment.TYPE_STAND_ALONE, panchangamType);
    }

    public PanchangamViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int displayType, String panchangamType) {
        super(fragmentManager, lifecycle);
        this.panchangamType = panchangamType;
        this.displayType = displayType;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return PanchangamFragment.newInstance(position, displayType, panchangamType);
    }

    @Override
    public int getItemCount() {
        return DayOfWeek.values().length;
    }
}
