package com.cleaner.booster.phone.repairer.app.fragments.dashboard;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.BatterySavingAct;
import com.cleaner.booster.phone.repairer.app.activities.CleanWhatsAppAct;
import com.cleaner.booster.phone.repairer.app.activities.CpuCooler;
import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAct;
import com.cleaner.booster.phone.repairer.app.activities.InternetSpeedAct;
import com.cleaner.booster.phone.repairer.app.activities.JunkFilesAct;
import com.cleaner.booster.phone.repairer.app.activities.PhoneBoostAct;
import com.cleaner.booster.phone.repairer.app.activities.RepairAct;
import com.cleaner.booster.phone.repairer.app.activities.SmartChargingAct;
import com.cleaner.booster.phone.repairer.app.activities.UnInstallAppAct;
import com.cleaner.booster.phone.repairer.app.activities.WhatsAppStatusAct;
import com.cleaner.booster.phone.repairer.app.fragments.BaseFragment;
import com.cleaner.booster.phone.repairer.app.permission.Permissions;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.Random;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;


public class DashboardFragment extends BaseFragment implements View.OnClickListener {

    private Permissions permissions;
    private Utils utils;
    private View root;
    private CircularProgressIndicator cpbRam, cpbStorage;
    private TextView tvRamInfo, tvStorageInfo, tv_num;
//    private Button b_optimize;

    private ImageView ivAppCleanup;
    private ImageView ivPowerSaving;
    private ImageView ivStatusSaver;
    private ImageView ivJunkFile;
    private ImageView ivCpuCooler;
    private ImageView ivBoostPhone;

