package org.tem.calendar.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.databinding.DayInMonthLayoutBinding;

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
        this(mContext, dates, weekEnds, null, swipeInterface);
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
        DayInMonthLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.day_in_month_layout, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateModel model = dataSet.get(position);
        holder.itemView.setOnTouchListener(new ActivitySwipe2Detector(swipeInterface));
        if (model == null) {
            holder.itemView.setClickable(false);
            holder.itemView.setEnabled(false);
            holder.itemView.setFocusable(false);
            holder.binding.primeDayTxt.setText("");
            holder.binding.secondaryDayTxt.setText("");
            holder.itemView.setEnabled(false);
            holder.itemView.setClickable(false);
            return;
        } else {
            holder.binding.primeDayTxt.setText(model.getPrimeDay() + "");
            holder.binding.secondaryDayTxt.setText(0 != model.getSecondaryDay() ? model.getSecondaryDay() + "" : "");


            holder.itemView.setOnClickListener(v -> {
                if (null != onClickListener) {
                    onClickListener.onClick(model.getDate());
                }
            });
        }

        if (weekEnds.contains((position % 7) + 1) || model.isHoliday()) {
            if (model.isToday()) {
                holder.itemView.setBackgroundResource(R.drawable.day_today_holiday_bg);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.day_holiday_bg);
            }
        } else {
            if (model.isToday()) {
                holder.itemView.setBackgroundResource(R.drawable.day_today_bg);
            } else {
                holder.itemView.setBackgroundResource(R.drawable.day_bg);
            }
        }

        int count = 0;
        if (model.getTithi() != -1) {
            setImage(count, model.getTithi(), holder.binding);
            count++;
        }

        if (model.getMuhurtham() != -1) {
            setImage(count, model.getMuhurtham(), holder.binding);
            count++;
        }
        if (model.getStar() != -1) {
            setImage(count, model.getStar(), holder.binding);
            count++;
        }
        if (model.getSpecial() != -1) {
            setImage(count, model.getSpecial(), holder.binding);
        }


    }

    private void setImage(int count, int resourceId, DayInMonthLayoutBinding binding) {
        switch (count) {
            case 0:
                binding.leftBottomImage.setImageResource(resourceId);
                break;
            case 1:
                binding.leftTopImage.setImageResource(resourceId);
                break;
            default:
                binding.rightBottomImage.setImageResource(resourceId);
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        DayInMonthLayoutBinding binding;

        public ViewHolder(DayInMonthLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
