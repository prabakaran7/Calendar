package org.tem.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.databinding.ActivityDailyBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.library.ActivitySwipeDetector;
import org.tem.calendar.library.DateUtil;
import org.tem.calendar.library.SwipeInterface;
import org.tem.calendar.model.KaranamData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.NallaNeramData;
import org.tem.calendar.model.RasiChartData;
import org.tem.calendar.model.RasiData;
import org.tem.calendar.model.StarData;
import org.tem.calendar.model.ThitiData;
import org.tem.calendar.model.WeekData;
import org.tem.calendar.model.YogamData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyActivity extends AppCompatActivity implements SwipeInterface {

    private ActivityDailyBinding binding;

    private LocalDate selectedDate;
    private String dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_daily);

        Intent intent = getIntent();

        if (intent == null || intent.getExtras() == null || !intent.getExtras().containsKey(Constants.EXTRA_DATE_SELECTED)) {
            selectedDate = LocalDate.now();
        } else {
            selectedDate = (LocalDate) intent.getSerializableExtra(Constants.EXTRA_DATE_SELECTED);
        }
        dateString = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        binding.headerLayout.dateTxt.setText(selectedDate.format(DateTimeFormatter.ofPattern("d-M-yyyy")));
        binding.headerLayout.monthHeaderTxt.setText(monthText());

        MonthData md = DBHelper.getInstance(this).getDate(selectedDate);
        if (null != md) {
            binding.headerLayout.secCalTxt.setText(getResources().getStringArray(R.array.tamil_year_names)[md.getTyear() - 1] + " , " +
                    getResources().getStringArray(R.array.tamizh_month_names)[md.getTmonth() - 1] + " - " +
                    md.getTday());
        }

        binding.headerLayout.prevBtn.setOnClickListener(v -> moveToPrevDay());
        binding.headerLayout.nextBtn.setOnClickListener(v -> moveToNextDay());
        binding.rootView.setOnTouchListener(new ActivitySwipeDetector(this, this));
        binding.scrollView.setOnTouchListener(new ActivitySwipeDetector(this, this));


        // raaghu & soolam
        WeekData wd = DBHelper.getInstance(this).getWeekData(selectedDate.getDayOfWeek());
        binding.raaghuEmaLayout.raaghuTxt.setText(wd.getRaaghu());
        binding.raaghuEmaLayout.emaTxt.setText(wd.getEma());
        binding.raaghuEmaLayout.kuligaiTxt.setText(wd.getKuligai());
        binding.raaghuEmaLayout.karananTxt.setText(wd.getKaranan());
        binding.vaaraSoolaiLayout.soolamTxt.setText(getResources().getStringArray(R.array.directions)[wd.getSoolam() - 1]);
        binding.vaaraSoolaiLayout.parigaramTxt.setText(getResources().getStringArray(R.array.parigaram)[wd.getParigaram() - 1]);
        int naa = wd.getSoolamTime();
        int[] hourMin = DateUtil.naazhigaiToHourMin(naa);
        binding.vaaraSoolaiLayout.soolamTimeTxt.setText(getString(R.string.nazhigaiTime, naa, hourMin[0], hourMin[1]));

        //NallaNeram
        NallaNeramData nd = DBHelper.getInstance(this).getNallaNeram(dateString);
        binding.nallaLayout.nallaNeramMorning.setText(nd.getNallaNeramM());
        binding.nallaLayout.nallaNeramEvening.setText(nd.getNallaNeramE());
        binding.nallaLayout.gowriMorning.setText(nd.getGowriM());
        binding.nallaLayout.gowriEvening.setText(nd.getGowriE());
        binding.sunriseLayout.sunriseTxt.setText(nd.getSunRise());
        binding.sunriseLayout.sunriseLaknamTxt.setText(getSunRiseLaknamText(nd.getLaknam(), nd.getLaknamTime()));

        // panchangam

        //vaaram
        binding.panchangamLayout.weekdayTxt.setText(getResources().getStringArray(R.array.weekday_long_names)[selectedDate.getDayOfWeek().getValue() - 1]);

        //thiti
        ThitiData td = DBHelper.getInstance(this).getThiti(dateString);
        String thiti = "-";
        if (null != td) {
            String[] thitiNames = getResources().getStringArray(R.array.thiti_names);
            if (td.getTime1().length() < 2) {
                thiti = getString(R.string.fullday_pangachangam, thitiNames[td.getThiti1()]);
            } else if (td.getTime2().length() < 2) {
                thiti = getString(R.string.two_panchangam, DateUtil.expandedTime(td.getTime1()), thitiNames[td.getThiti1()], thitiNames[td.getThiti2()]);
            } else {
                thiti = getString(R.string.three_panchangam, DateUtil.expandedTime(td.getTime1()), DateUtil.expandedTime(td.getTime2()), thitiNames[td.getThiti1()], thitiNames[td.getThiti2()], thitiNames[td.getThiti3()]);
            }
            if (td.getPirai() == -1) { //Amavasai
                binding.importantDayLayout.piraiImage.setImageResource(R.drawable.new_moon);
                binding.importantDayLayout.piraiImage.setOnClickListener(v -> Toast.makeText(this, R.string.today_new_moon, Toast.LENGTH_SHORT).show());
            } else if (td.getPirai() == -2) { //pournami
                binding.importantDayLayout.piraiImage.setImageResource(R.drawable.full_moon);
                binding.importantDayLayout.piraiImage.setOnClickListener(v -> Toast.makeText(this, R.string.today_full_moon, Toast.LENGTH_SHORT).show());
            } else if (td.getPirai() == 2) { //valarpirai
                binding.importantDayLayout.piraiImage.setImageResource(R.drawable.cresent_white);
                binding.importantDayLayout.piraiImage.setOnClickListener(v -> Toast.makeText(this, R.string.today_waxing_moon, Toast.LENGTH_SHORT).show());
            } else { //theipirai
                binding.importantDayLayout.piraiImage.setImageResource(R.drawable.cresent_black);
                binding.importantDayLayout.piraiImage.setOnClickListener(v -> Toast.makeText(this, R.string.today_waning_moon, Toast.LENGTH_SHORT).show());
            }
        }
        binding.panchangamLayout.thitiTxt.setText(thiti);

        //Stars
        StarData sd = DBHelper.getInstance(this).getStar(dateString);
        String star = "-";
        if (null != sd) {
            String[] starNames = getResources().getStringArray(R.array.star_names);
            if (sd.getTime1().length() < 2) {
                star = getString(R.string.fullday_pangachangam, starNames[sd.getStar1() - 1]);
            } else if (sd.getTime2().length() < 2) {
                star = getString(R.string.two_panchangam, DateUtil.expandedTime(sd.getTime1()), starNames[sd.getStar1() - 1], starNames[sd.getStar2() - 1]);
            } else {
                star = getString(R.string.three_panchangam, DateUtil.expandedTime(sd.getTime1()), DateUtil.expandedTime(sd.getTime2()),
                        starNames[sd.getStar1() - 1], starNames[sd.getStar2() - 1], starNames[sd.getStar3() - 1]);
            }
            String[] chandArry = sd.getChandrastamam().split("[ ]*[,][ ]*");
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < chandArry.length; index++) {
                sb.append(starNames[Integer.parseInt(chandArry[index]) - 1]);
                if (index != chandArry.length - 1) {
                    sb.append(",");
                }
            }
            binding.chandrastamLayout.chandrastamamTxt.setText(sb.toString());
            if (sd.getNokku() == 1) {
                binding.importantDayLayout.nokkuImage.setImageResource(R.drawable.up_arrow);
                binding.importantDayLayout.nokkuImage.setOnClickListener(v->Toast.makeText(this, R.string.mel_nokku_naal, Toast.LENGTH_SHORT).show());
            } else if (sd.getNokku() == 2) {
                binding.importantDayLayout.nokkuImage.setImageResource(R.drawable.down_arrow);
                binding.importantDayLayout.nokkuImage.setOnClickListener(v->Toast.makeText(this, R.string.kizh_nokku_naal, Toast.LENGTH_SHORT).show());
            } else {
                binding.importantDayLayout.nokkuImage.setImageResource(R.drawable.both_side_arrow);
                binding.importantDayLayout.nokkuImage.setOnClickListener(v->Toast.makeText(this, R.string.sama_nokku_naal, Toast.LENGTH_SHORT).show());
            }
        }
        binding.panchangamLayout.starTxt.setText(star);

        KaranamData kd = DBHelper.getInstance(this).getKaranam(dateString);
        String kara = "-";
        if (null != kd) {
            String[] karaNames = getResources().getStringArray(R.array.karanam_names);
            if (kd.getTime1().length() < 2) {
                kara = getString(R.string.fullday_pangachangam, karaNames[kd.getKara1() - 1]);
            } else if (kd.getTime2().length() < 2) {
                kara = getString(R.string.two_panchangam, DateUtil.expandedTime(kd.getTime1()),
                        karaNames[kd.getKara1() - 1], karaNames[kd.getKara2() - 1]);
            } else if (kd.getTime3().length() < 2) {
                kara = getString(R.string.three_panchangam, DateUtil.expandedTime(kd.getTime1()),
                        DateUtil.expandedTime(kd.getTime2()), karaNames[kd.getKara1() - 1],
                        karaNames[kd.getKara2() - 1], karaNames[kd.getKara3() - 1]);
            } else {
                kara = getString(R.string.four_panchangam, DateUtil.expandedTime(kd.getTime1()),
                        DateUtil.expandedTime(kd.getTime2()), DateUtil.expandedTime(kd.getTime3()),
                        karaNames[kd.getKara1() - 1], karaNames[kd.getKara2() - 1],
                        karaNames[kd.getKara3() - 1], karaNames[kd.getKara4() - 1]);
            }
        }
        binding.panchangamLayout.karanamTxt.setText(kara);

        String yogam = "-";
        YogamData yd = DBHelper.getInstance(this).getYogam(dateString);
        if (null != yd) {
            String[] yogaNames = getResources().getStringArray(R.array.yogam_names);
            if (yd.getTime1().equals("-")) {
                yogam = getString(R.string.fullday_pangachangam, yogaNames[yd.getYogam1()]);
            } else if (yd.getTime2().equals("-")) {
                yogam = getString(R.string.two_panchangam, DateUtil.expandedTime(yd.getTime1()),
                        yogaNames[yd.getYogam1() - 1], yogaNames[yd.getYogam2() - 1]);
            } else {
                System.out.println(yd);
                yogam = getString(R.string.three_panchangam, DateUtil.expandedTime(yd.getTime1()),
                        DateUtil.expandedTime(yd.getTime2()), yogaNames[yd.getYogam1() - 1],
                        yogaNames[yd.getYogam2() - 1], yogaNames[yd.getYogam3() - 1]);
            }
        }
        binding.panchangamLayout.yogamTxt.setText(yogam);

        // load rasi
        RasiData rd = DBHelper.getInstance(this).getRasi(dateString);
        if (null != rd) {
            String[] values = getResources().getStringArray(R.array.rasi_values);
            binding.rasiLayout.getRoot().setVisibility(View.VISIBLE);
            binding.rasiLayout.meshamTxt.setText(values[rd.getMesham()]);
            binding.rasiLayout.rishabamTxt.setText(values[rd.getRishabam()]);
            binding.rasiLayout.mithunamTxt.setText(values[rd.getMithunam()]);
            binding.rasiLayout.kadagamTxt.setText(values[rd.getKadagam()]);
            binding.rasiLayout.simmamTxt.setText(values[rd.getSimmam()]);
            binding.rasiLayout.kanniTxt.setText(values[rd.getKanni()]);
            binding.rasiLayout.thulamTxt.setText(values[rd.getThulam()]);
            binding.rasiLayout.viruchagamTxt.setText(values[rd.getViruchagam()]);
            binding.rasiLayout.dhanusuTxt.setText(values[rd.getDhanusu()]);
            binding.rasiLayout.magaramTxt.setText(values[rd.getMagaram()]);
            binding.rasiLayout.kumbamTxt.setText(values[rd.getKumbam()]);
            binding.rasiLayout.meenamTxt.setText(values[rd.getMeenam()]);
        } else {
            binding.rasiLayout.getRoot().setVisibility(View.GONE);
        }

        RasiChartData rcd = DBHelper.getInstance(this).getRasiChart(dateString);
        if (null != rcd) {
            binding.rasiChartLayout.h1Txt.setText(rcd.getH1().equals("-") ? "" : rcd.getH1());
            binding.rasiChartLayout.h2Txt.setText(rcd.getH2().equals("-") ? "" : rcd.getH2());
            binding.rasiChartLayout.h3Txt.setText(rcd.getH3().equals("-") ? "" : rcd.getH3());
            binding.rasiChartLayout.h4Txt.setText(rcd.getH4().equals("-") ? "" : rcd.getH4());
            binding.rasiChartLayout.h5Txt.setText(rcd.getH5().equals("-") ? "" : rcd.getH5());
            binding.rasiChartLayout.h6Txt.setText(rcd.getH6().equals("-") ? "" : rcd.getH6());
            binding.rasiChartLayout.h7Txt.setText(rcd.getH7().equals("-") ? "" : rcd.getH7());
            binding.rasiChartLayout.h8Txt.setText(rcd.getH8().equals("-") ? "" : rcd.getH8());
            binding.rasiChartLayout.h9Txt.setText(rcd.getH9().equals("-") ? "" : rcd.getH9());
            binding.rasiChartLayout.h10Txt.setText(rcd.getH10().equals("-") ? "" : rcd.getH10());
            binding.rasiChartLayout.h11Txt.setText(rcd.getH11().equals("-") ? "" : rcd.getH11());
            binding.rasiChartLayout.h12Txt.setText(rcd.getH12().equals("-") ? "" : rcd.getH12());

            if (!rcd.getC1().equals("-")) {
                binding.rasiChartLayout.c1Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c1Txt.setText(rcd.getC1());
            } else {
                binding.rasiChartLayout.c1Txt.setVisibility(View.GONE);
            }
            if (!rcd.getC2().equals("-")) {
                binding.rasiChartLayout.c2Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c2Txt.setText(rcd.getC2());
            } else {
                binding.rasiChartLayout.c2Txt.setVisibility(View.GONE);
            }
            if (!rcd.getC3().equals("-")) {
                binding.rasiChartLayout.c3Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c3Txt.setText(rcd.getC3());
            } else {
                binding.rasiChartLayout.c3Txt.setVisibility(View.GONE);
            }
            if (!rcd.getC4().equals("-")) {
                binding.rasiChartLayout.c4Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c4Txt.setText(rcd.getC4());
            } else {
                binding.rasiChartLayout.c4Txt.setVisibility(View.GONE);
            }
            if (!rcd.getC5().equals("-")) {
                binding.rasiChartLayout.c5Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c5Txt.setText(rcd.getC5());
            } else {
                binding.rasiChartLayout.c5Txt.setVisibility(View.GONE);
            }
            if (!rcd.getC6().equals("-")) {
                binding.rasiChartLayout.c6Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c6Txt.setText(rcd.getC6());
            } else {
                binding.rasiChartLayout.c6Txt.setVisibility(View.GONE);
            }
            if (!rcd.getC7().equals("-")) {
                binding.rasiChartLayout.c7Txt.setVisibility(View.VISIBLE);
                binding.rasiChartLayout.c7Txt.setText(rcd.getC7());
            } else {
                binding.rasiChartLayout.c7Txt.setVisibility(View.GONE);
            }
        } else {
            binding.rasiChartLayout.getRoot().setVisibility(View.GONE);
        }
    }

    private String getSunRiseLaknamText(int laknam, String laknamTime) {
        String[] time = laknamTime.split("[.]");
        return getResources().getString(R.string.sunrise_laknam_msg, getResources().getStringArray(R.array.rasi_short_names)[laknam - 1], time[0], time[1]);
    }

    private String monthText() {
        String[] monthNames = getResources().getStringArray(R.array.en_month_names);
        String[] weekDayNames = getResources().getStringArray(R.array.weekday_names);

        return monthNames[selectedDate.getMonthValue() - 1] + " - " + weekDayNames[selectedDate.getDayOfWeek().getValue() - 1];
    }

    @Override
    public void bottom2top(View v) {

    }

    @Override
    public void left2right(View v) {
        moveToPrevDay();
    }

    private void moveToNextDay() {
        finish();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        Intent intent = getIntent();
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, selectedDate.plusDays(1));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }

    private void moveToPrevDay() {
        finish();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        Intent intent = getIntent();
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, selectedDate.minusDays(1));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void right2left(View v) {
        moveToNextDay();
    }

    @Override
    public void top2bottom(View v) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}