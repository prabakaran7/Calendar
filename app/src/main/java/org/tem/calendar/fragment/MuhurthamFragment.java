package org.tem.calendar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.R;
import org.tem.calendar.adapter.MuhurthamRecyclerAdapter;
import org.tem.calendar.databinding.FragmentMuhurthamBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.MuhurthamData;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MuhurthamFragment extends Fragment {
    private final LocalDate date;
    private FragmentMuhurthamBinding binding;

    public MuhurthamFragment(LocalDate date) {
        this.date = date;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMuhurthamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<MuhurthamData> muhurthamDataList = DBHelper.getInstance(requireContext()).getMuhurthamList(date.getYear(), date.getMonthValue());
        if (muhurthamDataList.isEmpty()) {
            binding.muhurthamRecyclerView.setVisibility(View.GONE);
            binding.emptyMessageTxt.setVisibility(View.VISIBLE);
            binding.emptyMessageTxt.setText(getString(R.string.empty_muhurtham_msg, getResources().getStringArray(R.array.en_month_names)[date.getMonthValue() - 1]));
            binding.infoLayout.setVisibility(View.GONE);
        } else {
            binding.muhurthamRecyclerView.setVisibility(View.VISIBLE);
            binding.emptyMessageTxt.setVisibility(View.GONE);
            binding.infoLayout.setVisibility(View.VISIBLE);
            muhurthamDataList.sort(Comparator.comparing(MuhurthamData::getDate));
            MuhurthamRecyclerAdapter adapter = new MuhurthamRecyclerAdapter(requireContext(), muhurthamDataList);
            binding.muhurthamRecyclerView.setAdapter(adapter);
            binding.muhurthamRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
    }
}
