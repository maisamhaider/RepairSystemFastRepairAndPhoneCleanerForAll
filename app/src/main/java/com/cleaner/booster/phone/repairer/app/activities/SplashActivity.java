package com.cleaner.booster.phone.repairer.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;

public class SplashActivity extends BaseActivity {

    boolean isTermAccepted = false;
    View vWelcome, vPrivacy;
    private Button declineBtn;
    private Button acceptBtn;
    private CheckBox checkBox;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        vWelcome = findViewById(R.id.v_welcome);
        vPrivacy = findViewById(R.id.v_privacy);
        declineBtn = findViewById(R.id.declineBtn);
        acceptBtn = findViewById(R.id.acceptBtn);
        checkBox = findViewById(R.id.tAndCB);

        isTermAccepted = preferences.getBoolean("isTermCondition", false);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isTermAccepted) {
                    sNewActivityAds(new MainActivity());
                } else {
                    vWelcome.setVisibility(View.GONE);
                    vPrivacy.setVisibility(View.VISIBLE);
                }
            }
        }, 2_500);

        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    editor.putBoolean("isTermCondition", true).commit();
                    startActivity(new Intent(SplashActivity.this,
                            MainActivity.class));
                } else {
                    Toast.makeText(SplashActivity.this,
                            "Please check the box first",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}