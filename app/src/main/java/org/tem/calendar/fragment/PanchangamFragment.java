package org.tem.calendar.fragment;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.R;
import org.tem.calendar.adapter.PanchangamRecyclerAdapter;
import org.tem.calendar.databinding.FragmentPanchangamBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.PanchangamData;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanchangamFragment extends Fragment {

    public static final String GOWRI_PANCHANGAM = "gowri_panchangam";
    public static final String GRAHA_ORAI_PANCHANGAM = "graha_orai_panchangam";
    private static final Map<Integer, String> GOWRI_LABEL_MAP = new HashMap<Integer, String>() {
        {
            put(1, "6.00 - 7.30");
            put(2, "7.30 - 9.00");
            put(3, "9.00 - 10.30");
            put(4, "10.30 - 12.00");
            put(5, "12.00 - 1.30");
            put(6, "1.30 - 3.00");
            put(7, "3.00 - 4.30");
            put(8, "4.30 - 6.00");
        }
    };
    private static final Map<Integer, String> GRAHA_ORAI_LABEL_MAP = new HashMap<Integer, String>() {
        {
            put(1, "6.00 - 7.00");
            put(2, "7.00 - 8.00");
            put(3, "8.00 - 9.00");
            put(4, "9.00 - 10.00");
            put(5, "10.00 - 11.00");
            put(6, "11.00 - 12.00");
            put(7, "12.00 - 1.00");
            put(8, "1.00 - 2.00");
            put(9, "2.00 - 3.00");
            put(10, "3.00 - 4.00");
            put(11, "4.00 - 5.00");
            put(12, "5.00 - 6.00");
        }
    };
    public static final int TYPE_STAND_ALONE = 1;
    public static final int TYPE_DAILY = 0;
    private final DayOfWeek dayOfWeek;
    private final int displayType;
    private final String panchangamType;
    private FragmentPanchangamBinding binding;

    public PanchangamFragment(){
        this(DayOfWeek.FRIDAY, TYPE_DAILY, GOWRI_PANCHANGAM);
    }

    public PanchangamFragment(DayOfWeek dayOfWeek, int displayType, String panchangamType) {
        this.dayOfWeek = dayOfWeek;
        this.displayType = displayType;
        this.panchangamType = panchangamType;
    }

    public static PanchangamFragment newInstance(int position, int displayType, String panchangamType) {
        Pair<Integer, String> weekday = CalendarApp.getWeekDayNameList().get(position);
        return new PanchangamFragment(DayOfWeek.of(weekday.first), displayType, panchangamType);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPanchangamBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        binding.notesTxt.setVisibility(displayType == TYPE_DAILY ? View.GONE : View.VISIBLE);
        List<PanchangamData> gdList = new ArrayList<>();
        if (GOWRI_PANCHANGAM.equals(panchangamType)) {
            gdList.addAll(DBHelper.getInstance(requireContext()).getGowriPanchangamWeekData(dayOfWeek));
            String[] values = requireActivity().getResources().getStringArray(R.array.gowri_values);
            for (PanchangamData data : gdList) {
                data.setTimeIndexText(GOWRI_LABEL_MAP.get(data.getTimeIndex()));
                data.setMorningText(values[data.getMorningIndex() - 1]);
                data.setEveningText(values[data.getEveningIndex() - 1]);
            }
            binding.panchangamLayout.panchangamTxt.setText(getString(R.string.gowriPanchangamLabel));
            binding.panchangamLayout.notesTxt.setText(R.string.suba_panchangam_note);

        } else if(GRAHA_ORAI_PANCHANGAM.equals(panchangamType)){
            gdList.addAll(DBHelper.getInstance(requireContext()).getGrahaOraiPanchangamWeekData(dayOfWeek));
            String[] values = requireActivity().getResources().getStringArray(R.array.orai_values);
            for (PanchangamData data : gdList) {
                data.setTimeIndexText(GRAHA_ORAI_LABEL_MAP.get(data.getTimeIndex()));
                data.setMorningText(values[data.getMorningIndex() - 1]);
                data.setEveningText(values[data.getEveningIndex() - 1]);
            }
            binding.panchangamLayout.panchangamTxt.setText(getString(R.string.graha_orai_label));
            binding.panchangamLayout.notesTxt.setText(R.string.suba_orai_note);
        }
        binding.panchangamLayout.panchangamRecyclerView.setAdapter(new PanchangamRecyclerAdapter(gdList));
        binding.panchangamLayout.panchangamRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }
}