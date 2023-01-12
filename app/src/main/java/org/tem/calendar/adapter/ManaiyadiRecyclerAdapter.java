package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.databinding.ManaiyadiItemBinding;

import java.util.List;
import java.util.Locale;


public class ManaiyadiRecyclerAdapter extends RecyclerView.Adapter<ManaiyadiRecyclerAdapter.ViewHolder> {

    private final List<Pair<Integer, String>> dataSet;

    public ManaiyadiRecyclerAdapter(List<Pair<Integer, String>> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<Integer, String> data = dataSet.get(position);
        holder.binding.adiTxt.setText(String.format(Locale.getDefault(), "%d", data.first));
        holder.binding.palanTxt.setText(data.second);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ManaiyadiItemBinding binding;

        public ViewHolder(@NonNull ManaiyadiItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public static ViewHolder of(@NonNull ViewGroup parent) {
            return new ViewHolder(
                    ManaiyadiItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
            );
        }
    }
}
