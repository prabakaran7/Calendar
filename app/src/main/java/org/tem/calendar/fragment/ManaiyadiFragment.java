package org.tem.calendar.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.R;
import org.tem.calendar.adapter.ManaiyadiRecyclerAdapter;
import org.tem.calendar.databinding.FragmentManaiyadiBinding;
import org.tem.calendar.db.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManaiyadiFragment extends Fragment {

    private final int position;

    private FragmentManaiyadiBinding binding;

    private ManaiyadiFragment(int position){
        this.position = position;
    }

    public static ManaiyadiFragment newInstance(int position){
        return new ManaiyadiFragment(position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentManaiyadiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.notesTxt.setText(Html.fromHtml(String.format("<b>%s:</b>%s", getString(R.string.notes), getString(R.string.manaiyadi_notes)), Html.FROM_HTML_MODE_COMPACT));

        List<Pair<Integer, String>> dataSet = new ArrayList<>();
        Map<Integer, String> manaiyadiMap = DBHelper.getInstance(requireActivity()).getManaiyadiByType(position);
        for(Map.Entry<Integer, String> entry: manaiyadiMap.entrySet()){
            dataSet.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        ManaiyadiRecyclerAdapter adapter = new ManaiyadiRecyclerAdapter(dataSet);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(adapter);
    }
}
