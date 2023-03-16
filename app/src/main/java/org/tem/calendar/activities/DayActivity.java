package org.tem.calendar.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.DayRecyclerAdapter;
import org.tem.calendar.adapter.ZoomOutPageTransformer;
import org.tem.calendar.databinding.ActivityDayBinding;
import org.tem.calendar.model.DayViewModel;

import java.time.LocalDate;

public class DayActivity extends BaseActivity {

    private final MutableLiveData<Integer> currentPosition = new MutableLiveData<>();
    private ActivityDayBinding binding;
    private DayViewModel viewModel;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_day);
        Intent intent = getIntent();

        LocalDate selectedDate;
        if (intent == null || intent.getExtras() == null || !intent.getExtras().containsKey(Constants.EXTRA_DATE_SELECTED)) {
            selectedDate = LocalDate.now();
        } else {
            selectedDate = (LocalDate) intent.getSerializableExtra(Constants.EXTRA_DATE_SELECTED);
        }

        binding.adView.loadAd(new AdRequest.Builder().build());
        viewModel = new DayViewModel(selectedDate);

        DayRecyclerAdapter adapter = new DayRecyclerAdapter(this, viewModel);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setPageTransformer(new ZoomOutPageTransformer());
        binding.viewPager.setCurrentItem(2, false);
        currentPosition.postValue(2);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition.postValue(position);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    if (binding.viewPager.getCurrentItem() == viewModel.getList().size() - 1) {
                        if (viewModel.shiftForward()) {
                            binding.viewPager.setCurrentItem(0, false);
                            binding.viewPager.post(adapter::notifyDataSetChanged);
                        }
                    } else if (binding.viewPager.getCurrentItem() == 0) {
                        if (viewModel.shiftBackward()) {
                            binding.viewPager.setCurrentItem(viewModel.getList().size() - 1, false);
                            binding.viewPager.post(adapter::notifyDataSetChanged);
                        }
                    }
                }
            }
        });

        currentPosition.observe(this, position -> {
            LocalDate ld = viewModel.getList().get(position);
            if (!ld.equals(LocalDate.now())) {
                binding.resetBtn.setVisibility(View.VISIBLE);
            } else {
                binding.resetBtn.setVisibility(View.GONE);
            }
        });


        binding.resetBtn.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, DayActivity.class));
        });


    }

    public void moveForward() {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
    }

    public void moveBackward() {
        binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
    }

    @Override
    protected void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}