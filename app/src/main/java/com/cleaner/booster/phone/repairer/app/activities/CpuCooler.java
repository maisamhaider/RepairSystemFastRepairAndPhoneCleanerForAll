package com.cleaner.booster.phone.repairer.app.activities;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.BatterySavingAllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class CpuCooler extends AppCompatActivity {
    Utils utils;
    ProgressBar cooling_pb;
     private BatterySavingAllAppsAdapter allAppsAdapter;
    ConstraintLayout cpuCoolerSecond_cl, cpuCoolerMain_cl, cpuCooled_cl, coolingMain_cl;
    TextView cpuTemp_tv;
    SharedPreferences preferences;
    RecyclerView cpuCoolerApps_rv;
    LinearLayout cpuCoolBtn_ll;
    private CheckBox selectAll_cb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_cooler);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);


        cpuCoolerSecond_cl = findViewById(R.id.cpuCoolerSecond_cl);
        cpuCoolerApps_rv = findViewById(R.id.cpuCoolerApps_rv);
        cpuCoolBtn_ll = findViewById(R.id.cpuCoolBtn_ll);
        cpuTemp_tv = findViewById(R.id.cpuTemp_tv);
         cpuCooled_cl = findViewById(R.id.cpuCooled_cl);
        selectAll_cb = findViewById(R.id.selectAll_cb);

        String temp = String.format("%.1f", utils.cpuTemperature());
        cpuTemp_tv.setText(temp);

        //scanning cpu
        final ConstraintLayout cpuCoolingFirstScreenMain_cl = findViewById(R.id.cpuCoolingFirstScreenMain_cl);

        //Cooling apps
        coolingMain_cl = findViewById(R.id.coolingMain_cl);
        cooling_pb = findViewById(R.id.cooling_pb);
         coolingMain_cl.setVisibility(View.GONE);

        //last screen
        cpuCoolerMain_cl = findViewById(R.id.cpuCoolerMain_cl);
        cpuCoolerMain_cl.setVisibility(View.GONE);

        //analyzing apps


        Calendar current = Calendar.getInstance();
        if (preferences.getLong("lastCpuCooledTime", current.getTimeInMillis()) > current.getTimeInMillis()) {
            cpuCoolerSecond_cl.setVisibility(View.GONE);
            @SuppressLint("DefaultLocale") String temp1 = String.format("%.1f", utils.cpuTemperature());
            cpuTemp_tv.setText(temp1);
            cpuCooled_cl.setVisibility(View.VISIBLE);

        } else {
            cpuCooled_cl.setVisibility(View.GONE);
            cpuCoolerSecond_cl.setVisibility(View.VISIBLE);
            @SuppressLint("DefaultLocale") String temp2 = String.format("%.1f", utils.cpuTemperature());
            cpuTemp_tv.setText(temp2);
        }



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cpuCoolingFirstScreenMain_cl.setVisibility(View.GONE);
            }
        }, 4000);


    }

    @SuppressLint("StaticFieldLeak")
    class KillAppsTask extends AsyncTask<Void, Integer, String> {
        List<String> packageName;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            packageName = allAppsAdapter.getCheckList();
            coolingMain_cl.setVisibility(View.VISIBLE);
            cooling_pb.setMax(packageName.size());

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
            cooling_pb.setProgress(values[0]);


        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            coolingMain_cl.setVisibility(View.GONE);
            cpuCoolerSecond_cl.setVisibility(View.GONE);
            cpuCoolerMain_cl.setVisibility(View.VISIBLE);
            Calendar nextTime = Calendar.getInstance();
            nextTime.add(Calendar.MINUTE, 5);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putLong("lastCpuCooledTime", nextTime.getTimeInMillis()).commit();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    @SuppressLint("DefaultLocale") String temp4 = String.format("%.1f", utils.cpuTemperature());
                    cpuTemp_tv.setText(temp4);
                    cpuCoolerMain_cl.setVisibility(View.GONE);
                    cpuCooled_cl.setVisibility(View.VISIBLE);

                }
            }, 2000);


        }
    }


    protected void onResume() {
        super.onResume();
        List<String> list = utils.getSystemActiveApps();
        allAppsAdapter = new BatterySavingAllAppsAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        cpuCoolerApps_rv.setLayoutManager(linearLayoutManager);
        cpuCoolerApps_rv.setAdapter(allAppsAdapter);
        allAppsAdapter.notifyDataSetChanged();

        cpuCoolBtn_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allAppsAdapter.getCheckList().isEmpty())
                {
                    Toast.makeText(CpuCooler.this,
                            "Please select app first", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    KillAppsTask appsTask = new KillAppsTask();
                    appsTask.execute();
                }


            }
        });
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

    }

    protected void onPause() {
        super.onPause();
    }
}