package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.StorageUtils;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.List;

public class JunkFilesAct extends AppCompatActivity {
    private TextView trashCleanLast_tv;
    ImageView junkBinDone_iv;
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
    boolean isResidualJunkClean = false;
   LottieAnimationView lottie_av;

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

        ConstraintLayout cacheJunkClear_cl = findViewById(R.id.cacheJunkClear_cl);
        ConstraintLayout residualJunk_cl = findViewById(R.id.residualJunk_cl);
        ConstraintLayout installationPackages_cl = findViewById(R.id.installationPackages_cl);
        ConstraintLayout junkFileEmptyFolderClear_cl = findViewById(R.id.junkFileEmptyFolderClear_cl);


        ImageView cacheJunkClear_iv = findViewById(R.id.cacheJunkClear_iv);
        ImageView residualJunk_iv = findViewById(R.id.residualJunk_iv);
        ImageView installationPackages_iv = findViewById(R.id.installationPackages_iv);
        ImageView junkFileEmptyFolderClear_iv = findViewById(R.id.junkFileEmptyFolderClear_iv);

        cacheJunkBin_cl = findViewById(R.id.cacheJunkBin_cl);

        trashCleanLast_tv = findViewById(R.id.trashCleanLast_tv);
        junkBinDone_iv = findViewById(R.id.junkBinDone_iv);
        lottie_av = findViewById(R.id.lottie_av);


        junkFileEmptyFolderClear_iv.setImageResource(R.drawable.group_47);
        cacheJunkClear_iv.setImageResource(R.drawable.group_47);
        installationPackages_iv.setImageResource(R.drawable.group_47);
        residualJunk_iv.setImageResource(R.drawable.group_47);

        //junk File Empty Folder
        junkFileEmptyFolderClear_cl.setOnClickListener(v -> {
            if (isEFolderClean) {
                junkFileEmptyFolderClear_iv.setImageResource(R.drawable.group_47);
                isEFolderClean = false;
            } else {
                junkFileEmptyFolderClear_iv.setImageResource(R.drawable.ic_selected);
                isEFolderClean = true;
            }
        });

        //cache junks
        cacheJunkClear_cl.setOnClickListener(v -> {
            if (isCacheJunkClean) {
                cacheJunkClear_iv.setImageResource(R.drawable.group_47);
                isCacheJunkClean = false;
            } else {
                cacheJunkClear_iv.setImageResource(R.drawable.ic_selected);
                isCacheJunkClean = true;
            }
        });

        installationPackages_cl.setOnClickListener(v -> {
            if (isInstallationPkgClean) {
                installationPackages_iv.setImageResource(R.drawable.group_47);
                isInstallationPkgClean = false;
            } else {
                installationPackages_iv.setImageResource(R.drawable.ic_selected);
                isInstallationPkgClean = true;
            }
        });


        residualJunk_cl.setOnClickListener(v -> {
            if (isResidualJunkClean) {
                residualJunk_iv.setImageResource(R.drawable.group_47);
                isResidualJunkClean = false;
            } else {
                residualJunk_iv.setImageResource(R.drawable.ic_selected);
                isResidualJunkClean = true;
            }
        });

        dirPath = String.valueOf(Environment.getExternalStorageDirectory());
        utils = new Utils(this);


        junkFileCleanBtn_ll.setOnClickListener(v -> {
            if (isEFolderClean || isCacheJunkClean || isInstallationPkgClean || isResidualJunkClean) {
                junkCleaning();
                junkFileEmptyFolderClear_iv.setImageResource(R.drawable.group_47);
                residualJunk_iv.setImageResource(R.drawable.group_47);
                installationPackages_iv.setImageResource(R.drawable.group_47);
                cacheJunkClear_iv.setImageResource(R.drawable.group_47);
                isEFolderClean = false;
                isCacheJunkClean = false;
                isInstallationPkgClean = false;
                isResidualJunkClean = false;
            } else {
                Toast.makeText(this, "Please select item first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                pkg = utils.getAllPackages(dirPath);
            }
        });
     }


    public void lastView() {
        lottie_av.setVisibility(View.INVISIBLE);
        junkBinDone_iv.setVisibility(View.VISIBLE);
        junkBinDone_iv.setImageResource(R.drawable.ic_bin_sky_blue);
        trashCleanLast_tv.setText("CLEANING FINISHED");
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            cacheJunkBin_cl.setVisibility(View.INVISIBLE);
            junkBinDone_iv.setVisibility(View.INVISIBLE);
        }, 2000);
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

    public void junkCleaning() {
        String p = Environment.getExternalStorageDirectory().getAbsolutePath();
        cacheJunkBin_cl.setVisibility(View.VISIBLE);
        lottie_av.setVisibility(View.VISIBLE);
        trashCleanLast_tv.setText("CLEANING");

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanOrFinishFun(true, p);
            }
        },2000);

        startAnimation(100);



    }

    private void startAnimation(int setLevel) {
        ValueAnimator animator = ValueAnimator.ofInt(0, setLevel);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(2_000);
        animator.addUpdateListener(valueAnimator -> {

            int value = (int) valueAnimator.getAnimatedValue();
             if (value >= setLevel-5) {


                new Handler(Looper.getMainLooper())
                        .postDelayed(() -> {
                            lastView();
                        }, 3000);
            }
        });
        animator.start();
    }


}