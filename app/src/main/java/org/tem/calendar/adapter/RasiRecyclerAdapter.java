package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.databinding.RasiLayoutItemBinding;
import org.tem.calendar.util.RasiViewData;

import java.util.List;

public class RasiRecyclerAdapter extends RecyclerView.Adapter<RasiRecyclerAdapter.ViewHolder> {

    private final List<RasiViewData> dataSet;

    public RasiRecyclerAdapter(List<RasiViewData> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RasiViewData rvd = dataSet.get(position);
        holder.binding.rasiImage.setImageResource(rvd.getImageResourceId());
        holder.binding.rasiLabel.setText(rvd.getLabel());
        holder.binding.rasiTxt.setText(rvd.getValue());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        RasiLayoutItemBinding binding;

        public ViewHolder(@NonNull RasiLayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static ViewHolder from(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            RasiLayoutItemBinding binding = RasiLayoutItemBinding.inflate(inflater, parent, false);
            return new ViewHolder(binding);
        }
    }
}
