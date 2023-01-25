package org.tem.calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;

import org.tem.calendar.activities.BaseActivity;
import org.tem.calendar.activities.DashboardActivity;
import org.tem.calendar.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        Glide.with(this).load(R.drawable.vinayagar).into(binding.image1);

        long pauseTime = Constants.SPLASH_DISPLAY_LENGTH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            pauseTime = 0;
        }
        new Handler(Looper.myLooper()).postDelayed(() -> {
            /* Create an Intent that will start the Menu-Activity. */
            Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, pauseTime);


    }
}