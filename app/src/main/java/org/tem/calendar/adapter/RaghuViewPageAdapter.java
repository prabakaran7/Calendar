package org.tem.calendar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tem.calendar.fragment.RaghuEmaKuligaiFragment;

import java.time.DayOfWeek;

public class RaghuViewPageAdapter extends FragmentStateAdapter {
    // TODO Mapping between weekday and weeknames as in string
    public RaghuViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return RaghuEmaKuligaiFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return DayOfWeek.values().length;
    }
}
