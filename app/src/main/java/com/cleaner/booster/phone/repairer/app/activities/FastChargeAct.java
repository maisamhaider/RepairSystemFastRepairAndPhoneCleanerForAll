package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.FastChargeAllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.async.FastAllAppsTask;
import com.cleaner.booster.phone.repairer.app.database.Db;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

public class FastChargeAct extends AppCompatActivity implements SelectAll {

    FastChargeAllAppsAdapter adapter;
    Db db;
    RecyclerView fastCharge_rv;
    CheckBox selectAll_cb1;
    TextView select_tv, doneDtn;
    boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_charge);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fastCharge_rv = findViewById(R.id.fastCharge_rv);
        selectAll_cb1 = findViewById(R.id.selectAll_cb1);
        select_tv = findViewById(R.id.select_tv);
        doneDtn = findViewById(R.id.doneDtn);

        db = new Db(this);
        adapter = new FastChargeAllAppsAdapter(this, this);

        new FastAllAppsTask(this, adapter, fastCharge_rv, db);

        Utils utils = new Utils(this);
        if (utils.GetAllInstalledApkInfo().isEmpty()) {
            select_tv.setVisibility(View.GONE);
            selectAll_cb1.setVisibility(View.GONE);
        }
        selectAll_cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectAll selectAll = adapter.getSelectAll();
                if (!b) {
                    selectAll.selectAll(true);
                    b = true;
                } else {
                    selectAll.selectAll(false);
                    b = false;
                }
            }
        });
        doneDtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastChargeAct.this.finish();
            }
        });

    }

    @Override
    public void selectAll(boolean isSelectAll) {
        if (isSelectAll) {
            selectAll_cb1.setChecked(true);
            b = true;
        } else {
            selectAll_cb1.setChecked(false);
            b = false;
        }

    }
}