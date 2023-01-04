package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.DashboardActivity;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.databinding.DashboardCategoryBinding;
import org.tem.calendar.model.Dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardCategoryRecyclerViewAdapter extends RecyclerView.Adapter<DashboardCategoryRecyclerViewAdapter.ViewHolder> {

    private final List<Map.Entry<String, List<Dashboard>>> dataSet;
    private final DashboardActivity activity;

    public DashboardCategoryRecyclerViewAdapter(DashboardActivity activity) {
        this.dataSet = new ArrayList<>(CalendarApp.getCategoryMap().entrySet());
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map.Entry<String, List<Dashboard>> entry = dataSet.get(position);
        if (StringUtils.isBlank(entry.getKey())) {
            holder.binding.catLabel.setVisibility(View.GONE);
        } else {
            holder.binding.catLabel.setVisibility(View.VISIBLE);
            holder.binding.catLabel.setText(entry.getKey());
        }

        System.out.println(entry.getValue());

        holder.binding.categoryItemRecyclerView.setAdapter(new DashboardCategoryItemRecyclerViewAdapter(activity, entry.getValue()));
        holder.binding.categoryItemRecyclerView.setHasFixedSize(true);
        holder.binding.categoryItemRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        DashboardCategoryBinding binding;

        public ViewHolder(@NonNull DashboardCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static ViewHolder of(ViewGroup parent) {
            return new ViewHolder(
                    DashboardCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
            );
        }
    }
}
