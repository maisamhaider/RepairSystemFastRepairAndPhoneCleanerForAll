package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.StorageUtils;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ChargingLockedScreenAct extends AppCompatActivity implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private ProgressBar pbBattery;
    private TextView tvCharging, tvPercentage;
    private View vHead;
    LoadingDialog mLoadingDialog;
    private int selection = 0;
    private StorageUtils storageUtils;
    private Utils utils;
    private String dirPath;
    private ActivityManager am;
    private MediaPlayer mp;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView tvTime, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charging_locked_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        utils = new Utils(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = preferences.edit();

        mLoadingDialog = new LoadingDialog(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setShowWhenLocked(true);
        }

        ImageView ivJunkFile = findViewById(R.id.iv_junk_file);
        ImageView ivCpuCooler = findViewById(R.id.iv_cpu_cooler);
        ImageView ivBoostPhone = findViewById(R.id.iv_boost_phone);
        tvPercentage = findViewById(R.id.tv_percentage);
        tvCharging = findViewById(R.id.tv_charging);
        pbBattery = findViewById(R.id.pb_battery);
        vHead = findViewById(R.id.v_head);
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
        am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        storageUtils = new StorageUtils();
        dirPath = String.valueOf(Environment.getExternalStorageDirectory());

        tvTime.setText(getTime());
        tvDate.setText(getDate());

        ivJunkFile.setOnClickListener(v -> {
            selection = 1;
            new BackgroundTask().execute();

        });
        ivCpuCooler.setOnClickListener(v -> {
            selection = 2;
            new BackgroundTask().execute();
        });

        ivBoostPhone.setOnClickListener(v -> {
            selection = 3;
            new BackgroundTask().execute();
        });

        vHead.setBackground(getResources().getDrawable(R.drawable.s_bg_head));
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        startAnimation(60);
    }

    private void startAnimation(int setLevel) {
        ValueAnimator animator = ValueAnimator.ofInt(0, setLevel);
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(2_000);
        animator.addUpdateListener(valueAnimator -> {
            int value = (int) valueAnimator.getAnimatedValue();
            pbBattery.setProgress(value);
            String bValue = value + "%";
            tvPercentage.setText(bValue);
            if (value == 100) {
                playOrVibrate();
                vHead.setBackground(getResources().getDrawable(R.drawable.bg_head));
            }
        });


        animator.start();
    }


    private void playOrVibrate() {

        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if (preferences.getBoolean("FULL_CHARGED_ALARM", false)) {
            if (preferences.getBoolean("FULL_CHARGED_VIBRATE", false)) {

                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(2000);
            }

            if (preferences.getBoolean("FULL_CHARGED_SOUND", false)) {

                try {
                    mp = MediaPlayer.create(this, R.raw.notification_sound);
                    mp.prepareAsync();
                    mp.setLooping(false);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void cleanJunk() {
        storageUtils.deleteCache(getApplicationContext());
        storageUtils.deleteEmptyFolder(dirPath);
        List<CommonModel> pkg = utils.getAllPackages(dirPath);

        if (pkg.size() != 0) {
            for (int i = 0; i < pkg.size(); i++) {
                pkg.get(i).getPath();
            }
        }
    }

    public void cpuCooler() {
        for (int i = 0; i < utils.getSystemActiveApps().size(); i++) {
            am.killBackgroundProcesses(utils.getSystemActiveApps().get(i));
            Log.w("cpuCooler", "cpuCooler: " + utils.getSystemActiveApps().size());
        }
    }

    public void phoneBooster() {
        for (int i = 0; i < utils.getSystemActiveApps().size(); i++) {
            am.killBackgroundProcesses(utils.getSystemActiveApps().get(i));
        }
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int batterySource = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

            if (batterySource == 0) {
                tvCharging.setVisibility(View.GONE);
            } else {
                tvCharging.setVisibility(View.VISIBLE);
            }
            startAnimation(level);
        }
    };

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "Ring tone error", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        if (preferences.getBoolean("FULL_CHARGED_SOUND", false)) {
            mp.start();
            Toast.makeText(this, "Ring tone playing", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("StaticFieldLeak")
    class BackgroundTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (selection == 1) {
                cleanJunk();
            } else if (selection == 2) {
                cpuCooler();
            } else if (selection == 3) {
                phoneBooster();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLoadingDialog.dismiss();
            String msg = "";
            if (selection == 1) {
                msg = "Junk files is deleted successfully";
            } else if (selection == 2) {
                msg = "CPU Cool successfully";
            } else if (selection == 3) {
                msg = "Phone Boost successfully";
            }
            Toast.makeText(ChargingLockedScreenAct.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor.putLong("lock_charge_delay", Calendar.getInstance().getTimeInMillis()).commit();
        editor.putBoolean("first_lock_charge", false).commit();

        if (mp != null) {
            mp.release();
            mp = null;
        }
        if (mBatInfoReceiver != null) {
            unregisterReceiver(mBatInfoReceiver);
        }
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("h:mm a").format(calendar.getTime());
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("dd MMM yyyy").format(calendar.getTime());
    }

}