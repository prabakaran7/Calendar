package org.tem.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(SplashActivity.this, DashboardActivity.class);
//        Intent mainIntent = new Intent(SplashActivity.this, CalendarActivity.class);
//        Intent mainIntent = new Intent(SplashActivity.this, ScrollingDayActivity.class);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();

    }
}