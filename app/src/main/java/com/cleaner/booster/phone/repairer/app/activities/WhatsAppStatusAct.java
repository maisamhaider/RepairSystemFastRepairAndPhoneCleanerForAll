package com.cleaner.booster.phone.repairer.app.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.fragments.StatusImagesFrag;
import com.cleaner.booster.phone.repairer.app.fragments.StatusSavedFrag;
import com.cleaner.booster.phone.repairer.app.fragments.StatusVideosFrag;
import com.cleaner.booster.phone.repairer.app.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class WhatsAppStatusAct extends AppCompatActivity {
    Utils utils;
    Fragment mFragment;
    File file1;
    BottomNavigationView bnv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_status);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);


        StatusImagesFrag statusImagesFrag = new StatusImagesFrag();
        loadmyfrag(statusImagesFrag);

        file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/.Statuses");

        bnv = findViewById(R.id.status_bnv);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.status_image:
                        loadmyfrag(new StatusImagesFrag());
                        break;
                    case R.id.status_video:
                        loadmyfrag(new StatusVideosFrag());

                        break;
                    case R.id.status_saved:
                        loadmyfrag(new StatusSavedFrag());

                        break;
                }
                return true;
            }
        });

    }

    public void loadmyfrag(Fragment fragment) {
        this.mFragment = fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.whatsAppStatusFragContainer_fl, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}