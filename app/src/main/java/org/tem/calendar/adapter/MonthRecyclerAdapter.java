package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.R;
import org.tem.calendar.activities.MonthActivity;
import org.tem.calendar.custom.DateModel;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.databinding.MonthItemBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.MonthViewModel;
import org.tem.calendar.model.MuhurthamData;
import org.tem.calendar.model.VirathamMonthData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MonthRecyclerAdapter extends RecyclerView.Adapter<MonthRecyclerAdapter.ViewHolder> {

    private final MonthViewModel model;
    private final MonthActivity activity;
    private final String[] weekNames;
    private final String[] monthEnNames;
    private final String[] monthNames;

    public MonthRecyclerAdapter(MonthActivity activity, MonthViewModel model) {
        this.model = model;
        this.activity = activity;
        this.weekNames = activity.getResources().getStringArray(R.array.weekday_names);
        this.monthEnNames = activity.getResources().getStringArray(R.array.en_month_names);
        this.monthNames = activity.getResources().getStringArray(R.array.tamizh_month_names);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final LocalDate selectedDate = model.getList().get(position);

        clearSetAll(holder.binding);
        final List<Integer> tamilMonth = new ArrayList<>();
        holder.binding.engMonthTxt.setText(
                String.format(Locale.getDefault(), "%s %d",
                        monthEnNames[selectedDate.getMonthValue() - 1], selectedDate.getYear()));

        holder.binding.nextBtn.setOnClickListener(view -> activity.moveForward());
        holder.binding.prevBtn.setOnClickListener(view -> activity.moveBackward());

        if (CalendarApp.MAX_DATE.getYear() == selectedDate.getYear() && CalendarApp.MAX_DATE.getMonthValue() == selectedDate.getMonthValue()) {
            holder.binding.nextBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.nextBtn.setVisibility(View.VISIBLE);
        }

        if (CalendarApp.MIN_DATE.getYear() == selectedDate.getYear() && CalendarApp.MIN_DATE.getMonthValue() == selectedDate.getMonthValue()) {
            holder.binding.prevBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.binding.prevBtn.setVisibility(View.VISIBLE);
        }

        holder.binding.monthView.setListener(activity);
        holder.binding.monthView.refreshData(generateDates(holder.binding, selectedDate, tamilMonth));

        if (tamilMonth.size() > 1) {
            holder.binding.tamilMonthTxt.setText(String.format(Locale.getDefault(),
                    "%s - %s", monthNames[tamilMonth.get(0) - 1], monthNames[tamilMonth.get(tamilMonth.size() - 1) - 1]));
        }


    }

    private void clearSetAll(@NonNull MonthItemBinding binding) {
        binding.tamilMonthTxt.setText("");
        binding.muhurthamLayout.muhurthamRecyclerView.removeAllViewsInLayout();
        binding.govtHolidayLayout.listView.removeAllViewsInLayout();
        binding.viratham.amavasai.virathamTxt.setText("");
        binding.viratham.chathurthi.virathamTxt.setText("");
        binding.viratham.egadesi.virathamTxt.setText("");
        binding.viratham.karthigai.virathamTxt.setText("");
        binding.viratham.pournami.virathamTxt.setText("");
        binding.viratham.thiruvonam.virathamTxt.setText("");
        binding.viratham.sivarathri.virathamTxt.setText("");
        binding.viratham.pradosam.virathamTxt.setText("");
        binding.viratham.pradosamPlus.virathamTxt.setText("");
        binding.viratham.sankataChathurthi.virathamTxt.setText("");
        binding.viratham.shasti.virathamTxt.setText("");
        binding.viratham.shastiPlus.virathamTxt.setText("");
        binding.hinduFestivalLayout.listView.removeAllViewsInLayout();
        binding.monthView.removeAllViewsInLayout();
        binding.otherDays.astami.virathamTxt.setText("");
        binding.otherDays.karinaal.virathamTxt.setText("");
        binding.otherDays.navami.virathamTxt.setText("");
    }

    @NonNull
    private List<DateModel> generateDates(@NonNull MonthItemBinding binding, @NonNull LocalDate selectedDate, @NonNull List<Integer> tamilMonth) {
        List<DateModel> dates = new ArrayList<>();

        List<MonthData> dataList = DBHelper.getInstance(activity).getDates(selectedDate.getYear(), selectedDate.getMonthValue());
        tamilMonth.clear();
        if (dataList.isEmpty()) {
            for (int i = 0; i < selectedDate.lengthOfMonth(); i++) {
                LocalDate date = selectedDate.plusDays(i);
                DateModel model = new DateModel();
                model.setDate(date);
                model.setPrimeDay(date.getDayOfMonth());
                dates.add(model);
            }
        } else {
            //get Viratham List
            List<VirathamMonthData> vdList = DBHelper.getInstance(activity).getVirathamList(selectedDate.getYear(), selectedDate.getMonthValue());
            Map<LocalDate, List<VirathamMonthData>> vdMap = new TreeMap<>();
            for (VirathamMonthData vmd : vdList) {
                LocalDate ld = DateUtil.ofLocalDate(vmd.getDate());
                if (!vdMap.containsKey(ld)) {
                    vdMap.put(ld, new ArrayList<>());
                }

                Objects.requireNonNull(vdMap.get(ld)).add(vmd);
            }

            //  Holiday/Festival Day List
            Map<LocalDate, String> hindhuFestivalMap = DBHelper.getInstance(activity).getHinduFestivalDays(selectedDate.getYear(), selectedDate.getMonthValue());

            List<String> festivals = new ArrayList<>();

            for (Map.Entry<LocalDate, String> entry : hindhuFestivalMap.entrySet()) {
                festivals.add(
                        entry.getKey().getDayOfMonth() + " " + weekNames[entry.getKey().getDayOfWeek().getValue() - 1]
                                + " - " + entry.getValue()
                );
            }
            if (festivals.isEmpty()) {
                festivals.add("          -          ");
            }
            ArrayAdapter<String> festivalAdapter = new ArrayAdapter<>(activity, R.layout.simple_textview_item, R.id.text, festivals);
            binding.hinduFestivalLayout.listView.setAdapter(festivalAdapter);

            binding.hinduFestivalLayout.headerTitle.setText(activity.getString(R.string.hindu_festivals));

            Map<LocalDate, String> holidayMap = DBHelper.getInstance(activity).getHolidays(selectedDate.getYear(), selectedDate.getMonthValue());
            List<String> holidays = new ArrayList<>();
            for (Map.Entry<LocalDate, String> entry : holidayMap.entrySet()) {
                holidays.add(
                        entry.getKey().getDayOfMonth() + " " + weekNames[entry.getKey().getDayOfWeek().getValue() - 1]
                                + " - " + entry.getValue()
                );
            }
            if (holidays.isEmpty()) {
                holidays.add("          -          ");
            }
            ArrayAdapter<String> holidayAdapter = new ArrayAdapter<>(activity, R.layout.simple_textview_item, R.id.text, holidays);
            binding.govtHolidayLayout.listView.setAdapter(holidayAdapter);

            binding.govtHolidayLayout.headerTitle.setText(activity.getString(R.string.govtHolidays));


            // Muhurtham List
            final Map<LocalDate, MuhurthamData> muhurthamDataMap = DBHelper.getInstance(activity)
                    .getMuhurthamList(selectedDate.getYear(), selectedDate.getMonthValue())
                    .stream().collect(Collectors.toMap(MuhurthamData::getDate, e -> e));

            dataList.sort(Comparator.comparing(o -> LocalDate.of(o.getYear(), o.getMonth(), o.getDay())));

            for (MonthData md : dataList) {
                DateModel dateModel = new DateModel();
                dateModel.setDate(LocalDate.of(md.getYear(), md.getMonth(), md.getDay()));
                dateModel.setPrimeDay(md.getDay());
                dateModel.setSecondaryDay(md.getTday());
                if (holidayMap.containsKey(dateModel.getDate())) {
                    dateModel.setHoliday(true);
                }

                if (!tamilMonth.contains(md.getTmonth())) {
                    tamilMonth.add(md.getTmonth());
                }

                if (vdMap.containsKey(dateModel.getDate())) {
                    List<VirathamMonthData> vmdList = vdMap.get(dateModel.getDate());
                    assert vmdList != null;
                    for (VirathamMonthData vd : vmdList) {
                        switch (vd.getViratham()) {
                            case 0:
                                dateModel.setTithi(R.drawable.new_moon);
                                binding.viratham.amavasai.getRoot().setVisibility(View.VISIBLE);
                                binding.viratham.amavasai.virathamHeader.setText(activity.getString(R.string.amavasaiTxt));
                                Glide.with(activity)
                                        .load(R.drawable.new_moon)
                                        .into(binding.viratham.amavasai.virathamImage);
                                setMuhurtham(binding.viratham.amavasai.virathamTxt, vd.getDate());
                                break;
                            case 1:
                                dateModel.setTithi(R.drawable.full_moon);
                                binding.viratham.pournami.getRoot().setVisibility(View.VISIBLE);
                                binding.viratham.pournami.virathamHeader.setText(activity.getString(R.string.pournamiTxt));
                                Glide.with(activity)
                                        .load(R.drawable.full_moon)
                                        .into(binding.viratham.pournami.virathamImage);
                                setMuhurtham(binding.viratham.pournami.virathamTxt, vd.getDate());
                                break;
                            case 2:
                                dateModel.setStar(R.drawable.star);
                                binding.viratham.karthigai.getRoot().setVisibility(View.VISIBLE);
                                binding.viratham.karthigai.virathamHeader.setText(activity.getString(R.string.karthigaiTxt));
                                Glide.with(activity)
                                        .load(R.drawable.star)
                                        .into(binding.viratham.karthigai.virathamImage);
                                setMuhurtham(binding.viratham.karthigai.virathamTxt, vd.getDate());
                                break;
                            case 3:
                            case 7:
                                dateModel.setTithi(R.drawable.sivarathri);
                                binding.viratham.sivarathri.getRoot().setVisibility(View.VISIBLE);
                                binding.viratham.sivarathri.virathamHeader.setText(activity.getString(R.string.sivarathriTxt));
                                Glide.with(activity)
                                        .load(R.drawable.sivarathri)
                                        .into(binding.viratham.sivarathri.virathamImage);
                                setMuhurtham(binding.viratham.sivarathri.virathamTxt, vd.getDate());
                                break;
                            case 4:
                                dateModel.setTithi(R.drawable.chathurthi);
                                if (vd.getPirai() == 2) { //valarpirai
                                    binding.viratham.chathurthi.getRoot().setVisibility(View.VISIBLE);
                                    binding.viratham.chathurthi.virathamHeader.setText(activity.getString(R.string.chathurthiTxt));
                                    Glide.with(activity)
                                            .load(R.drawable.chathurthi)
                                            .into(binding.viratham.pournami.virathamImage);
                                    setMuhurtham(binding.viratham.chathurthi.virathamTxt, vd.getDate());
                                } else if (vd.getPirai() == 1) { //theipirai
                                    binding.viratham.sankataChathurthi.getRoot().setVisibility(View.VISIBLE);
                                    binding.viratham.sankataChathurthi.virathamHeader.setText(activity.getString(R.string.sankada_chathurthi_txt));
                                    Glide.with(activity)
                                            .load(R.drawable.chathurthi)
                                            .into(binding.viratham.chathurthi.virathamImage);
                                    setMuhurtham(binding.viratham.sankataChathurthi.virathamTxt, vd.getDate());
                                }
                                break;
                            case 5:
                                dateModel.setStar(R.drawable.thiruvonam);
                                binding.viratham.thiruvonam.getRoot().setVisibility(View.VISIBLE);
                                binding.viratham.thiruvonam.virathamHeader.setText(activity.getString(R.string.thiruvonamTxt));
                                Glide.with(activity)
                                        .load(R.drawable.thiruvonam)
                                        .into(binding.viratham.thiruvonam.virathamImage);
                                setMuhurtham(binding.viratham.thiruvonam.virathamTxt, vd.getDate());
                                break;
                            case 6:
                                dateModel.setTithi(R.drawable.shasti);
                                if (vd.getPirai() == 2) { //valarpirai
                                    binding.viratham.shastiPlus.getRoot().setVisibility(View.VISIBLE);
                                    binding.viratham.shastiPlus.virathamHeader.setText(activity.getString(R.string.shastiValarpiraiTxt));
                                    Glide.with(activity)
                                            .load(R.drawable.shasti)
                                            .into(binding.viratham.shastiPlus.virathamImage);
                                    setMuhurtham(binding.viratham.shastiPlus.virathamTxt, vd.getDate());
                                } else if (vd.getPirai() == 1) { //theipirai
                                    binding.viratham.shasti.getRoot().setVisibility(View.VISIBLE);
                                    binding.viratham.shasti.virathamHeader.setText(activity.getString(R.string.shastiTxt));
                                    Glide.with(activity)
                                            .load(R.drawable.shasti)
                                            .into(binding.viratham.shasti.virathamImage);
                                    setMuhurtham(binding.viratham.shasti.virathamTxt, vd.getDate());
                                }
                                break;
                            case 8:
                                dateModel.setTithi(R.drawable.astami);
                                binding.otherDays.astami.getRoot().setVisibility(View.VISIBLE);
                                binding.otherDays.astami.virathamHeader.setText(activity.getString(R.string.astamiTxt));
                                Glide.with(activity)
                                        .load(R.drawable.astami)
                                        .into(binding.otherDays.astami.virathamImage);
                                setMuhurtham(binding.otherDays.astami.virathamTxt, vd.getDate());
                                break;
                            case 9:
                                dateModel.setTithi(R.drawable.navami);
                                binding.otherDays.navami.getRoot().setVisibility(View.VISIBLE);
                                binding.otherDays.navami.virathamHeader.setText(activity.getString(R.string.navamiTxt));
                                Glide.with(activity)
                                        .load(R.drawable.navami)
                                        .into(binding.otherDays.navami.virathamImage);
                                setMuhurtham(binding.otherDays.navami.virathamTxt, vd.getDate());
                                break;
                            case 12:
                                dateModel.setTithi(R.drawable.ekadeshi);
                                binding.viratham.egadesi.getRoot().setVisibility(View.VISIBLE);
                                binding.viratham.egadesi.virathamHeader.setText(activity.getString(R.string.ekadeshiTxt));
                                Glide.with(activity)
                                        .load(R.drawable.ekadeshi)
                                        .into(binding.viratham.egadesi.virathamImage);
                                setMuhurtham(binding.viratham.egadesi.virathamTxt, vd.getDate());
                                break;
                            case 13:
                                dateModel.setTithi(R.drawable.pradhosam);
                                if (vd.getPirai() == 2) { //valarpirai
                                    binding.viratham.pradosamPlus.getRoot().setVisibility(View.VISIBLE);
                                    binding.viratham.pradosamPlus.virathamHeader.setText(activity.getString(R.string.pradosamValarPiraiTxt));
                                    Glide.with(activity)
                                            .load(R.drawable.pradhosam)
                                            .into(binding.viratham.pradosamPlus.virathamImage);
                                    setMuhurtham(binding.viratham.pradosamPlus.virathamTxt, vd.getDate());
                                } else if (vd.getPirai() == 1) { //theipirai
                                    binding.viratham.pradosam.getRoot().setVisibility(View.VISIBLE);
                                    binding.viratham.pradosam.virathamHeader.setText(activity.getString(R.string.pradosamTheiPiraiTxt));
                                    Glide.with(activity)
                                            .load(R.drawable.pradhosam)
                                            .into(binding.viratham.pradosam.virathamImage);
                                    setMuhurtham(binding.viratham.pradosam.virathamTxt, vd.getDate());
                                }
                                break;
                        }
                    }
                }

                if (muhurthamDataMap.containsKey(dateModel.getDate())) {
                    dateModel.setMuhurtham(R.drawable.wedding);
                }
                dates.add(dateModel);
            }
            loadMuhurtham(binding, selectedDate, muhurthamDataMap);

            List<LocalDate> knList = DBHelper.getInstance(activity).KariNaalList(selectedDate.getYear(), selectedDate.getMonthValue());
            Collections.sort(knList);
            if (!knList.isEmpty()) {
                binding.otherDays.karinaal.virathamHeader.setText(activity.getString(R.string.kariNaalTxt));
                Glide.with(activity)
                        .load(R.drawable.hotsun)
                        .into(binding.otherDays.karinaal.virathamImage);
                for (LocalDate ld : knList) {
                    setMuhurtham(binding.otherDays.karinaal.virathamTxt, ld);
                }
            }
        }
        return dates;
    }

    private void setMuhurtham(AppCompatTextView virathamTxt, String date) {
        setMuhurtham(virathamTxt, DateUtil.ofLocalDate(date));
    }

    private void setMuhurtham(@NonNull AppCompatTextView virathamTxt, LocalDate date) {
        if (virathamTxt.getText().length() > 0) {
            virathamTxt.append(", ");
        }
        virathamTxt.append(date.getDayOfMonth() + " " + weekNames[date.getDayOfWeek().getValue() - 1]);
    }

    @Override
    public int getItemCount() {
        return model.getList().size();
    }

    private void loadMuhurtham(@NonNull MonthItemBinding binding, @NonNull LocalDate selectedDate, @NonNull Map<LocalDate, MuhurthamData> muhurthamDataMap) {
        if (muhurthamDataMap.isEmpty()) {
            binding.muhurthamLayout.muhurthamLinearLayout.setVisibility(View.GONE);
            binding.muhurthamLayout.muhurthamEmptyTxt.setVisibility(View.VISIBLE);
            binding.muhurthamLayout.muhurthamEmptyTxt.setText(activity.getString(R.string.empty_muhurtham_msg, activity.getResources().getStringArray(R.array.en_month_names)[selectedDate.getMonthValue() - 1]));
        } else {
            binding.muhurthamLayout.muhurthamLinearLayout.setVisibility(View.VISIBLE);
            binding.muhurthamLayout.muhurthamEmptyTxt.setVisibility(View.GONE);
            Map<LocalDate, MuhurthamData> mdList = new TreeMap<>(muhurthamDataMap);
            binding.muhurthamLayout.muhurthamRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
            MonthMuhurthamRecyclerAdapter adapter = new MonthMuhurthamRecyclerAdapter(activity, new ArrayList<>(mdList.values()));
            binding.muhurthamLayout.muhurthamRecyclerView.setAdapter(adapter);
            binding.muhurthamLayout.muhurthamRecyclerView.suppressLayout(true);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MonthItemBinding binding;

        public ViewHolder(@NonNull MonthItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public static ViewHolder of(@NonNull ViewGroup parent) {
            return new ViewHolder(MonthItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent, false
            ));
        }

    }
}
