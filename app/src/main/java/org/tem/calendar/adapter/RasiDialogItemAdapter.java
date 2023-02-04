package org.tem.calendar.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.custom.RasiItemOnClickListener;
import org.tem.calendar.databinding.RasiDialogItemBinding;

import java.util.List;
import java.util.Random;

public class RasiDialogItemAdapter extends RecyclerView.Adapter<RasiDialogItemAdapter.ViewHolder> {

    private String[] rasiNames;
    private final RasiItemOnClickListener listener;

    private final List<Integer> dataSet;

    public RasiDialogItemAdapter(RasiItemOnClickListener listener, List<Integer> dataSet, String[] rasiNames) {
        this.listener = listener;
        this.dataSet = dataSet;
        this.rasiNames = rasiNames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RasiDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.text.setText(rasiNames[position]);
        holder.binding.image.setImageResource(dataSet.get(position));
        holder.binding.image.setBackgroundTintList(ColorStateList.valueOf(getRandomColor()));
        holder.itemView.setOnClickListener(view -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RasiDialogItemBinding binding;


        public ViewHolder(@NonNull RasiDialogItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
