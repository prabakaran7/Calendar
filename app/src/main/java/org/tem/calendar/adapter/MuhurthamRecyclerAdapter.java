package org.tem.calendar.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.databinding.MuhurthamItemBinding;
import org.tem.calendar.model.MuhurthamData;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MuhurthamRecyclerAdapter extends RecyclerView.Adapter<MuhurthamRecyclerAdapter.ViewHolder> {

    private final List<MuhurthamData> dataSet;
    private final Context mContext;
    private final String[] dayNames;
    private final String[] tamilMonths;
    private final String[] thitiNames;
    private final String[] starNames;
    private final String[] yogamNames;
    private final String[] laknamNames;
    private int previousExpandedPosition = -1;
    private int mExpandedPosition = -1;

    public MuhurthamRecyclerAdapter(Context context, List<MuhurthamData> dataSet) {
        this.mContext = context;
        this.dataSet = dataSet;
        dayNames = mContext.getResources().getStringArray(R.array.weekday_names);
        this.tamilMonths = mContext.getResources().getStringArray(R.array.tamizh_month_names);
        this.thitiNames = mContext.getResources().getStringArray(R.array.thiti_names);
        this.starNames = mContext.getResources().getStringArray(R.array.star_names);
        this.yogamNames = mContext.getResources().getStringArray(R.array.yogam_names);
        this.laknamNames = mContext.getResources().getStringArray(R.array.rasi_names);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MuhurthamItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.muhurtham_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        MuhurthamData data = dataSet.get(pos);
        holder.binding.dateTxt.setText(data.getDate().format(DateTimeFormatter.ofPattern("d-M-yyyy")));
        holder.binding.dayTxt.setText(dayNames[data.getDate().getDayOfWeek().getValue() - 1]);
        holder.binding.tamilDayTxt.setText(tamilMonths[data.getTmonth() - 1] + ", " + data.getTday());
        holder.binding.piraiImage.setImageResource(data.isValarpirai() ? R.drawable.cresent_white : R.drawable.cresent_black);
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

        MuhurthamItemBinding binding;

        public ViewHolder(MuhurthamItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
