package org.tem.calendar.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.databinding.StarMatchingItemBinding;

import java.util.List;
import java.util.Locale;

public class StarMatchingRecyclerAdapter extends RecyclerView.Adapter<StarMatchingRecyclerAdapter.ViewHolder> {

    private final List<Object[]> dataSet;

    public StarMatchingRecyclerAdapter(List<Object[]> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object[] data = dataSet.get(position);
        holder.binding.text1.setText(String.format(Locale.getDefault(), "%d. %s", position + 1, data[0]));
        boolean notMatched = Integer.parseInt(data[1].toString()) == 0;
        holder.binding.text2.setText(holder.itemView.getContext().getString(notMatched ? R.string.not_matching : R.string.matching));
        holder.binding.image1.setImageResource(notMatched ? R.drawable.cross : R.drawable.tick);
        holder.binding.expTxt.setText(data[2].toString());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final StarMatchingItemBinding binding;

        public ViewHolder(@NonNull StarMatchingItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(view -> {
                if (null == view.getTag() || !Boolean.parseBoolean(view.getTag().toString())) {
                    binding.expTxt.animate()
                            .translationY(binding.expTxt.getHeight())
                            .alpha(1.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    binding.expTxt.setVisibility(View.VISIBLE);
                                }
                            });
                    view.setTag(true);
                } else {
                    binding.expTxt.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    binding.expTxt.setVisibility(View.GONE);
                                }
                            });
                    view.setTag(false);
                }
            });

        }

        public static ViewHolder of(ViewGroup parent) {
            return new ViewHolder(StarMatchingItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
}
