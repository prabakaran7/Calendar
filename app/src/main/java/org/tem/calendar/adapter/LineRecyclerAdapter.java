package org.tem.calendar.adapter;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.tem.calendar.R;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.model.LineDataModel;

import java.util.Arrays;
import java.util.List;

public class LineRecyclerAdapter extends RecyclerView.Adapter<LineRecyclerAdapter.ViewHolder> {

    private final List<LineDataModel> dataSet;

    private static final List<String> headerStyle = Arrays.asList("bold", "italic", "bold_italic");
    private float textPixelSize = 0;

    public LineRecyclerAdapter(List<LineDataModel> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_line_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (textPixelSize == 0) {
            textPixelSize = holder.view.getTextSize();
        }
        LineDataModel ldm = dataSet.get(position);
        holder.view.setText(ldm.getText());
        holder.view.setTextAlignment("right".equalsIgnoreCase(ldm.getAlignment())
                ? View.TEXT_ALIGNMENT_TEXT_END
                : "center".equalsIgnoreCase(ldm.getAlignment())
                ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_TEXT_START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if ("justify".equalsIgnoreCase(ldm.getAlignment())) {
                holder.view.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            } else {
                holder.view.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_NONE);
            }
        }
        holder.view.setTypeface(Typeface.defaultFromStyle(
                "bold".equalsIgnoreCase(ldm.getFontStyle())
                        ? Typeface.BOLD
                        : "bold_italic".equalsIgnoreCase(ldm.getFontStyle())
                        ? Typeface.BOLD_ITALIC
                        : "italic".equalsIgnoreCase(ldm.getFontStyle())
                        ? Typeface.ITALIC
                        : Typeface.NORMAL));
        TypedValue value = new TypedValue();
        Resources.Theme theme = holder.itemView.getContext().getTheme();
        if (headerStyle.contains(ldm.getFontStyle())) {
            theme.resolveAttribute(R.attr.bgSubTitle, value, true);
        } else {
            theme.resolveAttribute(R.attr.txt_color, value, true);
        }

        holder.view.setTextColor(value.data);

        holder.view.setTextSize(TypedValue.COMPLEX_UNIT_PX, (StringUtils.isNotBlank(ldm.getWeight()) ? Float.parseFloat(ldm.getWeight()) : 1.0f) * textPixelSize);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.text);
        }
    }
}
