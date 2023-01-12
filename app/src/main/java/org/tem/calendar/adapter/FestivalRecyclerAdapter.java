package org.tem.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.databinding.FestivalItemBinding;
import org.tem.calendar.util.KeyValuePair;

import java.util.List;
import java.util.Locale;

public class FestivalRecyclerAdapter extends RecyclerView.Adapter<FestivalRecyclerAdapter.ViewHolder> {

    private final List<KeyValuePair> dataSet;
    private final String[] weekNames;

    public FestivalRecyclerAdapter(Context context, List<KeyValuePair> list) {
        this.dataSet = list;
        this.weekNames = context.getResources().getStringArray(R.array.weekday_names);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.festival_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KeyValuePair kvp = dataSet.get(position);
        if (null == kvp) return;
        holder.binding.dateTxt.setText(String.format(Locale.getDefault(), "%d", kvp.getKey().getDayOfMonth()));
        holder.binding.dayTxt.setText(weekNames[kvp.getKey().getDayOfWeek().getValue() - 1]);
        holder.binding.festivalTxt.setText(kvp.getValue());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final FestivalItemBinding binding;

        public ViewHolder(@NonNull FestivalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
