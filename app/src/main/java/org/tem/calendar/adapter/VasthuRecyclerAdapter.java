package org.tem.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.databinding.VasthuItemBinding;
import org.tem.calendar.model.VasthuData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VasthuRecyclerAdapter extends RecyclerView.Adapter<VasthuRecyclerAdapter.ViewHolder> {

    private final List<VasthuData> dataList;
    private final Context context;
    private final String[] enMonths;
    private final String[] dayNames;
    private final String[] taMonths;

    public VasthuRecyclerAdapter(Context context, List<VasthuData> dataList) {
        this.dataList = dataList;
        this.context = context;
        enMonths = context.getResources().getStringArray(R.array.en_month_names);
        taMonths = context.getResources().getStringArray(R.array.tamizh_month_names);
        dayNames = context.getResources().getStringArray(R.array.weekday_names);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VasthuItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.vasthu_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VasthuData vd = dataList.get(position);
        if (null != vd) {
            System.out.println(vd);
            LocalDate date = DateUtil.ofLocalDate(vd.getDate());
            String title = enMonths[date.getMonthValue() - 1] + " " + date.getDayOfMonth() + ", " + dayNames[date.getDayOfWeek().getValue() - 1];
            title += " - " + taMonths[vd.getTmonth() - 1] + " " + vd.getTday();
            String subTitle = getPrefixedTime(context, vd.getTime());
            holder.binding.title.setText(title);
            holder.binding.subtitle.setText(subTitle);
        }
    }

    private String getPrefixedTime(Context context, String time) {
        LocalTime lt = LocalTime.parse(time.split("[ ]*[-][ ]*")[0], DateTimeFormatter.ofPattern("H.m"));
        if (lt.getHour() < 12) {
            return context.getString(R.string.morningLabel) + " " + time;
        } else {
            return context.getString(R.string.eveningLabel) + " " + time;
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        VasthuItemBinding binding;

        public ViewHolder(VasthuItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
