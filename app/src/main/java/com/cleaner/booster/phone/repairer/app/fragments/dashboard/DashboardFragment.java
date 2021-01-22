package com.cleaner.booster.phone.repairer.app.fragments.dashboard;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.CleanWhatsAppAct;
import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAct;
import com.cleaner.booster.phone.repairer.app.activities.InternetSpeedAct;
import com.cleaner.booster.phone.repairer.app.activities.RepairAct;
import com.cleaner.booster.phone.repairer.app.activities.SmartChargingAct;
import com.cleaner.booster.phone.repairer.app.fragments.BaseFragment;
import com.cleaner.booster.phone.repairer.app.permission.Permissions;
import com.cleaner.booster.phone.repairer.app.utils.Utils;
import com.cleaner.booster.phone.repairer.app.utils.customprogresschart.StackedHorizontalChart;
import com.cleaner.booster.phone.repairer.app.utils.customprogresschart.StackedHorizontalChartModel;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;

import hendrawd.storageutil.library.StorageUtil;


public class DashboardFragment extends BaseFragment
        implements View.OnClickListener {

    private Permissions permissions;
    private Utils utils;
    private View root;
    private TextView tvStorageInfo, tv_num;
    TextView cel_tv;
    TextView far_tv;

    Context context;


    private StackedHorizontalChart stackedHorizontalChart;

    private float totalSpan;
    private float photos;
    private float videos;
    private float other;
    private float audios;
    private float free;

    private ArrayList<StackedHorizontalChartModel> progressList;
    private StackedHorizontalChartModel progressItem;

    //    private Button b_optimize;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getContext();
        stackedHorizontalChart = root.findViewById(R.id.stackedHorChart);
        permissions = new Permissions(getContext());
        if (permissions.permission()) {

            init();
        }

        if (Build.VERSION_CODES.N < Build.VERSION.SDK_INT) {
            init();
        }
        return root;
    }

    public void init() {

        permissions.permission();
        utils = new Utils(getContext());


        tvStorageInfo = root.findViewById(R.id.tv_storage_info);
        tv_num = root.findViewById(R.id.tv_num);


        ConstraintLayout cleanWhatsApp_cl = root.findViewById(R.id.cleanWhatsApp_cl);
        ConstraintLayout smartCharge_cl = root.findViewById(R.id.smartCharge_cl);
        ConstraintLayout deepClean_cl = root.findViewById(R.id.deepClean_cl);
        ConstraintLayout speedTest_cl = root.findViewById(R.id.speedTest_cl);
        ConstraintLayout repair_cl = root.findViewById(R.id.repair_cl);
        cel_tv = root.findViewById(R.id.cel_tv);
        far_tv = root.findViewById(R.id.far_tv);

//        b_optimize.setOnClickListener(this);

        float temp1 = utils.cpuTemperature();

        cpuTemp(temp1, true);
        cel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpuTemp(temp1, true);

            }
        });
        far_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpuTemp(temp1, false);
            }
        });

        cleanWhatsApp_cl.setOnClickListener(this);
        smartCharge_cl.setOnClickListener(this);
        deepClean_cl.setOnClickListener(this);
        speedTest_cl.setOnClickListener(this);
        repair_cl.setOnClickListener(this);

        ramAndStorageFun();


    }

    private void initDataToSeekbar() {
        progressList = new ArrayList<>();
        // photos
        progressItem = new StackedHorizontalChartModel();
        progressItem.itemPercentage = ((photos / totalSpan) * 100);
        progressItem.filledColor = R.color.colorGrayDark;
        progressList.add(progressItem);

        // video
        progressItem = new StackedHorizontalChartModel();
        progressItem.itemPercentage = ((videos / totalSpan) * 100);
        Log.i("Mainactivity", progressItem.itemPercentage + "");
        progressItem.filledColor = R.color.material_red_400;
        progressList.add(progressItem);

        // audio span
        progressItem = new StackedHorizontalChartModel();
        progressItem.itemPercentage = (audios / totalSpan) * 100;
        progressItem.filledColor = R.color.colorPrimaryDark;
        progressList.add(progressItem);
        // app span
        progressItem = new StackedHorizontalChartModel();
        progressItem.itemPercentage = (other / totalSpan) * 100;
        progressItem.filledColor = R.color.colorYellow;
        progressList.add(progressItem);
        // greyspan
        progressItem = new StackedHorizontalChartModel();
        progressItem.itemPercentage = (free / totalSpan) * 100;
        progressItem.filledColor = R.color.colorBorderCL;
        progressList.add(progressItem);
        stackedHorizontalChart.init(progressList);
        stackedHorizontalChart.invalidate();
    }


    @SuppressLint("SetTextI18n")
    private void ramAndStorageFun() {

        TextView freeRam_tv = root.findViewById(R.id.freeRam_tv);
        TextView usedRam_tv = root.findViewById(R.id.usedRam_tv);
        TextView totalRam_tv = root.findViewById(R.id.totalRam_tv);

//for Ram
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        float totalRam1 = memoryInfo.totalMem;
        float freeRam1 = memoryInfo.availMem;
        float usedRam = totalRam1 - freeRam1;
//        ramPercent_tv.setText( String.format( "%.1f",utils.getPercentage( totalRam1,usedRam ) )+"%" );

//        cpbRam.setProgress(usedRam, totalRam1);
        double[] value = new double[]{freeRam1, usedRam};

        pieChart(value);
        setAnimation(freeRam_tv, freeRam1);
        setAnimation(usedRam_tv, usedRam);
        setAnimation(totalRam_tv, totalRam1);


//        tvRamInfo.setText(String.format(utils.getCalculatedDataSize(usedRam) + "
//        " + utils.getCalculatedDataSize(totalRam1)));

        //for Storage
        float totalStorageBytes, availableStorageBytes, usedStorageBytes;

        totalStorageBytes = utils.getTotalStorage();
        availableStorageBytes = utils.getAvailableStorage();

        usedStorageBytes = totalStorageBytes - availableStorageBytes;
        ArrayList<String> strings = new ArrayList<>();
        strings = utils.getExternalMounts();
        String[] externalStoragePaths = StorageUtil.getStorageDirectories(context);
        float sdTotal = 0;
        float sdFree = 0;
        float sdUsed = 0;
        if (!strings.isEmpty()) {
            sdTotal = utils.getTotalStorage(externalStoragePaths[0]+"Android/");
            sdFree = utils.getAvailableStorage(externalStoragePaths[0]+"Android/");
            sdUsed = sdTotal - sdFree;

        }

        int total = (int)utils.getCalculatedDataSizeFloat(totalStorageBytes);
        int sd = (int)utils.getCalculatedDataSizeFloat(sdTotal);

        int sdUsage2 = (int)utils.getCalculatedDataSizeFloat(sdUsed);
        int deviceUsage = (int)utils.getCalculatedDataSizeFloat(usedStorageBytes);

        String totalString =android.text.format.Formatter.formatFileSize(getContext(),
                (long) usedStorageBytes + (long) sdUsed);
        if (total<2)
        {
            if ( !strings.isEmpty())
            {
                int t =  2+ utils.memoryFun(sd);

                tvStorageInfo.setText(totalString+ "   " + t +" GB");
            }
            else
            {
                tvStorageInfo.setText(totalString + "   " + "2 GB");
            }
        }
        if (total>2 && total <4)
        {
            if ( !strings.isEmpty())
        {
            int t =  4+ utils.memoryFun(sd);

            tvStorageInfo.setText(totalString + "   " + t+" GB");
        }
        else
        {
            tvStorageInfo.setText(totalString + "   " + "4 GB");
        }

        }
        else if (total>4 && total <8)
        {
            if ( !strings.isEmpty())
            {
                int t =  8+ utils.memoryFun(sd);

                tvStorageInfo.setText(totalString + "   " + t+" GB");
            }
            else
            {
                tvStorageInfo.setText(totalString + "   " + "8 GB");
            }
        }
        else
        if (total>8 && total <16)
        {

            if ( !strings.isEmpty())
            {
                int t =  16+ utils.memoryFun(sd);

                tvStorageInfo.setText(totalString + "   " + t+" GB");
            }
            else
            {
                tvStorageInfo.setText(totalString + "   " + "16 GB");
            }
        }
        else
            if (total>16 && total <32)
            {

                if ( !strings.isEmpty())
                {

                    int t =  32+ utils.memoryFun(sd);

                    tvStorageInfo.setText(totalString + "   " +t+ " GB");
                }
                else
                {
                    tvStorageInfo.setText(totalString + "   " + "32 GB");
                }
            }
            else
            if (total>32 && total <64)
            {

                if ( !strings.isEmpty())
                {
                    int t =  64+ utils.memoryFun(sd);
                    tvStorageInfo.setText(totalString + "   " +t+" GB");
                }
                else
                {          tvStorageInfo.setText(totalString + "   " + "64 GB");

                }
            } else if (total > 64 && total < 128) {

                if ( !strings.isEmpty())
                {
                    int t =  128+ utils.memoryFun(sd);

                    tvStorageInfo.setText(totalString + "   " + t + " GB");
                }
                else
                {

                    tvStorageInfo.setText(totalString + "   " + "128 GB");
                }
            }else if (total > 128 && total < 256) {

                if ( !strings.isEmpty())
                {
                    int t =  256+ utils.memoryFun(sd);

                    tvStorageInfo.setText(totalString + "   " + t +" GB");
                }
                else
                {
                    tvStorageInfo.setText(totalString + "   " + "256 GB");
                }
            }
            else if (total > 256 && total < 512) {

                  if ( !strings.isEmpty())
                {
                    int t =  512+ utils.memoryFun(sd);

                    tvStorageInfo.setText(totalString + "   " + t+" GB");
                }
                else
                {
                    tvStorageInfo.setText( totalString+ "   " + "512 GB");
                }
            }else if (total > 512 && total < 1024) {

                if ( !strings.isEmpty())
                {
                    int t =  1024+ utils.memoryFun(sd);

                    tvStorageInfo.setText(totalString + "   " + t+" GB");
                }
                else
                {
                    tvStorageInfo.setText(totalString+ "   " + "1024 GB");
                }
            }


        totalSpan = (totalStorageBytes + sdTotal)  ;
        photos = utils.getAllIAAsSize("images") ;
        videos = utils.getAllIAAsSize("videos") ;
        audios = utils.getAllIAAsSize("audios")  ;

        float kbUsage = (usedStorageBytes + sdUsed);
        other =  (kbUsage   - (photos + videos + audios));
        free = (availableStorageBytes+ sdFree);

        Log.d("totalSpan",utils.getCalculatedDataSize(totalSpan));
        Log.d("photos",utils.getCalculatedDataSize(photos));
        Log.d("videos",utils.getCalculatedDataSize(videos));
        Log.d("audios",utils.getCalculatedDataSize(audios));
        Log.d("other",utils.getCalculatedDataSize(other));
        Log.d("free",utils.getCalculatedDataSize(free));
        initDataToSeekbar();
    }

    public void setAnimation(TextView view, float inValue) {
        ValueAnimator storageAnimator = ValueAnimator.ofInt(0, (int) inValue);
        storageAnimator.setInterpolator(new LinearInterpolator());
        storageAnimator.setStartDelay(0);
        storageAnimator.setDuration(1_500);
        storageAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int intVal = (int) valueAnimator.getAnimatedValue();

                view.setText(utils.getCalculatedDataSize(inValue)
                        /* Formatter.formatFileSize(getContext(), intVal)*/);
            }
        });
        storageAnimator.start();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new RepairAct());
                } else
                    Toast.makeText(getContext(), "Permission is not granted ",
                            Toast.LENGTH_SHORT).show();
                break;

            case R.id.cleanWhatsApp_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new CleanWhatsAppAct());
                } else
                    Toast.makeText(getContext(), "Permission is not granted ",
                            Toast.LENGTH_SHORT).show();
                break;

            case R.id.smartCharge_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new SmartChargingAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.deepClean_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new DeepCleanAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.speedTest_cl:
                if (permissions.permission()) {
                    sNewActivityAds(new InternetSpeedAct());
                } else {
                    Toast.makeText(getContext(), "Permission is not granted ",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void pieChart(double[] VALUES) {
        int[] colorsArray = new int[]{getResources().getColor(R.color.colorPrimaryDark), Color.BLACK};

        CategorySeries series = new CategorySeries("");

        DefaultRenderer renderer = new DefaultRenderer();


        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setClickEnabled(false);
        renderer.setInScroll(false);
        renderer.setPanEnabled(false);
        renderer.setShowLegend(false);
        renderer.setShowLabels(false);

        for (double value : VALUES) {
            series.add(value);
            SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer();
            renderer1.setColor(colorsArray[(series.getItemCount() - 1) % colorsArray.length]);
            renderer.addSeriesRenderer(renderer1);
        }


        LinearLayout layout = root.findViewById(R.id.chartPie);


        GraphicalView graphicalView = ChartFactory.getPieChartView(getContext(), series, renderer);
        layout.addView(graphicalView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        graphicalView.repaint();

    }

    @SuppressLint("DefaultLocale")
    public void cpuTemp(float temp, boolean isCal) {
        TextView textView91 = root.findViewById(R.id.textView91);

        tv_num.setText(String.valueOf((int) utils.cpuTemperature()));

        if (isCal) {
            tv_num.setText(String.valueOf((int) utils.cpuTemperature()));
            textView91.setText("°C");
            far_tv.setBackground(ResourcesCompat
                    .getDrawable(getResources(), R.drawable.left_curved_background_bg, null));
            cel_tv.setBackground(ResourcesCompat
                    .getDrawable(getResources(), R.drawable.right_curved_primary_bg, null));
        } else {
            tv_num.setText(String.valueOf((int) utils.getFahrenheit(temp)));
            textView91.setText("°F");
            far_tv.setBackground(ResourcesCompat
                    .getDrawable(getResources(), R.drawable.left_curved_primary_bg, null));
            cel_tv.setBackground(ResourcesCompat
                    .getDrawable(getResources(), R.drawable.right_curved_background_bg, null));
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        context = getContext();


    }

    @Override
    public void onPause() {
        super.onPause();

    }


}