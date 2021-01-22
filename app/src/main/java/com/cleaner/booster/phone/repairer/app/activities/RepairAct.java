package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.List;

public class RepairAct extends AppCompatActivity {

    View vRepairOne, repair_two;
    private ProgressBar pb;
     private Utils untils;
    private TextView tvPackge,progress_tv;
    private List<String> packageName;
    private boolean enabledBackPress = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }

    private void init() {
        vRepairOne = findViewById(R.id.repair_one);
        repair_two = findViewById(R.id.repair_two);
        progress_tv = findViewById(R.id.progress_tv);
        pb = findViewById(R.id.pb);
         tvPackge = findViewById(R.id.tv_package);
        untils = new Utils(RepairAct.this);
        startRepairing();


    }

    public void startRepairing() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        packageName = untils.getActiveApps();
        vRepairOne.setVisibility(View.VISIBLE);
        pb.setMax(100);
        pb.setProgress(0);

        for (int i = 0; i < packageName.size(); i++) {
            am.killBackgroundProcesses(packageName.get(i));
        }
        startAnimation();

    }


    private void startAnimation() {

        vRepairOne.setVisibility(View.VISIBLE);
        enabledBackPress = false;
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(30_000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                pb.setProgress(value);
                progress_tv.setText(value + "%");
                tvPackge.setText(packageName.get(value));

                if ((100 - 1) == value) {
                    vRepairOne.setVisibility(View.GONE);
                    repair_two.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 2000);
                }
            }
        });
        animator.start();
    }

    @Override
    public void onBackPressed() {
        if (enabledBackPress) {
            super.onBackPressed();
        }
    }
}