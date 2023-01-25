package org.tem.calendar.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.tem.calendar.CalendarApp;
import org.tem.calendar.R;
import org.tem.calendar.activities.DayActivity;
import org.tem.calendar.custom.DateUtil;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.databinding.DayItemBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.DayViewModel;
import org.tem.calendar.model.FestivalDayData;
import org.tem.calendar.model.KaranamData;
import org.tem.calendar.model.KuralData;
import org.tem.calendar.model.MonthData;
import org.tem.calendar.model.MuhurthamData;
import org.tem.calendar.model.NallaNeramData;
import org.tem.calendar.model.RasiChartData;
import org.tem.calendar.model.RasiData;
import org.tem.calendar.model.StarData;
import org.tem.calendar.model.ThitiData;
import org.tem.calendar.model.VirathamData;
import org.tem.calendar.model.WeekData;
import org.tem.calendar.model.YogamData;
import org.tem.calendar.util.RasiViewData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DayRecyclerAdapter extends RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder> {

    private static final LinearLayout.LayoutParams params;

    static {
        params = new LinearLayout.LayoutParams((int) (48 * CalendarApp.getDpFactor()), (int) (48 * CalendarApp.getDpFactor()));
        params.setMargins(10, 10, 10, 10);
    }

    private final DayViewModel model;
    private final DayActivity activity;

    public DayRecyclerAdapter(DayActivity activity, DayViewModel model) {
        this.model = model;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalDate selectedDate = model.getList().get(position);
        DayItemBinding binding = holder.binding;

        binding.importantDayLayout.imageLayout.removeAllViews();

        binding.headerLayout.nextBtn.setVisibility(selectedDate.isEqual(CalendarApp.MAX_DATE) ? View.INVISIBLE : View.VISIBLE);
        binding.headerLayout.prevBtn.setVisibility(selectedDate.isEqual(CalendarApp.MIN_DATE) ? View.INVISIBLE : View.VISIBLE);

        String dateString = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        binding.headerLayout.dateTxt.setText(selectedDate.format(DateTimeFormatter.ofPattern("d-M-yyyy")));
        if (selectedDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            binding.headerLayout.dateTxt.setTypeface(ResourcesCompat.getFont(activity, R.font.tourney_semi_bold), Typeface.NORMAL);
        } else {
            binding.headerLayout.dateTxt.setTypeface(ResourcesCompat.getFont(activity, R.font.tourney_black), Typeface.NORMAL);
        }

        binding.headerLayout.monthHeaderTxt.setText(monthText(selectedDate));

        MonthData md = DBHelper.getInstance(activity).getDate(selectedDate);
        if (null != md && md.getTday() > 0) {
            binding.headerLayout.secCalTxt.setText(
                    String.format(Locale.getDefault(),
                            "%s , %s - %d",
                            activity.getResources().getStringArray(R.array.tamil_year_names)[md.getTyear() - 1],
                            activity.getResources().getStringArray(R.array.tamizh_month_names)[md.getTmonth() - 1],
                            md.getTday()
                    )
            );
        }

        binding.headerLayout.prevBtn.setOnClickListener(v -> activity.moveBackward());
        binding.headerLayout.nextBtn.setOnClickListener(v -> activity.moveForward());

        raaghuSoolamData(binding, selectedDate);

        nallaNeramData(binding, dateString);

        binding.panchangamLayout.weekdayTxt.setText(activity.getResources().getStringArray(R.array.weekday_long_names)[selectedDate.getDayOfWeek().getValue() - 1]);

        //thiti
        ThitiData td = DBHelper.getInstance(activity).getThiti(dateString);
        String thiti = "-";
        if (null != td) {
            String[] thitiNames = activity.getResources().getStringArray(R.array.thiti_names);
            if (td.getTime1().length() < 2) {
                thiti = activity.getString(R.string.fullday_pangachangam, thitiNames[td.getThiti1()]);
            } else if (td.getTime2().length() < 2) {
                thiti = activity.getString(R.string.two_panchangam, DateUtil.expandedTime(td.getTime1()), thitiNames[td.getThiti1()], thitiNames[td.getThiti2()]);
            } else {
                thiti = activity.getString(R.string.three_panchangam, DateUtil.expandedTime(td.getTime1()), DateUtil.expandedTime(td.getTime2()), thitiNames[td.getThiti1()], thitiNames[td.getThiti2()], thitiNames[td.getThiti3()]);
            }

            ImageView iv = new ImageView(activity.getApplicationContext());
            iv.setRotation(150);
            iv.setMinimumHeight((int) (48 * CalendarApp.getDpFactor()));

            int resourceId;
            if (td.getPirai() == -1) { //Amavasai
                resourceId = R.drawable.new_moon;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.today_new_moon, Toast.LENGTH_SHORT).show());
            } else if (td.getPirai() == -2) { //pournami
                resourceId = R.drawable.full_moon;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.today_full_moon, Toast.LENGTH_SHORT).show());
            } else if (td.getPirai() == 2) { //valarpirai
                resourceId = R.drawable.cresent_white;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.today_waxing_moon, Toast.LENGTH_SHORT).show());
            } else { //theipirai
                resourceId = R.drawable.cresent_black;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.today_waning_moon, Toast.LENGTH_SHORT).show());
            }

            Glide.with(activity)
                    .load(resourceId)
                    .into(iv);

            binding.importantDayLayout.imageLayout.addView(iv, params);

        }
        binding.panchangamLayout.thitiTxt.setText(thiti);

        //Stars
        StarData sd = DBHelper.getInstance(activity).getStar(dateString);
        String star = "-";
        if (null != sd) {
            String[] starNames = activity.getResources().getStringArray(R.array.star_names);
            if (sd.getTime1().length() < 2) {
                star = activity.getString(R.string.fullday_pangachangam, starNames[sd.getStar1() - 1]);
            } else if (sd.getTime2().length() < 2) {
                star = activity.getString(R.string.two_panchangam, DateUtil.expandedTime(sd.getTime1()), starNames[sd.getStar1() - 1], starNames[sd.getStar2() - 1]);
            } else {
                star = activity.getString(R.string.three_panchangam, DateUtil.expandedTime(sd.getTime1()), DateUtil.expandedTime(sd.getTime2()),
                        starNames[sd.getStar1() - 1], starNames[sd.getStar2() - 1], starNames[sd.getStar3() - 1]);
            }
            String[] chandArr = sd.getChandrastamam().split("[ ]*[,][ ]*");
            StringBuilder sb = new StringBuilder();
            for (int index = 0; index < chandArr.length; index++) {
                sb.append(starNames[Integer.parseInt(chandArr[index]) - 1]);
                if (index != chandArr.length - 1) {
                    sb.append(",");
                }
            }

            binding.chandrastamLayout.chandrastamamTxt.setText(sb.toString());
            ImageView iv = new ImageView(activity.getApplicationContext());
            int resourceId;
            if (sd.getNokku() == 1) {
                resourceId = R.drawable.up_arrow;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.mel_nokku_naal, Toast.LENGTH_SHORT).show());
            } else if (sd.getNokku() == 2) {
                resourceId = R.drawable.down_arrow;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.kizh_nokku_naal, Toast.LENGTH_SHORT).show());
            } else {
                resourceId = R.drawable.both_side_arrow;
                iv.setOnClickListener(v -> Toast.makeText(activity, R.string.sama_nokku_naal, Toast.LENGTH_SHORT).show());
            }
            Glide.with(activity)
                    .load(resourceId)
                    .into(iv);
            binding.importantDayLayout.imageLayout.addView(iv, params);
        }
        binding.panchangamLayout.starTxt.setText(star);

        KaranamData kd = DBHelper.getInstance(activity).getKaranam(dateString);
        String kara = "-";
        if (null != kd) {
            String[] karaNames = activity.getResources().getStringArray(R.array.karanam_names);
            if (kd.getTime1().length() < 2) {
                kara = activity.getString(R.string.fullday_pangachangam, karaNames[kd.getKara1() - 1]);
            } else if (kd.getTime2().length() < 2) {
                kara = activity.getString(R.string.two_panchangam, DateUtil.expandedTime(kd.getTime1()),
                        karaNames[kd.getKara1() - 1], karaNames[kd.getKara2() - 1]);
            } else if (kd.getTime3().length() < 2) {
                kara = activity.getString(R.string.three_panchangam, DateUtil.expandedTime(kd.getTime1()),
                        DateUtil.expandedTime(kd.getTime2()), karaNames[kd.getKara1() - 1],
                        karaNames[kd.getKara2() - 1], karaNames[kd.getKara3() - 1]);
            } else {
                kara = activity.getString(R.string.four_panchangam, DateUtil.expandedTime(kd.getTime1()),
                        DateUtil.expandedTime(kd.getTime2()), DateUtil.expandedTime(kd.getTime3()),
                        karaNames[kd.getKara1() - 1], karaNames[kd.getKara2() - 1],
                        karaNames[kd.getKara3() - 1], karaNames[kd.getKara4() - 1]);
            }
        }
        binding.panchangamLayout.karanamTxt.setText(kara);

        String yogam = "-";
        YogamData yd = DBHelper.getInstance(activity).getYogam(dateString);
        if (null != yd) {
            String[] yogaNames = activity.getResources().getStringArray(R.array.yogam_names);
            if (yd.getTime1().equals("-")) {
                yogam = activity.getString(R.string.fullday_pangachangam, yogaNames[yd.getYogam1() - 1]);
            } else if (yd.getTime2().equals("-")) {
                yogam = activity.getString(R.string.two_panchangam, DateUtil.expandedTime(yd.getTime1()),
                        yogaNames[yd.getYogam1() - 1], yogaNames[yd.getYogam2() - 1]);
            } else {
                yogam = activity.getString(R.string.three_panchangam, DateUtil.expandedTime(yd.getTime1()),
                        DateUtil.expandedTime(yd.getTime2()), yogaNames[yd.getYogam1() - 1],
                        yogaNames[yd.getYogam2() - 1], yogaNames[yd.getYogam3() - 1]);
            }
        }
        binding.panchangamLayout.yogamTxt.setText(yogam);

        // load rasi
        RasiData rd = DBHelper.getInstance(activity).getRasi(dateString);
        if (null != rd) {
            String[] values = activity.getResources().getStringArray(R.array.rasi_values);
            binding.rasiLayout.getRoot().setVisibility(View.VISIBLE);
            List<RasiViewData> rasiList = new ArrayList<>();
            rasiList.add(new RasiViewData(R.drawable.mesham, activity.getString(R.string.mesham), values[rd.getMesham()]));
            rasiList.add(new RasiViewData(R.drawable.rishabam, activity.getString(R.string.rishabam), values[rd.getRishabam()]));
            rasiList.add(new RasiViewData(R.drawable.mithunam, activity.getString(R.string.mithunam), values[rd.getMithunam()]));
            rasiList.add(new RasiViewData(R.drawable.kadagam, activity.getString(R.string.kadagam), values[rd.getKadagam()]));
            rasiList.add(new RasiViewData(R.drawable.simmam, activity.getString(R.string.simmam), values[rd.getSimmam()]));
            rasiList.add(new RasiViewData(R.drawable.kanni, activity.getString(R.string.kanni), values[rd.getKanni()]));
            rasiList.add(new RasiViewData(R.drawable.thulam, activity.getString(R.string.thulam), values[rd.getThulam()]));
            rasiList.add(new RasiViewData(R.drawable.vrichagam, activity.getString(R.string.vrichagam), values[rd.getViruchagam()]));
            rasiList.add(new RasiViewData(R.drawable.thanusu, activity.getString(R.string.thanusu), values[rd.getDhanusu()]));
            rasiList.add(new RasiViewData(R.drawable.magaram, activity.getString(R.string.magaram), values[rd.getMagaram()]));
            rasiList.add(new RasiViewData(R.drawable.kumbam, activity.getString(R.string.kumbam), values[rd.getKumbam()]));
            rasiList.add(new RasiViewData(R.drawable.meenam, activity.getString(R.string.meenam), values[rd.getMeenam()]));

            RasiRecyclerAdapter rasiRecyclerAdapter = new RasiRecyclerAdapter(rasiList);
            binding.rasiLayout.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            binding.rasiLayout.recyclerView.setAdapter(rasiRecyclerAdapter);
        } else {
            binding.rasiLayout.getRoot().setVisibility(View.GONE);
        }

        RasiChartData rcd = DBHelper.getInstance(activity).getRasiChart(dateString);
        if (null != rcd) {
            binding.rasiChartLayout.h1Txt.setText(rcd.getH1().equals("-") ? " " : rcd.getH1());
            binding.rasiChartLayout.h2Txt.setText(rcd.getH2().equals("-") ? " " : rcd.getH2());
            binding.rasiChartLayout.h3Txt.setText(rcd.getH3().equals("-") ? " " : rcd.getH3());
            binding.rasiChartLayout.h4Txt.setText(rcd.getH4().equals("-") ? " " : rcd.getH4());
            binding.rasiChartLayout.h5Txt.setText(rcd.getH5().equals("-") ? " " : rcd.getH5());
            binding.rasiChartLayout.h6Txt.setText(rcd.getH6().equals("-") ? " " : rcd.getH6());
            binding.rasiChartLayout.h7Txt.setText(rcd.getH7().equals("-") ? " " : rcd.getH7());
            binding.rasiChartLayout.h8Txt.setText(rcd.getH8().equals("-") ? " " : rcd.getH8());
            binding.rasiChartLayout.h9Txt.setText(rcd.getH9().equals("-") ? " " : rcd.getH9());
            binding.rasiChartLayout.h10Txt.setText(rcd.getH10().equals("-") ? " " : rcd.getH10());
            binding.rasiChartLayout.h11Txt.setText(rcd.getH11().equals("-") ? " " : rcd.getH11());
            binding.rasiChartLayout.h12Txt.setText(rcd.getH12().equals("-") ? " " : rcd.getH12());

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

        StringBuilder sb = new StringBuilder();
        MuhurthamData muh = DBHelper.getInstance(activity).getMuhurtham(dateString);
        if (null != muh) {
            ImageView iv = new ImageView(activity.getApplicationContext());
            Glide.with(activity)
                    .load(R.drawable.wedding)
                    .into(iv);
            binding.importantDayLayout.imageLayout.addView(iv, params);
            sb.append(activity.getResources().getString(
                    R.string.suba_muhurtham,
                    (td != null && (td.getPirai() == -2 || td.getPirai() == 2)) ?
                            activity.getResources().getString(R.string.valapirai) : activity.getResources().getString(R.string.theipirai)));
            sb.append(", ");
        }

        List<VirathamData> virathamDataList = DBHelper.getInstance(activity).getVirathamList(dateString);

        String[] virathams = activity.getResources().getStringArray(R.array.viratham_names);

        for (int index = 0; index < virathamDataList.size(); index++) {
            VirathamData vd = virathamDataList.get(index);
            ImageView iv;
            switch (vd.getViratham()) {
                case 0: // new moon
                    break;

                case 1: // full moon
                    break;

                case 2:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.star)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.karthigaiTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 3:

                case 7: //Maha Sivarathri
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.sivarathri)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.sivarathriTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 4:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.chathurthi)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.sankada_chathurthi_txt)), Toast.LENGTH_SHORT).show());
                    break;

                case 5:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.thiruvonam)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.thiruvonamTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 6:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.shasti)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.shastiTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 8:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.astami)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.astamiTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 9:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.navami)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.navamiTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 10: // thasami

                    break;

                case 11: //Chandra Tharisanam
                    break;

                case 12:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.ekadeshi)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);
                    binding.importantDayLayout.imageLayout.setOnClickListener(view -> Toast.makeText(activity, activity.getString(R.string.day_msg, activity.getString(R.string.ekadeshiTxt)), Toast.LENGTH_SHORT).show());
                    break;

                case 13:
                    iv = new ImageView(activity.getApplicationContext());
                    Glide.with(activity)
                            .load(R.drawable.pradhosam)
                            .into(iv);
                    binding.importantDayLayout.imageLayout.addView(iv, params);

                    binding.importantDayLayout.imageLayout.setOnClickListener(
                            view -> {
                                assert td != null;
                                Toast.makeText(activity,
                                        activity.getString(R.string.day_msg, activity.getString((td.getPirai() == 2) ? R.string.pradosamValarPiraiTxt : R.string.pradosamTheiPiraiTxt)), Toast.LENGTH_SHORT).show();
                            });
                    break;
            }
            sb.append(virathams[vd.getViratham()]);
            if (vd.getTiming().length() > 1) {
                sb.append("[").append(vd.getTiming()).append("]");
            }
            sb.append(", ");
        }

        //KariNaal
        if (null != md && DBHelper.getInstance(activity).isKariNaal(md.getTmonth(), md.getTday())) {
            sb.append(activity.getResources().getString(R.string.kari_naal));
            sb.append(", ");
            ImageView iv = new ImageView(activity.getApplicationContext());
            Glide.with(activity)
                    .load(R.drawable.hotsun)
                    .into(iv);
            iv.setOnClickListener(view -> Toast.makeText(activity, String.format("%s %s", activity.getString(R.string.today),
                    activity.getString(R.string.kari_naal)), Toast.LENGTH_SHORT).show());
            binding.importantDayLayout.imageLayout.addView(iv, params);
        }


        if (sb.length() != 0) {
            if (sb.toString().endsWith(", ")) {
                sb.setLength(sb.toString().length() - 2);
            }
            binding.importantDayLayout.viruthamTxt.setText(sb.toString());
        } else {
            binding.importantDayLayout.viruthamTxt.setVisibility(View.GONE);
        }

        FestivalDayData fd = DBHelper.getInstance(activity).getFestivalDays(dateString);
        if (null != fd) {
            binding.govtHolidayTxt.setVisibility(fd.isLeave() ? View.VISIBLE : View.GONE);

            Set<String> festivals = new HashSet<>();
            if (fd.getHindhu() != null && !fd.getHindhu().equals("-")) {
                festivals.addAll(Arrays.asList(fd.getHindhu().split("[ ]*[,][ ]*")));
            }

            if (null != fd.getGovt() && !fd.getGovt().equals("-")) {
                festivals.addAll(Arrays.asList(fd.getGovt().split("[ ]*[,][ ]*")));
            }

            if (null != fd.getImportant() && !fd.getImportant().equals("-")) {
                festivals.addAll(Arrays.asList(fd.getImportant().split("[ ]*[,][ ]*")));
            }
            if (!festivals.isEmpty()) {
                binding.importantDayLayout.specialDayLayout.setVisibility(View.VISIBLE);
                binding.importantDayLayout.specialDayTxt.setText(StringUtils.join(festivals, ","));

            } else {
                binding.importantDayLayout.specialDayLayout.setVisibility(View.GONE);
            }
        }

        loadMiscData(binding);
    }

    public void loadMiscData(DayItemBinding binding) {
        long epochDays = LocalDate.now().toEpochDay();
        String quote = DBHelper.getInstance(activity).getQuote((int) (epochDays % CalendarApp.getMaxQuoteNumber(activity) + 1));
        KuralData kd = DBHelper.getInstance(activity).getKural((int) (epochDays % 1330 + 1));
        if (StringUtils.isBlank(quote) && null == kd) {
            binding.miscLayout.getRoot().setVisibility(View.GONE);
        }
        if (!StringUtils.isBlank(quote)) {
            binding.miscLayout.quoteTxt.setText(quote);
            binding.miscLayout.quoteTxt.setVisibility(View.VISIBLE);
        } else {
            binding.miscLayout.quoteTxt.setVisibility(View.GONE);
        }

        if (null != kd) {
            binding.miscLayout.kuralLayout.setVisibility(View.VISIBLE);
            binding.miscLayout.kuralNumberTxt.setText(activity.getString(R.string.kural_number_txt, kd.getId()));
            binding.miscLayout.athigaramTxt.setText(kd.getAthigaram());
            binding.miscLayout.kuralTxt.setText(kd.getKural());
        } else {
            binding.miscLayout.kuralLayout.setVisibility(View.GONE);
        }
    }


    private void nallaNeramData(DayItemBinding binding, String dateString) {
        // NallaNeram
        NallaNeramData nd = DBHelper.getInstance(activity).getNallaNeram(dateString);
        if (null != nd) {
            binding.nallaLayout.nallaNeramMorning.setText(nd.getNallaNeramM());

            binding.nallaLayout.nallaNeramEvening.setText(nd.getNallaNeramE());

            binding.nallaLayout.gowriMorning.setText(nd.getGowriM());

            binding.nallaLayout.gowriEvening.setText(nd.getGowriE());

            binding.sunriseLayout.sunriseTxt.setText(nd.getSunRise());

            binding.sunriseLayout.sunriseLaknamTxt.setText(getSunRiseLaknamText(nd.getLaknam(), nd.getLaknamTime()));
        }
    }

    private void raaghuSoolamData(@NonNull DayItemBinding binding, @NonNull LocalDate selectedDate) {
        // raaghu & soolam
        WeekData wd = DBHelper.getInstance(activity).getWeekData(selectedDate.getDayOfWeek());
        if (null != wd) {

            binding.raaghuEmaLayout.raaghuTxt.setText(wd.getRaaghu());

            binding.raaghuEmaLayout.emaTxt.setText(wd.getEma());

            binding.raaghuEmaLayout.kuligaiTxt.setText(wd.getKuligai());

            binding.raaghuEmaLayout.karananTxt.setText(wd.getKaranan());

            binding.vaaraSoolaiLayout.soolamTxt.setText(activity.getResources().getStringArray(R.array.directions)[wd.getSoolam() - 1]);

            binding.vaaraSoolaiLayout.parigaramTxt.setText(activity.getResources().getStringArray(R.array.parigaram)[wd.getParigaram() - 1]);

            int naa = wd.getSoolamTime();
            int[] hourMin = DateUtil.naazhigaiToHourMin(naa);
            binding.vaaraSoolaiLayout.soolamTimeTxt.setText(activity.getString(R.string.nazhigaiTime, naa, hourMin[0], hourMin[1]));
        }
    }

    @Override
    public int getItemCount() {
        return model.getList().size();
    }

    @NonNull
    private String getSunRiseLaknamText(int laknam, @NonNull String laknamTime) {
        String[] time = laknamTime.split("[.]");
        return activity.getResources().getString(R.string.sunrise_laknam_msg, activity.getResources().getStringArray(R.array.rasi_short_names)[laknam - 1], time[0], time[1]);
    }

    @NonNull
    private String monthText(@NonNull LocalDate selectedDate) {
        String[] monthNames = activity.getResources().getStringArray(R.array.en_month_names);
        String[] weekDayNames = activity.getResources().getStringArray(R.array.weekday_names);

        return monthNames[selectedDate.getMonthValue() - 1] + " - " + weekDayNames[selectedDate.getDayOfWeek().getValue() - 1];
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final DayItemBinding binding;

        public ViewHolder(@NonNull DayItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @NonNull
        public static ViewHolder from(@NonNull ViewGroup parent) {
            return new ViewHolder(
                    DayItemBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false)
            );
        }
    }
}
