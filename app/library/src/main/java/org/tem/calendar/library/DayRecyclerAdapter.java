package org.tem.calendar.library;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder> {

    private final List<DateModel> dataSet;
    private final List<Integer> weekEnds;
    private final CalendarDayOnClickListener onClickListener;
    private final Context mContext;
    private final SwipeInterface swipeInterface;

    public DayRecyclerAdapter(Context mContext, List<DateModel> dates, SwipeInterface swipeInterface) {
        this(mContext, dates, Arrays.asList(1, 6), swipeInterface);
    }

    public DayRecyclerAdapter(Context mContext, List<DateModel> dates, List<Integer> weekEnds, SwipeInterface swipeInterface) {
        this(mContext, dates, weekEnds, null,swipeInterface);
    }

    public DayRecyclerAdapter(Context mContext, List<DateModel> dates, List<Integer> weekEnds, CalendarDayOnClickListener onClickListener, SwipeInterface swipeInterface) {
        this.mContext = mContext;
        this.dataSet = dates;
        this.weekEnds = weekEnds;
        this.onClickListener = onClickListener;
        this.swipeInterface = swipeInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.day_in_month_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateModel model = dataSet.get(position);
        holder.itemView.setOnTouchListener(new ActivitySwipe2Detector(swipeInterface));
        if (model == null) {
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
            holder.itemView.setFocusable(false);
            holder.primeTxt.setText("");
            holder.secondaryTxt.setText("");
            holder.itemView.setEnabled(false);
            holder.itemView.setClickable(false);
        } else {
            holder.primeTxt.setText(model.getPrimeDay() + "");
            holder.secondaryTxt.setText(0 != model.getSecondaryDay() ? model.getSecondaryDay() + "" : "");


            holder.itemView.setOnClickListener(v -> {
                if (null != onClickListener) {
                    onClickListener.onClick(model.getDate());
                }
            });
        }

        if (weekEnds.contains((position % 7) + 1)) {
            if(null != model && model.isToday()){
                holder.itemView.setBackgroundResource(R.drawable.day_today_holiday_bg);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.day_holiday_bg);
            }
        } else {
            if(null != model && model.isToday()){
                holder.itemView.setBackgroundResource(R.drawable.day_today_bg);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.day_bg);
            }
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView primeTxt;
        AppCompatTextView secondaryTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            primeTxt = itemView.findViewById(R.id.primeDayTxt);
            secondaryTxt = itemView.findViewById(R.id.secondaryDayTxt);
        }
    }
}
