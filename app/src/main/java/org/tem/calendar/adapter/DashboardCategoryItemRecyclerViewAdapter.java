package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.DashboardActivity;
import org.tem.calendar.databinding.DashboardCategoryItemBinding;
import org.tem.calendar.model.Dashboard;

import java.util.List;

public class DashboardCategoryItemRecyclerViewAdapter extends RecyclerView.Adapter<DashboardCategoryItemRecyclerViewAdapter.ViewHolder> {
    private final DashboardActivity activity;
    private final List<Dashboard> dataSet;

    public DashboardCategoryItemRecyclerViewAdapter(DashboardActivity activity, List<Dashboard> dataSet) {
        this.activity = activity;
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dashboard dashboard = dataSet.get(position);
        System.out.println(dashboard);
        holder.binding.itemImage.setImageResource(dashboard.getImageResourceId());
        holder.binding.itemTxt.setText(dashboard.getName());
        holder.binding.getRoot().setOnClickListener(view->{
            activity.onClick(dashboard);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        DashboardCategoryItemBinding binding;

        public ViewHolder(@NonNull DashboardCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static ViewHolder of(ViewGroup parent) {
            return new ViewHolder(DashboardCategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
}
