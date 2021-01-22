package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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
    LottieAnimationView battery_lav;

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
        battery_lav = findViewById(R.id.battery_lav);

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
                selectAll.selectAll(b);
            }
        });
        powerSavingBtn_ll.setOnClickListener(v -> {

            if (allAppsAdapter.getCheckList().isEmpty()) {
                Toast.makeText(this, "Please select app first", Toast.LENGTH_SHORT).show();
            } else {
                startBoosting();
            }
        });

    }

    public void startBoosting() {
        List<String> packageName;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        packageName = allAppsAdapter.getCheckList();
        hibernatingAppsPkgMain_cl.setVisibility(View.VISIBLE);
        hibernatingAppsPkg_pb.setMax(packageName.size());
        packageName = allAppsAdapter.getCheckList();

        for (int i = 0; i < packageName.size(); i++) {
            am.killBackgroundProcesses(packageName.get(i));
         }
        startAnimation(packageName.size());

    }

    private void startAnimation(int setLevel) {
        ValueAnimator animator = ValueAnimator.ofInt(0, setLevel);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(5000);
        animator.addUpdateListener(valueAnimator -> {

            int value = (int) valueAnimator.getAnimatedValue();
            hibernatingAppsPkg_pb.setProgress(value);
            String bValue = value + "%";
            hibernatingAppsPkg_tv.setText(String.format("%s/%s", value, setLevel));


            if (value == setLevel) {
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
        });
        animator.start();
    }
}