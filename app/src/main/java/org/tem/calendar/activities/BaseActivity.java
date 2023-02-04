package org.tem.calendar.activities;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.tem.calendar.Constants;
import org.tem.calendar.R;

public class BaseActivity extends AppCompatActivity {

    private int themeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences pref = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
        int theme = pref.getInt(Constants.PREF_THEME, Constants.THEME_DEFAULT);
        if (theme == Constants.THEME_RED) {
            setTheme(R.style.Theme_Calendar_Red);
        } else if (theme == Constants.THEME_GREEN) {
            setTheme(R.style.Theme_Calendar_Green);
        } else if (theme == Constants.THEME_GREY) {
            setTheme(R.style.Theme_Calendar_Grey);
        } else if (theme == Constants.THEME_ORANGE) {
            setTheme(R.style.Theme_Calendar_Orange);
        } else if (theme == Constants.THEME_NIGHT) {
            setTheme(R.style.Theme_Calendar_Night);
        } else {
            setTheme(R.style.Theme_Calendar_Default);
        }
        themeId = theme;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        int themeNew = getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE).getInt(Constants.PREF_THEME, Constants.THEME_DEFAULT);
        if (themeId != themeNew) {
            recreate();
        }
        super.onResume();
    }
}
