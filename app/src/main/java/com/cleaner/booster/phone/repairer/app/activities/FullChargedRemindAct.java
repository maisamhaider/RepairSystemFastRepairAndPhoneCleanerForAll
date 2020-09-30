package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.cleaner.booster.phone.repairer.app.R;
import com.suke.widget.SwitchButton;

public class FullChargedRemindAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_charged_remind);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        SwitchButton fullChargedAlarm_switch, fullChargedRemindSound_switch, fullChargedRemindVibrate_switch;

        fullChargedAlarm_switch = findViewById(R.id.fullChargedAlarm_switch);
        fullChargedRemindSound_switch = findViewById(R.id.fullChargedRemindSound_switch);
        fullChargedRemindVibrate_switch = findViewById(R.id.fullChargedRemindVibrate_switch);
        if (preferences.getBoolean("FULL_CHARGED_ALARM",false))
        {
            fullChargedAlarm_switch.setChecked(true);
            if (preferences.getBoolean("FULL_CHARGED_SOUND",false))
            {
                fullChargedRemindSound_switch.setChecked(true);
            }
            if (preferences.getBoolean("FULL_CHARGED_VIBRATE",false))
            {
                fullChargedRemindVibrate_switch.setChecked(true);
            }

        }

        fullChargedAlarm_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    fullChargedRemindSound_switch.setEnabled(true);
                    fullChargedRemindVibrate_switch.setEnabled(true);

                    fullChargedRemindSound_switch.setChecked(true);
                    fullChargedRemindVibrate_switch.setChecked(true);


                    editor.putBoolean("FULL_CHARGED_ALARM", true).commit();
                    editor.putBoolean("FULL_CHARGED_SOUND", true).commit();
                    editor.putBoolean("FULL_CHARGED_VIBRATE", true).commit();

                } else {
                    fullChargedRemindSound_switch.setChecked(false);
                    fullChargedRemindVibrate_switch.setChecked(false);

                    fullChargedRemindSound_switch.setEnabled(false);
                    fullChargedRemindVibrate_switch.setEnabled(false);

                    editor.putBoolean("FULL_CHARGED_ALARM", false).commit();
                    editor.putBoolean("FULL_CHARGED_SOUND", false).commit();
                    editor.putBoolean("FULL_CHARGED_VIBRATE", false).commit();

                }
            }
        });

        fullChargedRemindSound_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("FULL_CHARGED_SOUND", true).commit();

                } else {
                    editor.putBoolean("FULL_CHARGED_SOUND", false).commit();

                }
            }
        });
        fullChargedRemindVibrate_switch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean("FULL_CHARGED_VIBRATE", true).commit();

                } else {
                    editor.putBoolean("FULL_CHARGED_VIBRATE", false).commit();

                }
            }
        });

    }
}