package org.tem.calendar.fragment;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.R;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.databinding.FragmentRaghuEmaKuligaiBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.WeekData;

import java.time.DayOfWeek;

public class RaghuEmaKuligaiFragment extends Fragment {

    public static final int TYPE_STAND_ALONE = 1;
    public static final int TYPE_DAILY = 0;
    private final DayOfWeek dayOfWeek;
    private FragmentRaghuEmaKuligaiBinding binding;
    private final int type;


    @Keep
    public RaghuEmaKuligaiFragment(){
        this(DayOfWeek.FRIDAY, TYPE_DAILY);
    }

    public RaghuEmaKuligaiFragment(DayOfWeek dayOfWeek, int type) {
        this.dayOfWeek = dayOfWeek;
        this.type = type;
    }

    public static RaghuEmaKuligaiFragment newInstance(int position) {
        Pair<Integer, String> weekday = CalendarApp.getWeekDayNameList().get(position);
        return new RaghuEmaKuligaiFragment(DayOfWeek.of(weekday.first), TYPE_STAND_ALONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRaghuEmaKuligaiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);


        binding.notesTxt.setVisibility(type == TYPE_DAILY ? View.GONE : View.VISIBLE);

        WeekData wd = DBHelper.getInstance(requireContext()).getWeekData(dayOfWeek);

        // Raghu, ema & kuligai
        binding.raahuEmaLayout.raaghuTxt.setText(wd.getRaaghu());
        binding.raahuEmaLayout.emaTxt.setText(wd.getEma());
        binding.raahuEmaLayout.kuligaiTxt.setText(wd.getKuligai());
        binding.raahuEmaLayout.karananTxt.setText(wd.getKaranan());

        // Vaara Soolai
        binding.vaaraSoolaiLayout.soolamTxt.setText(getResources().getStringArray(R.array.directions)[wd.getSoolam() - 1]);
        binding.vaaraSoolaiLayout.parigaramTxt.setText(getResources().getStringArray(R.array.parigaram)[wd.getParigaram() - 1]);
        int naa = wd.getSoolamTime();
        int[] hourMin = DateUtil.naazhigaiToHourMin(naa);
        binding.vaaraSoolaiLayout.soolamTimeTxt.setText(getString(R.string.nazhigaiTime, naa, hourMin[0], hourMin[1]));

    }
}