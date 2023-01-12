package org.tem.calendar.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.databinding.SimpleTextviewItem3Binding;

import java.util.List;

public class ThreeTextViewRecyclerAdapter extends RecyclerView.Adapter<ThreeTextViewRecyclerAdapter.ViewHolder> {

    private final List<String[]> dataSet;

    public ThreeTextViewRecyclerAdapter(List<String[]> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String[] data = dataSet.get(position);

            holder.binding.text1.setText(data[0]);
            holder.binding.text2.setText(data[1]);
            holder.binding.text3.setText(data[2]);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final SimpleTextviewItem3Binding binding;

        public ViewHolder(@NonNull SimpleTextviewItem3Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public static ViewHolder of(@NonNull ViewGroup parent) {
            return new ViewHolder(SimpleTextviewItem3Binding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
}
