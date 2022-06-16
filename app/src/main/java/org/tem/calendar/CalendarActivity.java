package org.tem.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.tem.calendar.activities.DailyActivity;
import org.tem.calendar.activities.ManaiyadiSastharamActivity;
import org.tem.calendar.activities.MonthActivity;
import org.tem.calendar.activities.MuhurthamActivity;
import org.tem.calendar.activities.PanchangamActivity;
import org.tem.calendar.activities.RaghuEmaKuligaiActivity;
import org.tem.calendar.activities.VasthuActivity;
import org.tem.calendar.databinding.ActivityCalendarBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.PanchangamFragment;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.NallaNeramData;

import java.time.LocalDate;

public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        loadCurrentDay();

        binding.dailyCalendarCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, DailyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.monthlyCalendarCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, MonthActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        binding.muhurthamCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, MuhurthamActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.raghuCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, RaghuEmaKuligaiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.gowriCard.setOnClickListener(view ->{
            Intent intent = new Intent(CalendarActivity.this, PanchangamActivity.class);
            intent.putExtra(Constants.EXTRA_PANCHANGAM, PanchangamFragment.GOWRI_PANCHANGAM);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.horaiCard.setOnClickListener(view ->{
            Intent intent = new Intent(CalendarActivity.this, PanchangamActivity.class);
            intent.putExtra(Constants.EXTRA_PANCHANGAM, PanchangamFragment.GRAHA_ORAI_PANCHANGAM);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.vasthuCard.setOnClickListener(view->{
            Intent intent = new Intent(CalendarActivity.this, VasthuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.manaiyadiCard.setOnClickListener(view->{
            Intent intent = new Intent(CalendarActivity.this, ManaiyadiSastharamActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void loadCurrentDay() {
        MonthData md = DBHelper.getInstance(this).getDate(LocalDate.now());

        if (md.getTyear() > 0) {
            binding.tamilYearTxt.setText(getResources().getStringArray(R.array.tamil_year_names)[md.getTyear() - 1]);
            binding.tamilDateTxt.setText(md.getTday() + "");
            binding.tamilMonthTxt.setText(getResources().getStringArray(R.array.tamizh_month_names)[md.getTmonth() - 1]);
            NallaNeramData nnd = DBHelper.getInstance(this).getNallaNeram(md.getDate());
            if (null != nnd) {
                binding.nallaNeramMorning.setText(nnd.getNallaNeramM());
                binding.nallaNeramEvening.setText(nnd.getNallaNeramE());
            }
        }

        binding.monthDayTxt.setText(getResources().getStringArray(R.array.en_month_names)[md.getMonth() - 1] + "-" +
                getResources().getStringArray(R.array.weekday_names)[md.getWeekday() - 1]);
        binding.dateTxt.setText(md.getDate());

    }
}