package org.tem.calendar.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import org.tem.calendar.R;
import org.tem.calendar.adapter.ThreeTextViewRecyclerAdapter;
import org.tem.calendar.databinding.ActivityPalliPalanBinding;
import org.tem.calendar.db.DBHelper;
import org.tem.calendar.model.PalliPalanData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PalliPalanActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPalliPalanBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_palli_palan);

        List<PalliPalanData> ppList = DBHelper.getInstance(this).getPalliPalans();
        List<String[]> dataSet = new ArrayList<>();
        for(PalliPalanData ppd: ppList){
            dataSet.add(new String[]{ppd.getPart(), ppd.getRight(), ppd.getLeft()});
        }

        setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(new ThreeTextViewRecyclerAdapter(dataSet));
        binding.recyclerView.setHasFixedSize(false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

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