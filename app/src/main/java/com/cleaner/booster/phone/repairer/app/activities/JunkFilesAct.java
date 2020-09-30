package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.fragments.dashboard.DashboardFragment;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.StorageUtils;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static java.lang.Compiler.disable;

public class JunkFilesAct extends AppCompatActivity {
    private TextView trashCleanLast_tv;
    private GifImageView junkCollecting_giv, junkBinDone_giv;
    private ConstraintLayout cacheJunkBin_cl;
    private List<CommonModel> pkg;
    private Utils utils;
    private String dirPath;
    private StorageUtils storageUtils;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    MainActivity mainActivity;
    boolean isEFolderClean = false;
    boolean isCacheJunkClean = false;
    boolean isInstallationPkgClean = false;
    boolean isResidualJunkClean =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junk_files_atc);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        storageUtils = new StorageUtils();
        editor = preferences.edit();
        mainActivity = new MainActivity();

        LinearLayout junkFileCleanBtn_ll = findViewById(R.id.junkFileCleanBtn_ll);

        ImageView cacheJunkClear_iv = findViewById(R.id.cacheJunkClear_iv);
        ImageView residualJunk_iv = findViewById(R.id.residualJunk_iv);
        ImageView installationPackages_iv = findViewById(R.id.installationPackages_iv);
        ImageView junkFileEmptyFolderClear_iv = findViewById(R.id.junkFileEmptyFolderClear_iv);

        cacheJunkBin_cl = findViewById(R.id.cacheJunkBin_cl);

        trashCleanLast_tv = findViewById(R.id.trashCleanLast_tv);
        junkCollecting_giv = findViewById(R.id.junkCollecting_giv);
        junkBinDone_giv = findViewById(R.id.junkBinDone_giv);

        cacheJunkBin_cl.setVisibility(View.GONE);
        junkCollecting_giv.setVisibility(View.GONE);




            junkFileEmptyFolderClear_iv.setImageResource(R.drawable.ic_deselect);
            cacheJunkClear_iv.setImageResource(R.drawable.ic_deselect);
            installationPackages_iv.setImageResource(R.drawable.ic_deselect);
            residualJunk_iv.setImageResource(R.drawable.ic_deselect);


        //junk File Empty Folder
        junkFileEmptyFolderClear_iv.setOnClickListener(v -> {
            if (isEFolderClean) {
                junkFileEmptyFolderClear_iv.setImageResource(R.drawable.ic_deselect);
                isEFolderClean = false;
             } else {
                junkFileEmptyFolderClear_iv.setImageResource(R.drawable.ic_select);
                isEFolderClean = true;
            }
        });

        //cache junks
        cacheJunkClear_iv.setOnClickListener(v -> {
            if (isCacheJunkClean) {
                cacheJunkClear_iv.setImageResource(R.drawable.ic_deselect);
                isCacheJunkClean = false;
            } else {
                cacheJunkClear_iv.setImageResource(R.drawable.ic_select);
                isCacheJunkClean = true;
            }
        });

        installationPackages_iv.setOnClickListener(v -> {
            if (isInstallationPkgClean) {
                installationPackages_iv.setImageResource(R.drawable.ic_deselect);
                isInstallationPkgClean= false;
            } else {
                installationPackages_iv.setImageResource(R.drawable.ic_select);
                isInstallationPkgClean= true;
            }
        });


        residualJunk_iv.setOnClickListener(v -> {
            if (isInstallationPkgClean) {
                residualJunk_iv.setImageResource(R.drawable.ic_deselect);
                isInstallationPkgClean = false;
             } else {
                residualJunk_iv.setImageResource(R.drawable.ic_select);
                isInstallationPkgClean = true;
            }
        });


        dirPath = String.valueOf(Environment.getExternalStorageDirectory());
        utils = new Utils(this);





        junkFileCleanBtn_ll.setOnClickListener(v -> {
            if (isEFolderClean || isCacheJunkClean || isInstallationPkgClean || isResidualJunkClean) {
                new CacheClean().execute();
            }
            else
            {
                Toast.makeText(this, "Please select item first", Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        new JunkCleanerTask().execute();
    }


    public void lastView() {
        junkCollecting_giv.setVisibility(View.GONE);
        junkBinDone_giv.setVisibility(View.VISIBLE);
        junkBinDone_giv.setImageResource(R.drawable.junk_bin);
        trashCleanLast_tv.setText("CLEANING FINISHED");
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            cacheJunkBin_cl.setVisibility(View.GONE);
            junkBinDone_giv.setVisibility(View.GONE);
        }, 2000);
    }

    @SuppressLint("StaticFieldLeak")
    class JunkCleanerTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            pkg = utils.getAllPackages(dirPath);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }


    class CacheClean extends AsyncTask<Void, Integer, String> {
        String p = Environment.getExternalStorageDirectory().getAbsolutePath();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cacheJunkBin_cl.setVisibility(View.VISIBLE);
            junkCollecting_giv.setVisibility(View.VISIBLE);
            junkCollecting_giv.setFreezesAnimation(true);
            trashCleanLast_tv.setText("CLEANING");

        }

        @Override
        protected String doInBackground(Void... voids) {
            cleanOrFinishFun(true, p);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Handler handler = new Handler();
            handler.postDelayed(() -> lastView(), 3000);
        }
    }

    public void cleanOrFinishFun(boolean isClean, String p) {
        if (isClean) {

            if (preferences.getBoolean("isCacheJunkClean", false)) {
                storageUtils.deleteCache(JunkFilesAct.this);
            }
            if (preferences.getBoolean("isEFolderClean", false)) {
                storageUtils.deleteEmptyFolder(dirPath);
                storageUtils.deleteCache(JunkFilesAct.this);
                for (CommonModel path : storageUtils.getAllUnUsableFile(p)) {
                    File file = new File(path.getPath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            if (preferences.getBoolean("isInstallationPkgClean", false)) {
                if (pkg.size() != 0) {
                    for (int i = 0; i < pkg.size(); i++) {
                        pkg.get(i).getPath();
                    }
                }
            }
            if (preferences.getBoolean("isResidualJunkClean", false)) {
                storageUtils.deleteCache(JunkFilesAct.this);
            }
        } else {
            finish();
        }
    }

}