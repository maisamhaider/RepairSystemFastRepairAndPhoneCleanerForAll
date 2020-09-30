package com.cleaner.booster.phone.repairer.app.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleaner.booster.phone.repairer.app.BuildConfig;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.fragments.dashboard.DashboardFragment;
import com.cleaner.booster.phone.repairer.app.fragments.me.AboutUsFragment;
import com.cleaner.booster.phone.repairer.app.fragments.tools.ToolsFragment;
import com.cleaner.booster.phone.repairer.app.services.SmartChargeService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ImageView home_nav_iv, tools_nav_iv, me_nav_iv;
    private TextView home_nav_tv, tools_nav_tv, me_nav_tv;
    private ConstraintLayout home_nav_cl, tools_nav_cl, me_nav_cl;
    SharedPreferences preferences;
    int fragInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);

        home_nav_iv = findViewById(R.id.home_nav_iv);
        tools_nav_iv = findViewById(R.id.tools_nav_iv);
        me_nav_iv = findViewById(R.id.me_nav_iv);

        home_nav_tv = findViewById(R.id.home_nav_tv);
        tools_nav_tv = findViewById(R.id.tools_nav_tv);
        me_nav_tv = findViewById(R.id.me_nav_tv);

        home_nav_cl = findViewById(R.id.home_nav_cl);
        tools_nav_cl = findViewById(R.id.tools_nav_cl);
        me_nav_cl = findViewById(R.id.me_nav_cl);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        if (preferences.getBoolean("IS_SMART_CHARGE_ENABLED", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(MainActivity.this, SmartChargeService.class));
            } else {
                startService(new Intent(MainActivity.this, SmartChargeService.class));
            }
        }

        home_nav_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFun(v);
            }
        });
        tools_nav_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFun(v);
            }
        });
        me_nav_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navFun(v);
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            exitt();
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment).commit();
    }

    public void exitt() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = getLayoutInflater();
            @SuppressLint("InflateParams") final View dialogView = layoutInflater.inflate(R.layout.dialog_exit, null);
            Button yes = dialogView.findViewById(R.id.yes);
            Button no = dialogView.findViewById(R.id.no);
            Button rate = dialogView.findViewById(R.id.rate);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            yes.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View view) {
                    moveTaskToBack(true);
                    alertDialog.cancel();
                    finishAffinity();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rateUs();
                }
            });
        } catch (Exception a) {
            a.printStackTrace();
        }
    }

    public void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragInt == 0) {
            homeSelected();
        } else if (fragInt == 1) {
            toolSelected();
        } else if (fragInt == 2) {
            aboutSelected();
        }
    }

    public void navFun(View view) {
        switch (view.getId()) {
            case R.id.home_nav_cl:
                homeSelected();
                break;
            case R.id.tools_nav_cl:
                toolSelected();

                break;
            case R.id.me_nav_cl:
                aboutSelected();
                break;
        }
    }

    public void homeSelected() {
        loadFragment(new DashboardFragment());
        fragInt = 0;
        home_nav_iv.setImageResource(R.drawable.ic_select_home);
        tools_nav_iv.setImageResource(R.drawable.ic_tools);
        me_nav_iv.setImageResource(R.drawable.ic_more);

        home_nav_tv.setVisibility(View.VISIBLE);
        tools_nav_tv.setVisibility(View.GONE);
        me_nav_tv.setVisibility(View.GONE);

        home_nav_cl.setBackground(getDrawable(R.drawable.orange_two_d));
        tools_nav_cl.setBackgroundColor(Color.WHITE);
        me_nav_cl.setBackgroundColor(Color.WHITE);

    }

    public void toolSelected() {
        loadFragment(new ToolsFragment());
        fragInt = 1;
        home_nav_iv.setImageResource(R.drawable.ic_home);
        tools_nav_iv.setImageResource(R.drawable.ic_select_tools);
        me_nav_iv.setImageResource(R.drawable.ic_more);

        home_nav_tv.setVisibility(View.GONE);
        tools_nav_tv.setVisibility(View.VISIBLE);
        me_nav_tv.setVisibility(View.GONE);

        home_nav_cl.setBackgroundColor(Color.WHITE);
        tools_nav_cl.setBackground(getDrawable(R.drawable.orange_two_d));
        me_nav_cl.setBackgroundColor(Color.WHITE);


    }

    public void aboutSelected() {
        fragInt = 2;
        loadFragment(new AboutUsFragment());
        home_nav_iv.setImageResource(R.drawable.ic_home);
        tools_nav_iv.setImageResource(R.drawable.ic_tools);
        me_nav_iv.setImageResource(R.drawable.ic_select_more);

        home_nav_tv.setVisibility(View.GONE);
        tools_nav_tv.setVisibility(View.GONE);
        me_nav_tv.setVisibility(View.VISIBLE);

        home_nav_cl.setBackgroundColor(Color.WHITE);
        tools_nav_cl.setBackgroundColor(Color.WHITE);
        me_nav_cl.setBackground(getDrawable(R.drawable.orange_two_d));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}