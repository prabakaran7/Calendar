package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.activities.YearActivity;
import org.tem.calendar.custom.DateModel;
import org.tem.calendar.databinding.YearMonthItemBinding;

import java.util.List;

public class YearMonthRecyclerAdapter extends RecyclerView.Adapter<YearMonthRecyclerAdapter.ViewHolder> {

    private final List<List<DateModel>> dataSet;
    private final String[] monthNames;

    private final YearActivity activity;

    public YearMonthRecyclerAdapter(YearActivity activity, List<List<DateModel>> dataSet) {
        this.dataSet = dataSet;
        this.activity = activity;
        monthNames = activity.getResources().getStringArray(R.array.en_month_names);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<DateModel> dataModel = dataSet.get(position);
        holder.binding.monthTitle.setText(monthNames[position]);
        holder.binding.monthView.setListener(activity);
        holder.binding.monthView.refreshData(dataModel);

        holder.binding.monthTitle.setOnClickListener(view ->{
            activity.monthClick(dataModel.get(0).getDate());
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final YearMonthItemBinding binding;

        public ViewHolder(@NonNull YearMonthItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static ViewHolder of(ViewGroup parent) {
            return new ViewHolder(
                    YearMonthItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
            );
        }
    }
}
