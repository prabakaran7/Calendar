package org.tem.calendar.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import org.tem.calendar.R;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.databinding.FragmentMonthVirathamBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.VirathamMonthData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MonthVirathamFragment extends Fragment {

    public static final int SUBA_VIRATHAM = 1;
    public static final int ASUBA_VIRATHAM = 2;
    private final LocalDate selectedDate;
    private final int type;
    private String[] weekNames;
    private FragmentMonthVirathamBinding binding;

    private MonthVirathamFragment(LocalDate date, int type) {
        this.selectedDate = date;
        this.type = type;
    }

    public static Fragment newInstance(LocalDate date, int type) {
        return new MonthVirathamFragment(date, type);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMonthVirathamBinding.inflate(inflater, container, false);
        weekNames = getResources().getStringArray(R.array.weekday_names);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final List<VirathamMonthData> virathamList = DBHelper.getInstance(this.requireActivity())
                .getVirathamList(selectedDate.getYear(), selectedDate.getMonthValue());

        for (VirathamMonthData vd : virathamList) {
            if (ASUBA_VIRATHAM == type) {
                switch (vd.getViratham()) {
                    case 8:
                        binding.astami.getRoot().setVisibility(View.VISIBLE);
                        binding.astami.virathamHeader.setText(getString(R.string.astamiTxt));
                        binding.astami.virathamImage.setImageResource(R.drawable.astami);
                        setMuhurtham(binding.astami.virathamTxt, vd.getDate());
                        break;
                    case 9:
                        binding.navami.getRoot().setVisibility(View.VISIBLE);
                        binding.navami.virathamHeader.setText(getString(R.string.navamiTxt));
                        binding.navami.virathamImage.setImageResource(R.drawable.navami);
                        setMuhurtham(binding.navami.virathamTxt, vd.getDate());
                        break;
                }
            } else {
                switch (vd.getViratham()) {
                    case 0:
                        binding.amavasai.getRoot().setVisibility(View.VISIBLE);
                        binding.amavasai.virathamHeader.setText(getString(R.string.amavasaiTxt));
                        binding.amavasai.virathamImage.setImageResource(R.drawable.new_moon);
                        setMuhurtham(binding.amavasai.virathamTxt, vd.getDate());
                        break;
                    case 1:
                        binding.pournami.getRoot().setVisibility(View.VISIBLE);
                        binding.pournami.virathamHeader.setText(getString(R.string.pournamiTxt));
                        binding.pournami.virathamImage.setImageResource(R.drawable.full_moon);
                        setMuhurtham(binding.pournami.virathamTxt, vd.getDate());
                        break;
                    case 2:
                        binding.karthigai.getRoot().setVisibility(View.VISIBLE);
                        binding.karthigai.virathamHeader.setText(getString(R.string.karthigaiTxt));
                        binding.karthigai.virathamImage.setImageResource(R.drawable.star);
                        setMuhurtham(binding.karthigai.virathamTxt, vd.getDate());
                        break;
                    case 3:
                    case 7:
                        binding.sivarathri.getRoot().setVisibility(View.VISIBLE);
                        binding.sivarathri.virathamHeader.setText(getString(R.string.sivarathriTxt));
                        binding.sivarathri.virathamImage.setImageResource(R.drawable.sivarathri);
                        setMuhurtham(binding.sivarathri.virathamTxt, vd.getDate());
                        break;
                    case 4:
                        if (vd.getPirai() == 2) { //varpirai
                            binding.chathurthi.getRoot().setVisibility(View.VISIBLE);
                            binding.chathurthi.virathamHeader.setText(getString(R.string.chathurthiTxt));
                            binding.chathurthi.virathamImage.setImageResource(R.drawable.chathurthi);
                            setMuhurtham(binding.chathurthi.virathamTxt, vd.getDate());
                        } else if (vd.getPirai() == 1) { //theipirai
                            binding.sankataChathurthi.getRoot().setVisibility(View.VISIBLE);
                            binding.sankataChathurthi.virathamHeader.setText(getString(R.string.sankada_chathurthi_txt));
                            binding.sankataChathurthi.virathamImage.setImageResource(R.drawable.chathurthi);
                            setMuhurtham(binding.sankataChathurthi.virathamTxt, vd.getDate());
                        }
                        break;
                    case 5:
                        binding.thiruvonam.getRoot().setVisibility(View.VISIBLE);
                        binding.thiruvonam.virathamHeader.setText(getString(R.string.thiruvonamTxt));
                        binding.thiruvonam.virathamImage.setImageResource(R.drawable.thiruvonam);
                        setMuhurtham(binding.thiruvonam.virathamTxt, vd.getDate());
                        break;
                    case 6:
                        if (vd.getPirai() == 2) { //varpirai
                            binding.shastiPlus.getRoot().setVisibility(View.VISIBLE);
                            binding.shastiPlus.virathamHeader.setText(getString(R.string.shastiValarpiraiTxt));
                            binding.shastiPlus.virathamImage.setImageResource(R.drawable.shasti);
                            setMuhurtham(binding.shastiPlus.virathamTxt, vd.getDate());
                        } else if (vd.getPirai() == 1) { //theipirai
                            binding.shasti.getRoot().setVisibility(View.VISIBLE);
                            binding.shasti.virathamHeader.setText(getString(R.string.shastiTxt));
                            binding.shasti.virathamImage.setImageResource(R.drawable.shasti);
                            setMuhurtham(binding.shasti.virathamTxt, vd.getDate());
                        }
                        break;

                    case 12:
                        binding.egadesi.getRoot().setVisibility(View.VISIBLE);
                        binding.egadesi.virathamHeader.setText(getString(R.string.ekadeshiTxt));
                        binding.egadesi.virathamImage.setImageResource(R.drawable.ekadeshi);
                        setMuhurtham(binding.egadesi.virathamTxt, vd.getDate());
                        break;
                    case 13:
                        if (vd.getPirai() == 2) { //varpirai
                            binding.pradosamPlus.getRoot().setVisibility(View.VISIBLE);
                            binding.pradosamPlus.virathamHeader.setText(getString(R.string.pradosamValarPiraiTxt));
                            binding.pradosamPlus.virathamImage.setImageResource(R.drawable.pradhosam);
                            setMuhurtham(binding.pradosamPlus.virathamTxt, vd.getDate());
                        } else if (vd.getPirai() == 1) { //theipirai
                            binding.pradosam.getRoot().setVisibility(View.VISIBLE);
                            binding.pradosam.virathamHeader.setText(getString(R.string.pradosamTheiPiraiTxt));
                            binding.pradosam.virathamImage.setImageResource(R.drawable.pradhosam);
                            setMuhurtham(binding.pradosam.virathamTxt, vd.getDate());
                        }
                        break;
                }
            }

        }
        if (ASUBA_VIRATHAM == type) {
            List<LocalDate> knList = DBHelper.getInstance(this.requireActivity()).KariNaalList(selectedDate.getYear(), selectedDate.getMonthValue());
            if (!knList.isEmpty()) {
                binding.karinaal.getRoot().setVisibility(View.VISIBLE);
                binding.karinaal.virathamHeader.setText(getString(R.string.kariNaalTxt));
                binding.karinaal.virathamImage.setImageResource(R.drawable.hotsun);
                for (LocalDate ld : knList) {
                    setMuhurtham(binding.karinaal.virathamTxt, ld);
                }
            }
        }

    }

    private void setMuhurtham(AppCompatTextView virathamTxt, String date) {
        setMuhurtham(virathamTxt, DateUtil.ofLocalDate(date));
    }

    private void setMuhurtham(AppCompatTextView virathamTxt, LocalDate date) {
        if (virathamTxt.getText().length() > 0) {
            virathamTxt.append(", ");
        }
        virathamTxt.append(date.getDayOfMonth() + " " + weekNames[date.getDayOfWeek().getValue() - 1]);
    }
}
