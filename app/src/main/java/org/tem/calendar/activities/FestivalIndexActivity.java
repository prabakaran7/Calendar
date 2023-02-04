package org.tem.calendar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.ads.AdRequest;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.databinding.ActivityFestivalIndexBinding;
import org.tem.calendar.fragment.FestivalFragment;

import java.util.Objects;

public class FestivalIndexActivity extends BaseActivity {

    ActivityFestivalIndexBinding binding;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_festival_index);
        binding.adView.loadAd(new AdRequest.Builder().build());
        binding.toolbar.setSubtitle(R.string.holidaysAndFestivals);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.govtHolidays.setOnClickListener(view -> loadNextPage(FestivalFragment.HOLIDAY));

        binding.hinduFestivals.setOnClickListener(view -> loadNextPage(FestivalFragment.HINDU_FESTIVALS));

        binding.christinaFestivals.setOnClickListener(view -> loadNextPage(FestivalFragment.CHRIST_FESTIVALS));

        binding.muslimFestivals.setOnClickListener(view -> loadNextPage(FestivalFragment.MUSLIM_FESTIVALS));
    }

    private void loadNextPage(int index) {
        Intent intent = new Intent(FestivalIndexActivity.this, FestivalActivity.class);
        intent.putExtra(Constants.EXTRA_TYPE, index);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}