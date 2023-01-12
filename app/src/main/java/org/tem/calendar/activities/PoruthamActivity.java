package org.tem.calendar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.tem.calendar.R;
import org.tem.calendar.adapter.StarMatchingRecyclerAdapter;
import org.tem.calendar.databinding.ActivityPoruthamBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.StarMatching;
import org.tem.calendar.model.StarsData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PoruthamActivity extends BaseActivity {

    private ActivityPoruthamBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_porutham);

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        List<StarsData> starMap = DBHelper.getInstance(this).getStarMap();
        //Map<Integer, StarsData> starsDataMap = starMap.stream().collect(Collectors.toMap(StarsData::getId, e->e));

        List<String> stars = starMap.stream().map(StarsData::getStar).collect(Collectors.toList());
        stars.add(0, getString(R.string.select_star));

        ArrayAdapter<String> boyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stars);
        boyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> girlAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stars);
        girlAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.boySpinner.setAdapter(boyAdapter);

        binding.boySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.container.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.girlSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.container.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.girlSpinner.setAdapter(girlAdapter);

        binding.matchBtn.setOnClickListener(view ->{
            int boyIndex = binding.boySpinner.getSelectedItemPosition();
            int girlIndex = binding.girlSpinner.getSelectedItemPosition();


            if(boyIndex == 0) {
                binding.errMsg.setText(R.string.select_boy_star);
                return;
            }

            if(girlIndex == 0){
                binding.errMsg.setText(R.string.select_girl_star);
                return;
            }

            StarMatching sm = DBHelper.getInstance(this).getStarPorutham(boyIndex, girlIndex);
            binding.errMsg.setText("");
            if(sm != null){
                binding.container.setVisibility(View.VISIBLE);
                List<Object[]> list = new ArrayList<>();
                int count = 0;
                String[] poruthamArray = getResources().getStringArray(R.array.poruthams);
                String[] poruthamExp = getResources().getStringArray(R.array.poruthams_explanations);
                list.add(new Object[]{poruthamArray[count], sm.getP1(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP2(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP3(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP4(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP5(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP6(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP7(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP8(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP9(), poruthamExp[count]});
                count++;
                list.add(new Object[]{poruthamArray[count], sm.getP10(), poruthamExp[count]});
                count++;

                list.add(new Object[]{poruthamArray[count], sm.getP11(), poruthamExp[count]});
                count++;

                list.add(new Object[]{poruthamArray[count], sm.getP12(), poruthamExp[count]});

                binding.matchTxt.setText(getString(R.string.matched_str, sm.getTotal(), list.size()));

                binding.recyclerView.setHasFixedSize(false);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                binding.recyclerView.setAdapter(new StarMatchingRecyclerAdapter(list));

            }

        });
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