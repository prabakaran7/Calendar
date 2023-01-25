package org.tem.calendar.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.tem.calendar.R;
import org.tem.calendar.databinding.MuhurthamItemBinding;
import org.tem.calendar.model.MuhurthamData;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MuhurthamRecyclerAdapter extends RecyclerView.Adapter<MuhurthamRecyclerAdapter.ViewHolder> {

    private final List<MuhurthamData> dataSet;
    private final String[] dayNames;
    private final String[] tamilMonths;
    private final String[] thitiNames;
    private final String[] starNames;
    private final String[] yogamNames;
    private final String[] laknamNames;
    private int previousExpandedPosition = -1;
    private int mExpandedPosition = -1;

    public MuhurthamRecyclerAdapter(Context context, List<MuhurthamData> dataSet) {
        this.dataSet = dataSet;
        dayNames = context.getResources().getStringArray(R.array.weekday_names);
        this.tamilMonths = context.getResources().getStringArray(R.array.tamizh_month_names);
        this.thitiNames = context.getResources().getStringArray(R.array.thiti_names);
        this.starNames = context.getResources().getStringArray(R.array.star_names);
        this.yogamNames = context.getResources().getStringArray(R.array.yogam_names);
        this.laknamNames = context.getResources().getStringArray(R.array.rasi_names);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.of(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        MuhurthamData data = dataSet.get(pos);
        holder.binding.dateTxt.setText(data.getDate().format(DateTimeFormatter.ofPattern("d-M-yyyy")));
        holder.binding.dayTxt.setText(dayNames[data.getDate().getDayOfWeek().getValue() - 1]);
        holder.binding.tamilDayTxt.setText(String.format(Locale.getDefault(), "%s, %d", tamilMonths[data.getTmonth() - 1], data.getTday()));
        Glide.with(holder.itemView.getContext())
                .load(data.isValarPirai() ? R.drawable.cresent_white : R.drawable.cresent_black)
                .into(holder.binding.piraiImage);
        final boolean isExpanded = pos==mExpandedPosition;
        // Add animation
        holder.binding.detailsView.setVisibility(isExpanded ? View.VISIBLE:View.GONE);
        ValueAnimator animator = isExpanded ? ValueAnimator.ofFloat(0, 90) : ValueAnimator.ofFloat(90, 0);
        animator.setDuration(400);
        animator.addUpdateListener(animation -> holder.binding.expanderImage.setRotation((Float) animation.getAnimatedValue()));
        animator.start();

        //details info
        holder.binding.thitiTxt.setText(thitiNames[data.getThiti() - 1]);
        holder.binding.starTxt.setText(starNames[data.getStar() - 1]);
        holder.binding.timeTxt.setText(data.getTime());
        holder.binding.yogamTxt.setText(yogamNames[data.getYogam() - 1]);
        holder.binding.laknamTxt.setText(laknamNames[data.getLaknam() - 1]);

        if(isExpanded){
            previousExpandedPosition = pos;
        }
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(v -> {
            mExpandedPosition = isExpanded ? -1 : pos;
            notifyItemChanged(previousExpandedPosition);
            notifyItemChanged(pos);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final MuhurthamItemBinding binding;

        public ViewHolder(@NonNull MuhurthamItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public static ViewHolder of(@NonNull ViewGroup parent) {
            return new ViewHolder(
                    MuhurthamItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
            );
        }
    }
}
