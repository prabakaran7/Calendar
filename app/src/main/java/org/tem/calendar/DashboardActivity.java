package org.tem.calendar;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.adapter.DashboardCategoryRecyclerViewAdapter;
import org.tem.calendar.databinding.ActivityDashboardBinding;
import org.tem.calendar.model.Dashboard;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        binding.categoryRecyclerView.setAdapter(new DashboardCategoryRecyclerViewAdapter(this));
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.categoryRecyclerView.setHasFixedSize(false);


    }

    public void onClick(Dashboard dashboard) {
        if (null != dashboard.getActivityClass()) {
            Intent intent = new Intent(DashboardActivity.this, dashboard.getActivityClass());
            if (null!= dashboard.getExtras() && !dashboard.getExtras().isEmpty()) {
                intent.putExtras(dashboard.getExtras());
            }

            startActivity(intent);
        }
    }
}