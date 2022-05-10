package org.tem.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.databinding.ActivityDailyBinding;
import org.tem.calendar.library.ActivitySwipe2Detector;
import org.tem.calendar.library.SwipeInterface;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DailyActivity extends AppCompatActivity implements SwipeInterface {

    ActivityDailyBinding binding;

    LocalDate selectedDate;

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

        binding.headerLayout.dateTxt.setText(selectedDate.format(DateTimeFormatter.ofPattern("d-M-yyyy")));
        binding.headerLayout.monthHeaderTxt.setText(monthText());

        binding.getRoot().setOnTouchListener(new ActivitySwipe2Detector(this));

        binding.headerLayout.prevBtn.setOnClickListener(v -> moveToPrevDay());
        binding.headerLayout.nextBtn.setOnClickListener(v -> moveToNextDay());
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
//        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void moveToPrevDay() {
        finish();
        //overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        Intent intent = getIntent();
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.EXTRA_DATE_SELECTED, selectedDate.minusDays(1));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void right2left(View v) {

        moveToNextDay();
    }

    @Override
    public void top2bottom(View v) {

    }
}