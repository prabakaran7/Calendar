package org.tem.calendar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.datatransport.BuildConfig;
import com.google.android.gms.ads.AdRequest;

import org.tem.calendar.R;
import org.tem.calendar.adapter.DashboardCategoryRecyclerViewAdapter;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.databinding.ActivityDashboardBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.Dashboard;
import org.tem.calendar.model.FestivalDayData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.NallaNeramData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        binding.adView.loadAd(new AdRequest.Builder().build());

        runOnUiThread(this::loadCurrentDay);

        runOnUiThread(() -> {
            binding.categoryRecyclerView.setAdapter(new DashboardCategoryRecyclerViewAdapter(DashboardActivity.this));
            binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
            binding.categoryRecyclerView.setHasFixedSize(false);
        });

        binding.headerLayout.setOnClickListener(
                view -> {
                    startActivity(new Intent(DashboardActivity.this, DayActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
        );



    }

    public void onClick(@NonNull Dashboard dashboard) {
        if (null != dashboard.getActivityClass()) {
            Intent intent = new Intent(DashboardActivity.this, dashboard.getActivityClass());
            if (null != dashboard.getExtras() && !dashboard.getExtras().isEmpty()) {
                intent.putExtras(dashboard.getExtras());
            }

            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            if (dashboard.getImageResourceId() == R.drawable.temtech) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Tem+Tech")));
            } else if (dashboard.getImageResourceId() == android.R.drawable.ic_menu_share) {

                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name_long));
                    String shareMessage = "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + this.getPackageName() + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
            } else if (dashboard.getImageResourceId() == android.R.drawable.ic_lock_lock) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://text2tem.github.io/")));
            }
        }
    }

    private void loadCurrentDay() {
        MonthData md = DBHelper.getInstance(this).getDate(LocalDate.now());

        if (md.getTyear() > 0) {
            binding.tamilYearTxt.setText(getResources().getStringArray(R.array.tamil_year_names)[md.getTyear() - 1]);
            binding.tamilDateTxt.setText(String.format(Locale.getDefault(), "%d", md.getTday()));
            binding.tamilMonthTxt.setText(getResources().getStringArray(R.array.tamizh_month_names)[md.getTmonth() - 1]);
            NallaNeramData nnd = DBHelper.getInstance(this).getNallaNeram(md.getDate());
            if (null != nnd) {
                binding.nallaNeramMorning.setText(nnd.getNallaNeramM());
                binding.nallaNeramEvening.setText(nnd.getNallaNeramE());
            }
        }

        binding.monthDayTxt.setText(String.format("%s-%s", getResources().getStringArray(R.array.en_month_names)[md.getMonth() - 1], getResources().getStringArray(R.array.weekday_names)[md.getWeekday() - 1]));
        binding.dateTxt.setText(md.getDate());
        LocalDate today = LocalDate.parse(md.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Typeface font;
        if(today.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            font = ResourcesCompat.getFont(this, R.font.tourney_semi_bold);

        } else {
            font = ResourcesCompat.getFont(this, R.font.tourney_black);
        }
        binding.dateTxt.setTypeface(font);

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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}