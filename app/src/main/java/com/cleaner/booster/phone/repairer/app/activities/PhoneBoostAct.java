package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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

public class PhoneBoostAct extends AppCompatActivity {
    Utils utils;
    ProgressBar phoneBoosting_pb;
    private BatterySavingAllAppsAdapter allAppsAdapter;
    ConstraintLayout phoneBoostSecond_cl, phoneBoostedLastMain_cl, phoneBoostedMain1_cl, BoostingMain_cl;
    TextView phoneBoostUsedPercent_tv, phoneBoostRamDetail_tv;
    SharedPreferences preferences;
    CheckBox selectAll_cb;
    ActivityManager activityManager;
    ActivityManager.MemoryInfo memoryInfo;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_boost);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        phoneBoostSecond_cl = findViewById(R.id.phoneBoostSecond_cl);
        RecyclerView phoneBoostApps_rv = findViewById(R.id.phoneBoostApps_rv);
        LinearLayout phoneBoostBtn_ll = findViewById(R.id.phoneBoostBtn_ll);
        phoneBoostUsedPercent_tv = findViewById(R.id.phoneBoostUsedPercent_tv);
        phoneBoostedMain1_cl = findViewById(R.id.phoneBoostedMain1_cl);
        phoneBoostRamDetail_tv = findViewById(R.id.phoneBoostRamDetail_tv);
        selectAll_cb = findViewById(R.id.selectAll_cb);


        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        float totalRam1 = memoryInfo.totalMem;
        float freeRam1 = memoryInfo.availMem;
        float usedRam = totalRam1 - freeRam1;
        phoneBoostRamDetail_tv.setText(utils.getCalculatedDataSize(usedRam) + "/" + utils.getCalculatedDataSize(totalRam1));
        String percent = (String.format("%.1f", utils.getPercentage(totalRam1, usedRam)));
        phoneBoostUsedPercent_tv.setText(percent);

        //scanning Ram

        //Boosting ram
        BoostingMain_cl = findViewById(R.id.BoostingMain_cl);
        phoneBoosting_pb = findViewById(R.id.phoneBoosting_pb);
        BoostingMain_cl.setVisibility(View.GONE);

        //boosted screen
        phoneBoostedLastMain_cl = findViewById(R.id.phoneBoostedLastMain_cl);
        phoneBoostedLastMain_cl.setVisibility(View.GONE);


        List<String> list = utils.getSystemActiveApps();
        Calendar current = Calendar.getInstance();
        if (preferences.getLong("lastPhoneBoostTime", current.getTimeInMillis()) > current.getTimeInMillis()) {
            phoneBoostSecond_cl.setVisibility(View.GONE);
            float totalRam3 = memoryInfo.totalMem;
            float freeRam3 = memoryInfo.availMem;
            float usedRam3 = totalRam3 - freeRam3;
            phoneBoostRamDetail_tv.setText(utils.getCalculatedDataSize(usedRam3) + "/" + utils.getCalculatedDataSize(totalRam3));
            String percent3 = (String.format("%.0f", utils.getPercentage(totalRam3, usedRam3)));
            phoneBoostUsedPercent_tv.setText(percent3);
            phoneBoostedMain1_cl.setVisibility(View.VISIBLE);

        } else {
            phoneBoostedMain1_cl.setVisibility(View.GONE);
            phoneBoostSecond_cl.setVisibility(View.VISIBLE);
            float totalRam4 = memoryInfo.totalMem;
            float freeRam4 = memoryInfo.availMem;
            float usedRam4 = totalRam4 - freeRam4;
            phoneBoostRamDetail_tv.setText(utils.getCalculatedDataSize(usedRam4) + "/" + utils.getCalculatedDataSize(totalRam4));
            String percent4 = (String.format("%.0f", utils.getPercentage(totalRam4, usedRam4)));
            phoneBoostUsedPercent_tv.setText(percent4);
         }

        allAppsAdapter = new BatterySavingAllAppsAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        phoneBoostApps_rv.setLayoutManager(linearLayoutManager);
        phoneBoostApps_rv.setAdapter(allAppsAdapter);
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


        phoneBoostBtn_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allAppsAdapter.getCheckList().isEmpty())
                {
                    Toast.makeText(PhoneBoostAct.this,
                            "Please select app first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    KillAppsTask appsTask = new KillAppsTask();
                    appsTask.execute();
                }


            }
        });


    }

    class KillAppsTask extends AsyncTask<Void, Integer, String> {
        List<String> packageName;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            packageName = allAppsAdapter.getCheckList();
            BoostingMain_cl.setVisibility(View.VISIBLE);
            phoneBoosting_pb.setMax(packageName.size());

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
            phoneBoosting_pb.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            BoostingMain_cl.setVisibility(View.GONE);
            phoneBoostSecond_cl.setVisibility(View.GONE);
            phoneBoostedLastMain_cl.setVisibility(View.VISIBLE);
            Calendar nextTime = Calendar.getInstance();
            nextTime.add(Calendar.MINUTE, 5);
            SharedPreferences.Editor editor = preferences.edit();

            float totalRam2 = memoryInfo.totalMem;
            float freeRam2 = memoryInfo.availMem;
            float usedRam1 = totalRam2 - freeRam2;
            editor.putLong("lastPhoneBoostTime", nextTime.getTimeInMillis()).commit();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    phoneBoostUsedPercent_tv.setText(String.format("%.0f", utils.getPercentage(totalRam2, usedRam1)));
                    phoneBoostRamDetail_tv.setText(utils.getCalculatedDataSize(usedRam1) + "/" + utils.getCalculatedDataSize(totalRam2));
                    phoneBoostedLastMain_cl.setVisibility(View.GONE);
                    phoneBoostedMain1_cl.setVisibility(View.VISIBLE);

                }
            }, 3000);


        }
    }


    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }
}