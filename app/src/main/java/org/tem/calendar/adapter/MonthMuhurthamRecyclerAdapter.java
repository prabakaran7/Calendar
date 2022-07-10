package org.tem.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.databinding.MonthlyMuhurthamItemBinding;
import org.tem.calendar.model.MuhurthamData;

import java.util.List;

public class MonthMuhurthamRecyclerAdapter extends RecyclerView.Adapter<MonthMuhurthamRecyclerAdapter.ViewHolder> {

    private final List<MuhurthamData> dataSet;
    private final Context mContext;
    private final String[] dayNames;
    private final String[] tamilMonths;

    public MonthMuhurthamRecyclerAdapter(Context mContext, List<MuhurthamData> dataSet) {
        this.mContext = mContext;
        this.dataSet = dataSet;
        dayNames = mContext.getResources().getStringArray(R.array.weekday_names);
        tamilMonths = mContext.getResources().getStringArray(R.array.tamizh_month_names);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.monthly_muhurtham_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MuhurthamData md = dataSet.get(position);
        if (null != md) {
            holder.binding.piraiImage.setImageResource(md.isValarPirai() ? R.drawable.cresent_white : R.drawable.cresent_black);
            holder.binding.muhurthamTxt.setText(
                    mContext.getString(R.string.muhurtham_msg,
                            md.getDate().getDayOfMonth(),
                            dayNames[md.getDate().getDayOfWeek().getValue() - 1],
                            tamilMonths[md.getTmonth() - 1],
                            md.getTday()
                    )
            );

            // date day - tmonth tday
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        final MonthlyMuhurthamItemBinding binding;

        public ViewHolder(MonthlyMuhurthamItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
