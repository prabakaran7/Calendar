package org.tem.calendar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.R;
import org.tem.calendar.activities.FestivalActivity;
import org.tem.calendar.adapter.FestivalRecyclerAdapter;
import org.tem.calendar.databinding.FragmentFestivalBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.util.KeyValuePair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FestivalFragment extends Fragment {
    public static final int HOLIDAY = 1;
    public static final int HINDU_FESTIVALS = 2;
    public static final int CHRIST_FESTIVALS = 3;
    public static final int MUSLIM_FESTIVALS = 4;
    private final int type;
    private final LocalDate date;
    private FragmentFestivalBinding binding;

    @Keep
    public FestivalFragment(){
        this(LocalDate.now(), FestivalFragment.HOLIDAY);
    }

    public FestivalFragment(LocalDate date, int type) {
        this.date = date;
        this.type = type;
    }

    public static Fragment newInstance(LocalDate date, int type) {
        return new FestivalFragment(date, type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFestivalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Map<LocalDate, String> mapList;
        if (type == FestivalFragment.HINDU_FESTIVALS) {
            mapList = DBHelper.getInstance(requireActivity()).getHinduFestivalDays(date.getYear(), date.getMonthValue());
        } else if (type == FestivalFragment.CHRIST_FESTIVALS) {
            mapList = DBHelper.getInstance(requireActivity()).getChristFestivalDays(date.getYear(), date.getMonthValue());
        } else if (type == FestivalFragment.MUSLIM_FESTIVALS) {
            mapList = DBHelper.getInstance(requireActivity()).getMuslimFestivalDays(date.getYear(), date.getMonthValue());
        } else {
            mapList = DBHelper.getInstance(requireActivity()).getHolidays(date.getYear(), date.getMonthValue());
        }
        List<KeyValuePair> list = new ArrayList<>();
        for(Map.Entry<LocalDate, String> entry: mapList.entrySet()){
            list.add(new KeyValuePair(entry.getKey(), entry.getValue()));
        }
        if(!list.isEmpty()) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyMessageTxt.setVisibility(View.GONE);
            FestivalRecyclerAdapter adapter = new FestivalRecyclerAdapter(requireActivity(), list);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        } else {
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyMessageTxt.setVisibility(View.VISIBLE);
            binding.emptyMessageTxt.setText(getString(R.string.empty_festival_msg, getResources().getStringArray(R.array.en_month_names)[date.getMonthValue() - 1], getString(FestivalActivity.getSubtitle(type))));

        }
        super.onViewCreated(view, savedInstanceState);
    }

}
