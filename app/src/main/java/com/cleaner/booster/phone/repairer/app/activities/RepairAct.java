package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.List;

import bot.box.appusage.utils.UsageUtils;

public class RepairAct extends AppCompatActivity {

    View vRepairOne,repair_two;
    private ProgressBar pb;
    private ImageView ivAppIcon;
    private Utils untils;
    private TextView tvPackge;
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
        pb = findViewById(R.id.pb);
        ivAppIcon = findViewById(R.id.iv_app_icon);
        tvPackge = findViewById(R.id.tv_package);
        untils = new Utils(RepairAct.this);
        new KillAppsTask().execute();


    }


    class KillAppsTask extends AsyncTask<Void, Integer, String> {

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            packageName = untils.getActiveApps();
            vRepairOne.setVisibility(View.VISIBLE);
            pb.setMax(packageName.size());
            pb.setProgress(0);
            startAnimation();
        }

        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 0; i < packageName.size(); i++) {
                am.killBackgroundProcesses(packageName.get(i));
                publishProgress(i); }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String s)
        { super.onPostExecute(s); }
    }


    private void startAnimation() {

        vRepairOne.setVisibility(View.VISIBLE);
        enabledBackPress = false;
        ValueAnimator animator = ValueAnimator.ofInt(0, packageName.size() - 1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(30_000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                pb.setProgress(value);
                tvPackge.setText(packageName.get(value));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(RepairAct.this).load(UsageUtils.parsePackageIcon(packageName.get(value), R.mipmap.ic_launcher))
                                .transition(new DrawableTransitionOptions().crossFade()).into(ivAppIcon);
                    }
                },500);

                if ((packageName.size() - 1) == value) {
                    vRepairOne.setVisibility(View.GONE);
                    repair_two.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run()
                        { finish(); }
                    },2000);
                } }
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