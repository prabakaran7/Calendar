package org.tem.calendar.adapter;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.tem.calendar.fragment.MuhurthamFragment;

import java.time.LocalDate;
import java.time.Month;

public class MuhurthamViewPageAdapter extends FragmentStateAdapter {

    private final int year;
    public MuhurthamViewPageAdapter(AppCompatActivity context, int year) {
        super(context.getSupportFragmentManager(), context.getLifecycle());
        this.year = year;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new MuhurthamFragment(LocalDate.of(year, position + 1, 1));
    }

    @Override
    public int getItemCount() {
        return Month.values().length;
    }
}
