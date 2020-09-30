package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.services.SmartChargeService;
import com.suke.widget.SwitchButton;

public class SmartChargingAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_charging_act);
         setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        SwitchButton smartCharging_switch = findViewById(R.id.smartCharging_switch);
         ImageView smartChargeFirstTimeBack_iv = findViewById(R.id.smartChargeFirstTimeBack_iv);
        LinearLayout smartChargeFirstTimeEnable_ll = findViewById(R.id.smartChargeFirstTimeEnable_ll);
        ConstraintLayout smart_charging_first_time = findViewById(R.id.smart_charging_first_time);

        ConstraintLayout fullChargedRemind_cl = findViewById(R.id.fullChargedRemind_cl);
        ConstraintLayout fastCharge_cl = findViewById(R.id.fastCharge_cl);

        boolean isEnable = preferences.getBoolean("IS_SMART_CHARGE_ENABLED", false);
        if (preferences.getBoolean("IS_SMART_CHARGE_ENABLED", false)) {
            smartCharging_switch.setChecked(true);
        }

        if (!isEnable) {
            smart_charging_first_time.setVisibility(View.VISIBLE);
        } else {
            smart_charging_first_time.setVisibility(View.GONE);
        }
        smartChargeFirstTimeEnable_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("IS_SMART_CHARGE_ENABLED", true).commit();
                smartCharging_switch.setChecked(true);
                  smart_charging_first_time.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(new Intent(SmartChargingAct.this, SmartChargeService.class));
                } else {
                    startService(new Intent(SmartChargingAct.this, SmartChargeService.class));
                }
            }
        });


        smartChargeFirstTimeBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        smartCharging_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("IS_SMART_CHARGE_ENABLED", true).commit();
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(new Intent(SmartChargingAct.this, SmartChargeService.class));
                    } else {
                        startService(new Intent(SmartChargingAct.this, SmartChargeService.class));
                    }
                } else {
                     editor.putBoolean("IS_SMART_CHARGE_ENABLED", false).commit();
                    stopService(new Intent(SmartChargingAct.this, SmartChargeService.class));

                }
            }
        });

        fullChargedRemind_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartChargingAct.this, FullChargedRemindAct.class);
                startActivity(intent);
            }
        });
        fastCharge_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmartChargingAct.this, FastChargeAct.class);
                startActivity(intent);
            }
        });
    }
}