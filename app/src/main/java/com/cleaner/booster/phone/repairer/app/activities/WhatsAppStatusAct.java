package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.fragments.StatusImagesFrag;
import com.cleaner.booster.phone.repairer.app.fragments.StatusSavedFrag;
import com.cleaner.booster.phone.repairer.app.fragments.StatusVideosFrag;
import com.cleaner.booster.phone.repairer.app.fragments.dashboard.DashboardFragment;
import com.cleaner.booster.phone.repairer.app.fragments.me.AboutUsFragment;
import com.cleaner.booster.phone.repairer.app.fragments.tools.ToolsFragment;
import com.cleaner.booster.phone.repairer.app.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class WhatsAppStatusAct extends AppCompatActivity {
    Utils utils;
    Fragment mFragment;
    private ConstraintLayout image_bar_cl, video_bar_cl, saved_bar_cl;
    private ImageView image_bar_iv, video_bar_iv, saved_bar_iv;
    private TextView image_bar_tv, video_bar_tv, saved_bar_tv;
    private ImageView statusSaverMain_iv;
    private TextView amountOfStatusImages_tv;
    File file1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_status);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);

        statusSaverMain_iv = findViewById(R.id.statusSaverMain_iv);
        amountOfStatusImages_tv = findViewById(R.id.amountOfStatusImages_tv);

        image_bar_cl = findViewById(R.id.image_bar_cl);
        video_bar_cl = findViewById(R.id.video_bar_cl);
        saved_bar_cl = findViewById(R.id.saved_bar_cl);

        image_bar_iv = findViewById(R.id.image_bar_iv);
        video_bar_iv = findViewById(R.id.video_bar_iv);
        saved_bar_iv = findViewById(R.id.saved_bar_iv);

        image_bar_tv = findViewById(R.id.Image_bar_tv);
        video_bar_tv = findViewById(R.id.video_bar_tv);
        saved_bar_tv = findViewById(R.id.saved_bar_tv);

        StatusImagesFrag statusImagesFrag = new StatusImagesFrag();
        loadmyfrag(statusImagesFrag);

        file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/.Statuses");
        amountOfStatusImages_tv.setText(utils.getListFiles(file1, "images").size() + " images status found");
        statusSaverMain_iv.setImageResource(R.drawable.ic_status_saver_image);

        image_bar_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFun(image_bar_cl);
            }
        });
        video_bar_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFun(video_bar_cl);
            }
        });
        saved_bar_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFun(saved_bar_cl);
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

    public void navFun(View view) {
        switch (view.getId()) {
            case R.id.image_bar_cl:
                loadmyfrag(new StatusImagesFrag());
                image_bar_cl.setBackground(getDrawable(R.drawable.orange_two_d));
                video_bar_cl.setBackgroundColor(Color.WHITE);
                saved_bar_cl.setBackgroundColor(Color.WHITE);

                image_bar_iv.setImageResource(R.drawable.ic_select_image);
                video_bar_iv.setImageResource(R.drawable.ic_videos);
                saved_bar_iv.setImageResource(R.drawable.ic_saved);

                image_bar_tv.setVisibility(View.VISIBLE);
                video_bar_tv.setVisibility(View.GONE);
                saved_bar_tv.setVisibility(View.GONE);
                amountOfStatusImages_tv.setText(utils.getListFiles(file1, "images").size() + " images status found");
                statusSaverMain_iv.setImageResource(R.drawable.ic_status_saver_image);
                break;
            case R.id.video_bar_cl:
                loadmyfrag(new StatusVideosFrag());

                image_bar_cl.setBackgroundColor(Color.WHITE);
                video_bar_cl.setBackground(getDrawable(R.drawable.orange_two_d));
                saved_bar_cl.setBackgroundColor(Color.WHITE);

                image_bar_iv.setImageResource(R.drawable.ic_image);
                video_bar_iv.setImageResource(R.drawable.ic_select_video);
                saved_bar_iv.setImageResource(R.drawable.ic_saved);
                image_bar_tv.setVisibility(View.GONE);
                video_bar_tv.setVisibility(View.VISIBLE);
                saved_bar_tv.setVisibility(View.GONE);


                File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/.Statuses");
                amountOfStatusImages_tv.setText(utils.getListFiles(file2, "videos").size() + " videos status found");
                statusSaverMain_iv.setImageResource(R.drawable.ic_status_video);

                break;
            case R.id.saved_bar_cl:
                loadmyfrag(new StatusSavedFrag());


                image_bar_cl.setBackgroundColor(Color.WHITE);
                video_bar_cl.setBackgroundColor(Color.WHITE);
                saved_bar_cl.setBackground(getDrawable(R.drawable.orange_two_d));

                image_bar_iv.setImageResource(R.drawable.ic_image);
                video_bar_iv.setImageResource(R.drawable.ic_videos);
                saved_bar_iv.setImageResource(R.drawable.ic_select_saved);

                image_bar_tv.setVisibility(View.GONE);
                video_bar_tv.setVisibility(View.GONE);
                saved_bar_tv.setVisibility(View.VISIBLE);


                amountOfStatusImages_tv.setText("Status Saved");
                statusSaverMain_iv.setImageResource(R.drawable.ic_status_saved);


                break;
        }
    }
}