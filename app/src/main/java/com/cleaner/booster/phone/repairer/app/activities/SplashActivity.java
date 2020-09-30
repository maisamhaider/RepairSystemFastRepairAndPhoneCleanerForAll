package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;

public class SplashActivity extends AppCompatActivity {

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

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isTermAccepted) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        if (!isTermAccepted) {
                            acceptBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (checkBox.isChecked()) {
                                        editor.putBoolean("isTermCondition",true);
                                        editor.commit();
                                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(SplashActivity.this, "Please check the box first", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            declineBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                        }

                        vWelcome.setVisibility(View.GONE);
                        vPrivacy.setVisibility(View.VISIBLE);
                    }
                }
            }, 2_500);
        }

}