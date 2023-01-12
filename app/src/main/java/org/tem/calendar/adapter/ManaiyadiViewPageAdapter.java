package org.tem.calendar.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tem.calendar.fragment.ManaiyadiFragment;

public class ManaiyadiViewPageAdapter extends FragmentStateAdapter {

    public static final int SIZE = 2;
    public ManaiyadiViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ManaiyadiFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return SIZE;
    }


}
