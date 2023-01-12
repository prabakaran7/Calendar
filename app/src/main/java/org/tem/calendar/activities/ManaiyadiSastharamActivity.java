package org.tem.calendar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.tem.calendar.R;
import org.tem.calendar.adapter.ManaiyadiViewPageAdapter;
import org.tem.calendar.databinding.ActivityManaiyadiSastharamBinding;

import java.util.Objects;

public class ManaiyadiSastharamActivity extends BaseActivity {

    ActivityManaiyadiSastharamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manaiyadi_sastharam);

        binding.toolbar.setSubtitle(R.string.manaiyadi_label);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.viewPager.setAdapter(new ManaiyadiViewPageAdapter(this));

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, ((tab, position) -> tab.setText(getResources().getStringArray(R.array.manaiyadi_types)[position]))).attach();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
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