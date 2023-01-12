package org.tem.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import org.tem.calendar.activities.DashboardActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.myLooper()).postDelayed(()->{
            /* Create an Intent that will start the Menu-Activity. */
            Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, Constants.SPLASH_DISPLAY_LENGTH);


    }
}