    private TextView appCleanupTv;
    private TextView powerSavingTv;
    private TextView statusSaverTv;
    private TextView junk_fileTv;
    private TextView cpu_coolerTv;
    private TextView boost_phoneTv;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init();
        return root;
    }
    public void init() {
        permissions = new Permissions(getContext());
        permissions.permission();
        utils = new Utils(getContext());
        cpbRam = root.findViewById(R.id.pgb_ram);
        cpbStorage = root.findViewById(R.id.pgb_storage);
//        b_optimize = root.findViewById(R.id.b_optimize);

        tvRamInfo = root.findViewById(R.id.tv_ram_info);
        tvStorageInfo = root.findViewById(R.id.tv_storage_info);
        tv_num = root.findViewById(R.id.tv_num);

        ivAppCleanup = root.findViewById(R.id.iv_app_cleanup);
        ivPowerSaving = root.findViewById(R.id.iv_power_saving);
        ivStatusSaver = root.findViewById(R.id.iv_status_saver);
        ivJunkFile = root.findViewById(R.id.iv_junk_file);
        ivCpuCooler = root.findViewById(R.id.iv_cpu_cooler);
        ivBoostPhone = root.findViewById(R.id.iv_boost_phone);

        appCleanupTv = root.findViewById(R.id.app_cleanup_tv);
        powerSavingTv = root.findViewById(R.id.power_saving_tv);
        statusSaverTv = root.findViewById(R.id.status_saver_tv);
        junk_fileTv = root.findViewById(R.id.junk_file_tv);
        cpu_coolerTv = root.findViewById(R.id.cpu_cooler_tv);
        boost_phoneTv = root.findViewById(R.id.boost_phone_tv);


        ConstraintLayout cleanWhatsApp_cl = root.findViewById(R.id.cleanWhatsApp_cl);
        ConstraintLayout smartCharge_cl = root.findViewById(R.id.smartCharge_cl);
        ConstraintLayout deepClean_cl = root.findViewById(R.id.deepClean_cl);
        ConstraintLayout speedTest_cl = root.findViewById(R.id.speedTest_cl);
        ConstraintLayout repair_cl = root.findViewById(R.id.repair_cl);

//        b_optimize.setOnClickListener(this);

        @SuppressLint("DefaultLocale")
        String temp1 = String.format("%d", (int) utils.cpuTemperature());
        Random random = new Random();

        tv_num.setText( String.valueOf(random.nextInt(40-10)));
         disable();

        ivAppCleanup.setOnClickListener(this);
        ivPowerSaving.setOnClickListener(this);
        ivStatusSaver.setOnClickListener(this);
        ivJunkFile.setOnClickListener(this);
        ivCpuCooler.setOnClickListener(this);
        ivBoostPhone.setOnClickListener(this);

        cleanWhatsApp_cl.setOnClickListener(this);
        smartCharge_cl.setOnClickListener(this);
        deepClean_cl.setOnClickListener(this);
        speedTest_cl.setOnClickListener(this);
        repair_cl.setOnClickListener(this);

        ramAndStorageFun();

    }


    private void ramAndStorageFun() {

        TextView storageProPercent_tv = root.findViewById(R.id.storageProPercent_tv);
        TextView ramProPercent_tv = root.findViewById(R.id.ramProPercent_tv);

//for Ram
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        float totalRam1 = memoryInfo.totalMem;
        float freeRam1 = memoryInfo.availMem;
        float usedRam = totalRam1 - freeRam1;
//        ramPercent_tv.setText( String.format( "%.1f",utils.getPercentage( totalRam1,usedRam ) )+"%" );
        cpbRam.setProgress(usedRam, totalRam1);
        tvRamInfo.setText(String.format(utils.getCalculatedDataSize(usedRam) + "   " + utils.getCalculatedDataSize(totalRam1)));

        // animation start
        ValueAnimator ramAnimator = ValueAnimator.ofInt(0, (int) utils.getPercentage(totalRam1, usedRam));
        ramAnimator.setInterpolator(new LinearInterpolator());
        ramAnimator.setStartDelay(0);
        ramAnimator.setDuration(1_500);
        ramAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                ramProPercent_tv.setText(value + "%");
            }
        });
        ramAnimator.start();
        ramProPercent_tv.setText(utils.getPercentage(totalRam1, usedRam) + "%");
        // animation end

        //for Storage
        float totalStorageBytes, availableStorageBytes, usedStorageBytes;

        totalStorageBytes = utils.getTotalStorage();
        availableStorageBytes = utils.getAvailableStorage();

        usedStorageBytes = totalStorageBytes - availableStorageBytes;
        cpbStorage.setProgress(usedStorageBytes, totalStorageBytes);
        tvStorageInfo.setText(utils.getCalculatedDataSize(usedStorageBytes) + "   " + utils.getCalculatedDataSize(totalStorageBytes));
        // animation start
        ValueAnimator storageAnimator = ValueAnimator.ofInt(0, (int) utils.getPercentage(totalStorageBytes, usedStorageBytes));
        storageAnimator.setInterpolator(new LinearInterpolator());
        storageAnimator.setStartDelay(0);
        storageAnimator.setDuration(1_500);
        storageAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                storageProPercent_tv.setText(value + "%");
            }
        });
        storageAnimator.start();
        // animation end
        storageProPercent_tv.setText(utils.getPercentage(totalStorageBytes, usedStorageBytes) + "%");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new RepairAct());
                } else
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cleanWhatsApp_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new CleanWhatsAppAct());
                } else
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_junk_file:
                changeFun(ivJunkFile);
                if (permissions.permission()) {
                    sNewActivityAds(new JunkFilesAct());
                } else
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.iv_cpu_cooler:
                changeFun(ivCpuCooler);
                if (permissions.permission()) {
                    sNewActivityAds(new CpuCooler());

                } else
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_boost_phone:
                changeFun(ivBoostPhone);
                if (permissions.permission()) {
                    sNewActivityAds(new PhoneBoostAct());
                } else
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_power_saving:
                changeFun(ivPowerSaving);
                if (permissions.permission()) {
                    sNewActivityAds(new BatterySavingAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_app_cleanup:
                changeFun(ivAppCleanup);
                if (permissions.permission()) {
                    sNewActivityAds(new UnInstallAppAct());

                } else {
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_status_saver:
                changeFun(ivStatusSaver);
                if (permissions.permission()) {
                    sNewActivityAds(new WhatsAppStatusAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.smartCharge_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new SmartChargingAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.deepClean_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new DeepCleanAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.speedTest_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new InternetSpeedAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void changeFun(View view) {
        switch (view.getId()) {
            case R.id.iv_junk_file:

                ivJunkFile.setImageResource(R.drawable.ic_select_junk_files);
                ivCpuCooler.setImageResource(R.drawable.ic_cpu_cooler);
                ivBoostPhone.setImageResource(R.drawable.ic_boost_phone);
                ivPowerSaving.setImageResource(R.drawable.ic_power_saving);
                ivAppCleanup.setImageResource(R.drawable.ic_app_cleanup);
                ivStatusSaver.setImageResource(R.drawable.ic_status_saver);

                junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextOne));
                cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextThree));

                break;
            case R.id.iv_cpu_cooler:
                ivJunkFile.setImageResource(R.drawable.ic_junk_files);
                ivCpuCooler.setImageResource(R.drawable.ic_select_cpu_cooler);
                ivBoostPhone.setImageResource(R.drawable.ic_boost_phone);
                ivAppCleanup.setImageResource(R.drawable.ic_app_cleanup);
                ivPowerSaving.setImageResource(R.drawable.ic_power_saving);
                ivStatusSaver.setImageResource(R.drawable.ic_status_saver);

                junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextOne));
                boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextThree));

                break;
            case R.id.iv_boost_phone:
                ivJunkFile.setImageResource(R.drawable.ic_junk_files);
                ivCpuCooler.setImageResource(R.drawable.ic_cpu_cooler);
                ivBoostPhone.setImageResource(R.drawable.ic_select_phone_booster);
                ivAppCleanup.setImageResource(R.drawable.ic_app_cleanup);
                ivPowerSaving.setImageResource(R.drawable.ic_power_saving);
                ivStatusSaver.setImageResource(R.drawable.ic_status_saver);

                junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextOne));
                appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                break;
            case R.id.iv_app_cleanup:
                ivJunkFile.setImageResource(R.drawable.ic_junk_files);
                ivCpuCooler.setImageResource(R.drawable.ic_cpu_cooler);
                ivBoostPhone.setImageResource(R.drawable.ic_boost_phone);
                ivAppCleanup.setImageResource(R.drawable.ic_select_app_cleanup);
                ivPowerSaving.setImageResource(R.drawable.ic_power_saving);
                ivStatusSaver.setImageResource(R.drawable.ic_status_saver);

                junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextOne));
                powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                break;

            case R.id.iv_power_saving:
                ivJunkFile.setImageResource(R.drawable.ic_junk_files);
                ivCpuCooler.setImageResource(R.drawable.ic_cpu_cooler);
                ivBoostPhone.setImageResource(R.drawable.ic_boost_phone);
                ivAppCleanup.setImageResource(R.drawable.ic_app_cleanup);
                ivPowerSaving.setImageResource(R.drawable.ic_select_power_saving);
                ivStatusSaver.setImageResource(R.drawable.ic_status_saver);

                junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextOne));
                statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                break;
            case R.id.iv_status_saver:
                ivJunkFile.setImageResource(R.drawable.ic_junk_files);
                ivCpuCooler.setImageResource(R.drawable.ic_cpu_cooler);
                ivBoostPhone.setImageResource(R.drawable.ic_boost_phone);
                ivPowerSaving.setImageResource(R.drawable.ic_power_saving);
                ivAppCleanup.setImageResource(R.drawable.ic_app_cleanup);
                ivStatusSaver.setImageResource(R.drawable.ic_select_status_saver);

                junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextThree));
                statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextOne));
                break;
        }
    }

    public void disable() {
        ivJunkFile.setImageResource(R.drawable.ic_junk_files);
        ivCpuCooler.setImageResource(R.drawable.ic_cpu_cooler);
        ivBoostPhone.setImageResource(R.drawable.ic_boost_phone);
        ivPowerSaving.setImageResource(R.drawable.ic_power_saving);
        ivAppCleanup.setImageResource(R.drawable.ic_app_cleanup);
        ivStatusSaver.setImageResource(R.drawable.ic_status_saver);

        junk_fileTv.setTextColor(getResources().getColor(R.color.colorTextThree));
        cpu_coolerTv.setTextColor(getResources().getColor(R.color.colorTextThree));
        boost_phoneTv.setTextColor(getResources().getColor(R.color.colorTextThree));
        powerSavingTv.setTextColor(getResources().getColor(R.color.colorTextThree));
        appCleanupTv.setTextColor(getResources().getColor(R.color.colorTextThree));
        statusSaverTv.setTextColor(getResources().getColor(R.color.colorTextThree));
    }

}