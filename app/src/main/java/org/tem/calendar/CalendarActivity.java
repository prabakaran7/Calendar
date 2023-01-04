package org.tem.calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.tem.calendar.activities.FestivalIndexActivity;
import org.tem.calendar.activities.ManaiyadiSastharamActivity;
import org.tem.calendar.activities.MonthActivity;
import org.tem.calendar.activities.MonthVirathamActivity;
import org.tem.calendar.activities.MuhurthamActivity;
import org.tem.calendar.activities.PanchangamActivity;
import org.tem.calendar.activities.RaghuEmaKuligaiActivity;
import org.tem.calendar.activities.DayActivity;
import org.tem.calendar.activities.VasthuActivity;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.databinding.ActivityCalendarBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.fragment.MonthVirathamFragment;
import org.tem.calendar.fragment.PanchangamFragment;
import org.tem.calendar.model.FestivalDayData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.NallaNeramData;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar);
        loadCurrentDay();

        binding.dailyCalendarCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, DayActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.monthlyCalendarCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, MonthActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.virathamCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, MonthVirathamActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        binding.muhurthamCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, MuhurthamActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.festivalCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, FestivalIndexActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.raghuCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, RaghuEmaKuligaiActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.asubaCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, MonthVirathamActivity.class);
            intent.putExtra(Constants.EXTRA_TYPE, MonthVirathamFragment.ASUBA_VIRATHAM);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.gowriCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, PanchangamActivity.class);
            intent.putExtra(Constants.EXTRA_PANCHANGAM, PanchangamFragment.GOWRI_PANCHANGAM);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.horaiCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, PanchangamActivity.class);
            intent.putExtra(Constants.EXTRA_PANCHANGAM, PanchangamFragment.GRAHA_ORAI_PANCHANGAM);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.vasthuCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, VasthuActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        binding.manaiyadiCard.setOnClickListener(view -> {
            Intent intent = new Intent(CalendarActivity.this, ManaiyadiSastharamActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });


        // other apps
        binding.otherAppCard.setOnClickListener(view-> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Tem+Tech"))));

        binding.privacyCard.setOnClickListener(view ->{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://text2tem.github.io/")));
        });

        binding.shareCard.setOnClickListener(view->{
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name_long));
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
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

        FestivalDayData fdd = DBHelper.getInstance(this).getFestivalDays(DateUtil.format(LocalDate.now()));
        if (fdd != null) {

            Set<String> festivals = new HashSet<>();
            if (!StringUtils.isBlank(fdd.getGovt()) && fdd.getGovt().trim().length() > 1) {
                festivals.addAll(Arrays.asList(fdd.getGovt().split("[ ]*[,][ ]*")));
            }

            if (!StringUtils.isBlank(fdd.getHindhu()) && fdd.getHindhu().trim().length() > 1) {
                festivals.addAll(Arrays.asList(fdd.getHindhu().split("[ ]*[,][ ]*")));
            }

            if (!StringUtils.isBlank(fdd.getChrist()) && fdd.getChrist().trim().length() > 1) {
                festivals.addAll(Arrays.asList(fdd.getChrist().split("[ ]*[,][ ]*")));
            }

            if (!StringUtils.isBlank(fdd.getMuslims()) && fdd.getMuslims().trim().length() > 1) {
                festivals.addAll(Arrays.asList(fdd.getMuslims().split("[ ]*[,][ ]*")));
            }

            if (!StringUtils.isBlank(fdd.getImportant()) && fdd.getImportant().trim().length() > 1) {
                festivals.addAll(Arrays.asList(fdd.getImportant().split("[ ]*[,][ ]*")));
            }

            if (!festivals.isEmpty()) {
                binding.importantDayTxt.setVisibility(View.VISIBLE);
                binding.importantDayTxt.setText(StringUtils.join(Arrays.asList(festivals.toArray()), ","));
            } else {
                binding.importantDayTxt.setVisibility(View.GONE);
            }

        } else {
            binding.importantDayTxt.setVisibility(View.GONE);
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}