package com.cleaner.booster.phone.repairer.app.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.fragments.MaintenanceFrag;
import com.cleaner.booster.phone.repairer.app.fragments.dashboard.DashboardFragment;
import com.cleaner.booster.phone.repairer.app.fragments.me.AboutUsFragment;
import com.cleaner.booster.phone.repairer.app.fragments.tools.ToolsFragment;
import com.cleaner.booster.phone.repairer.app.permission.Permissions;
import com.cleaner.booster.phone.repairer.app.services.SmartChargeService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    SharedPreferences preferences;
     BottomNavigationView mainBnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        mainBnv = findViewById(R.id.mainBnv);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        if (preferences.getBoolean("IS_SMART_CHARGE_ENABLED", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(MainActivity.this, SmartChargeService.class));
            } else {
                startService(new Intent(MainActivity.this, SmartChargeService.class));
            }
        }
        loadFragment(new DashboardFragment());
        mainBnv.setSelectedItemId(R.id.navigation_home);
        mainBnv.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new DashboardFragment());
                    break;
                case R.id.navigation_tool:
                    loadFragment(new ToolsFragment());
                    break;
                case R.id.navigation_maintenance:
                    loadFragment(new MaintenanceFrag());
                    break;
                case R.id.navigation_me:
                    loadFragment(new AboutUsFragment());
                    break;
            }
            return true;
        });

    }

    public void dashboard()
    {
        mainBnv.setSelectedItemId(R.id.navigation_home);
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
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void finishApp()
    {
       this.finishAffinity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1111)
        {
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
               dashboard();
            }
            else
            {
                exitOrAllow();
            }
        }
    }
    public  void exitOrAllow()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this)
                .setTitle("Permission").setMessage("Storage permission is required to"
                        +" run application.with out permission main"+
                        " graphs that show storage information do not work")
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Permissions permissions = new Permissions(MainActivity.this);
                        permissions.permission();
                        dialog.dismiss();
                    }
                }).setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                      finishApp();
                    }
                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}