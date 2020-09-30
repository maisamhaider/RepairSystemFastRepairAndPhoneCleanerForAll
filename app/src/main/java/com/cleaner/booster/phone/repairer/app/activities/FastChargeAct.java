package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;
 import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.FastChargeAllAppsAdapter;
 import com.cleaner.booster.phone.repairer.app.async.FastAllAppsTask;
import com.cleaner.booster.phone.repairer.app.database.Db;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

public class FastChargeAct extends AppCompatActivity implements SelectAll{

    FastAllAppsTask allAppsTask;
    FastChargeAllAppsAdapter adapter;
    Db db;
    RecyclerView fastCharge_rv;
    CheckBox selectAll_cb1;
TextView select_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_charge);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fastCharge_rv = findViewById(R.id.fastCharge_rv);
        selectAll_cb1 = findViewById(R.id.selectAll_cb1);
        select_tv = findViewById(R.id.select_tv);

        db = new Db(this);
        adapter = new FastChargeAllAppsAdapter(this,this);

        allAppsTask = new FastAllAppsTask(this, adapter, fastCharge_rv, db);
        allAppsTask.execute();
        Utils utils = new Utils(this);
        if ( utils.GetAllInstalledApkInfo().isEmpty())
        {
            select_tv.setVisibility(View.GONE);
            selectAll_cb1.setVisibility(View.GONE);
        }
        selectAll_cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SelectAll selectAll = adapter.getSelectAll();
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
    @Override
    public void selectAll(boolean isSelectAll) {

        if (isSelectAll)
        {
            selectAll_cb1.setChecked(true);
        }
        else {
            selectAll_cb1.setChecked(false);
        }
    }
}