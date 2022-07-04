package org.tem.calendar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import org.tem.calendar.R;
import org.tem.calendar.databinding.ActivityManaiyadiSastharamBinding;

import java.util.Objects;

public class ManaiyadiSastharamActivity extends AppCompatActivity {

    ActivityManaiyadiSastharamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manaiyadi_sastharam);

        binding.toolbar.setSubtitle(R.string.manaiyadi_label);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.webView.loadUrl("file:///android_asset/manaiyadi_sastharam.html");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }
}