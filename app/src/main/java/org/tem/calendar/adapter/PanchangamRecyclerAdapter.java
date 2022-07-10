package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.databinding.PanchangamItemBinding;
import org.tem.calendar.model.PanchangamData;

import java.util.List;

public class PanchangamRecyclerAdapter extends RecyclerView.Adapter<PanchangamRecyclerAdapter.ViewHolder> {


    private final List<PanchangamData> dataList;

    public PanchangamRecyclerAdapter(List<PanchangamData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PanchangamItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.panchangam_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PanchangamData gd = dataList.get(position);
        holder.binding.labelTxt.setText(gd.getTimeIndexText());
        holder.binding.morningValueTxt.setText(gd.getMorningText());
        holder.binding.eveningValueTxt.setText(gd.getEveningText());
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final PanchangamItemBinding binding;

        public ViewHolder(PanchangamItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
