package org.tem.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.tem.calendar.Constants;
import org.tem.calendar.R;
import org.tem.calendar.adapter.LineRecyclerAdapter;
import org.tem.calendar.adapter.RasiDialogItemAdapter;
import org.tem.calendar.custom.StringUtils;
import org.tem.calendar.databinding.ActivityYearPalanBinding;
import org.tem.calendar.databinding.RasiPalatteDialogBinding;
import org.tem.calendar.model.LineDataModel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YearPalanActivity extends AppCompatActivity {

    private ActivityYearPalanBinding binding;

    private int currentPosition = 1;

    private String folder = "year_palan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_year_palan);

        Intent intent = getIntent();
        String type;
        if(null != intent){
            type = intent.getStringExtra(Constants.EXTRA_TYPE);
        } else {
            type = "year_palan";
        }

        if(null != type){
            folder = type;
        }
        binding.toolbar.setTitle(R.string.tamil_calendar);
        binding.toolbar.setSubtitle(R.string.year_palan);

        setSupportActionBar(binding.toolbar);

        showDialog();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

    }

    private void showDialog() {
        RasiPalatteDialogBinding rpd = RasiPalatteDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(rpd.getRoot()).setCancelable(true);
        builder.setOnCancelListener(dialog -> loadData(currentPosition));

        AlertDialog dialog = builder.create();
        dialog.show();

        rpd.recyclerView.setHasFixedSize(false);
        rpd.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        rpd.recyclerView.setAdapter(new RasiDialogItemAdapter(position -> {
            loadData(position + 1);
            currentPosition = position;
            dialog.dismiss();
        }, Arrays.asList(
                R.drawable.mesham,
                R.drawable.rishabam,
                R.drawable.mithunam,
                R.drawable.kadagam,
                R.drawable.simmam,
                R.drawable.kanni,
                R.drawable.thulam,
                R.drawable.vrichagam,
                R.drawable.thanusu, R.drawable.magaram,
                R.drawable.kumbam, R.drawable.meenam),
                getResources().getStringArray(R.array.rasi_names)));
    }

    private void loadData(int rasiId) {

        List<LineDataModel> dataSet = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(folder + "/" + rasiId + ".txt")))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (StringUtils.isBlank(line)) {
                    dataSet.add(LineDataModel.EMPTY);
                } else {
                    String[] parts = line.split("#", -1);
                    if (parts.length == 5) {
                        dataSet.add(LineDataModel.of(parts[1], parts[2], parts[3], parts[4]));
                    }
                    if (parts.length == 1) {
                        dataSet.add(LineDataModel.of(line));
                    } else {
                        dataSet.add(LineDataModel.EMPTY);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(false);
        binding.recyclerView.setAdapter(new LineRecyclerAdapter(dataSet));

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        menu.clear();
        menu.add(Menu.NONE, 1001, Menu.NONE, "Change").setIcon(R.drawable.ic_change_dark)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1001) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }
}