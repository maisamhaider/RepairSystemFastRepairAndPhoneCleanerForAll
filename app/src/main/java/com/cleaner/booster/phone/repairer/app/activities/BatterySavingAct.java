package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.BatterySavingAllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.Utils;


import java.util.Calendar;
import java.util.List;

public class BatterySavingAct extends BaseActivity {
    Utils utils;

    ConstraintLayout hibernatingAppsPkgMain_cl;
    TextView hibernatingAppsPkg_tv, powerSavingMainAppsAmount_tv;
    ProgressBar hibernatingAppsPkg_pb;
     private BatterySavingAllAppsAdapter allAppsAdapter;
    ConstraintLayout powerSavingSecond_cl, powerSavingLastScreenMain_cl, noDrainingApp_cl;
    SharedPreferences preferences;
    CheckBox selectAll_cb;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_saving);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        utils = new Utils(this);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);


        powerSavingSecond_cl = findViewById(R.id.powerSavingSecond_cl);
        RecyclerView powerSavingApp_rv = findViewById(R.id.powerSavingApp_rv);
        LinearLayout powerSavingBtn_ll = findViewById(R.id.powerSavingBtn_ll);
         powerSavingMainAppsAmount_tv = findViewById(R.id.powerSavingMainAppsAmount_tv);
         noDrainingApp_cl = findViewById(R.id.noDrainingApp_cl);
        selectAll_cb = findViewById(R.id.selectAll_cb);


        //analyzing apps
        ConstraintLayout powerSavingFirstScreenMain_cl = findViewById(R.id.powerSavingFirstScreenMain_cl);
        ProgressBar progressBar = findViewById(R.id.roundedHorizontalPSF_pb);
        TextView analyzingBatteryStatusPercent_tv = findViewById(R.id.analyzingBatteryStatusPercent_tv);

        //hibernating apps
        hibernatingAppsPkgMain_cl = findViewById(R.id.hibernatingAppsPkgMain_cl);
        hibernatingAppsPkg_tv = findViewById(R.id.hibernatingAppsPkg_tv);
        hibernatingAppsPkg_pb = findViewById(R.id.hibernatingAppsPkg_pb);
         hibernatingAppsPkgMain_cl.setVisibility(View.GONE);

        //last screen
        powerSavingLastScreenMain_cl = findViewById(R.id.powerSavingLastScreenMain_cl);
         powerSavingLastScreenMain_cl.setVisibility(View.GONE);


        //analyzing apps
        progressBar.setMax(100);
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(3_500);
        animator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            progressBar.setProgress(value);
        });
        animator.start();
        List<String> list = utils.getActiveApps();
        Calendar current = Calendar.getInstance();
        if (preferences.getLong("lastBatterSaveTime", current.getTimeInMillis()) > current.getTimeInMillis()) {
            powerSavingSecond_cl.setVisibility(View.GONE);
            powerSavingMainAppsAmount_tv.setText("0");
            noDrainingApp_cl.setVisibility(View.VISIBLE);

        } else {
            noDrainingApp_cl.setVisibility(View.GONE);
            powerSavingSecond_cl.setVisibility(View.VISIBLE);
            powerSavingMainAppsAmount_tv.setText(String.valueOf(list.size()));
         }


        ValueAnimator animatorText = ValueAnimator.ofInt(0, 100);
        animatorText.setInterpolator(new LinearInterpolator());
        animatorText.setStartDelay(0);
        animatorText.setDuration(3_500);

        animatorText.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            analyzingBatteryStatusPercent_tv.setText(value + "%");
        });
        animatorText.start();

        Handler handler = new Handler();
        handler.postDelayed(() -> powerSavingFirstScreenMain_cl.setVisibility(View.GONE), 3500);


        allAppsAdapter = new BatterySavingAllAppsAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        powerSavingApp_rv.setLayoutManager(linearLayoutManager);
        powerSavingApp_rv.setAdapter(allAppsAdapter);
        allAppsAdapter.notifyDataSetChanged();

        selectAll_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SelectAll selectAll = allAppsAdapter.getSelectAll();
                if (b)
                {
                    selectAll.selectAll(true);
                }
                else
                {
                    selectAll.selectAll(false);
                }
            }
        });
        powerSavingBtn_ll.setOnClickListener(v -> {

            if (allAppsAdapter.getCheckList().isEmpty())
            {
                Toast.makeText(this, "Please select app first", Toast.LENGTH_SHORT).show();
            }
            else
            {
                KillAppsTask appsTask = new KillAppsTask();
                appsTask.execute();
            }

        });

    }

    @SuppressLint("StaticFieldLeak")
    class KillAppsTask extends AsyncTask<Void, Integer, String> {
        List<String> packageName;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            packageName = allAppsAdapter.getCheckList();
            hibernatingAppsPkgMain_cl.setVisibility(View.VISIBLE);
            hibernatingAppsPkg_pb.setMax(packageName.size());

        }

        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 0; i < packageName.size(); i++) {
                am.killBackgroundProcesses(packageName.get(i));
                publishProgress(i);
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            hibernatingAppsPkg_pb.setProgress(values[0]);
            hibernatingAppsPkg_tv.setText(String.format("%s/%s", values[0], packageName.size()));


        }


        @SuppressLint("ApplySharedPref")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hibernatingAppsPkgMain_cl.setVisibility(View.GONE);
            powerSavingSecond_cl.setVisibility(View.GONE);
            powerSavingLastScreenMain_cl.setVisibility(View.VISIBLE);
            Calendar nextTime = Calendar.getInstance();
            nextTime.add(Calendar.MINUTE, 5);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("lastBatterSaveTime", nextTime.getTimeInMillis()).commit();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                powerSavingMainAppsAmount_tv.setText("0");
                powerSavingLastScreenMain_cl.setVisibility(View.GONE);
                noDrainingApp_cl.setVisibility(View.VISIBLE);

            }, 2000);


        }
    }
}