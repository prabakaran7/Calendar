package org.tem.calendar.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.messaging.FirebaseMessaging;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.databinding.ActivitySettingsBinding;

import java.util.Objects;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        SharedPreferences pref = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        setSupportActionBar(binding.toolbar);

        int theme = pref.getInt(Constants.PREF_THEME, Constants.THEME_DEFAULT);

        if (theme == Constants.THEME_RED) {
            binding.radioTheme.check(binding.themeRed.getId());
        } else if (theme == Constants.THEME_GREEN) {
            binding.radioTheme.check(binding.themeGreen.getId());
        } else {
            binding.radioTheme.check(binding.themeDefault.getId());
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.radioTheme.setOnCheckedChangeListener((group, checkedId) -> {
            int themeNew;
            if (checkedId == binding.themeRed.getId()) {
                themeNew = Constants.THEME_RED;
            } else if (checkedId == binding.themeGreen.getId()) {
                themeNew = Constants.THEME_GREEN;
            } else {
                themeNew = Constants.THEME_DEFAULT;
            }
            pref.edit().putInt(Constants.PREF_THEME, themeNew).apply();
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
        });

        binding.generalNotification.setChecked(pref.getBoolean(Constants.PREF_GENERAL_NOTIFICATIONS, true));

        binding.generalNotification.setOnClickListener(view -> {
            boolean notifications = binding.generalNotification.isChecked();
            pref.edit().putBoolean(Constants.PREF_GENERAL_NOTIFICATIONS, notifications).apply();
            if (notifications) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.NOTIFICATION_CALENDAR);
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.NOTIFICATION_CALENDAR);
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
}