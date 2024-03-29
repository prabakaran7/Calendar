package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.tem.calendar.activities.DashboardActivity;
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
        if (dashboard.getImageResourceId() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(dashboard.getImageResourceId())
                    .into(holder.binding.itemImage);
        }
        holder.binding.itemTxt.setText(dashboard.getName());
        holder.binding.getRoot().setOnClickListener(view -> activity.onClick(dashboard));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final DashboardCategoryItemBinding binding;

        public ViewHolder(@NonNull DashboardCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public static ViewHolder of(@NonNull ViewGroup parent) {
            return new ViewHolder(DashboardCategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
}
