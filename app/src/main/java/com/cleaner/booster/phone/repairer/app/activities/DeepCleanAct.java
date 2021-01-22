package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.permission.Permissions;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

public class DeepCleanAct extends BaseActivity {

    private Permissions permissions;
    private ConstraintLayout deepCleanWhatsApp_cl, deeCleanImages_cl, deeCleanVideos_cl, deeCleanAudios_cl, deeCleanAppData_cl, deepCleanLargeFiles_cl, deeCleanInstallationPkg_cl;
    private ImageView deepCleanWhatsAppNext_iv, deepCleanImagesNext_iv, deepCleanVideosNext_iv, deepCleanAudiosNext_iv,deepCleanLargeFilesNext_iv, deepCleanAppDataNext_iv, deepCleanInstallationPkgsNext_iv;
     TextView deepCleanWhatsAppDataSize_tv, deepCleanImagesSize_tv, deepCleanVideosSize_tv,
            deepCleanAudiosDataSize_tv,deepCleanAppDataSize_tv, deepCleanLargeFileSize_tv, deepCleanInstallationPkgseSize_tv
            ,deepCleanDetail_tv,deepCleanMainTotalDataSize_tv;
    ProgressBar deepClean_pb;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_clean);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        permissions = new Permissions(this);
        permissions.permission();
        utils = new Utils(this);

        deepCleanWhatsApp_cl = findViewById(R.id.deepCleanWhatsApp_cl);
        deeCleanImages_cl = findViewById(R.id.deeCleanImages_cl);
        deeCleanVideos_cl = findViewById(R.id.deeCleanVideos_cl);
        deeCleanAudios_cl = findViewById(R.id.deeCleanAudios_cl);
        deepCleanLargeFiles_cl = findViewById(R.id.deeCleanLargeFiles_cl);
        deeCleanAppData_cl = findViewById(R.id.deeCleanAppData_cl);
        deeCleanInstallationPkg_cl = findViewById(R.id.deeCleanInstallationPkg_cl);

        deepCleanWhatsAppNext_iv = findViewById(R.id.deepCleanWhatsAppNext_iv);
        deepCleanImagesNext_iv = findViewById(R.id.deepCleanImagesNext_iv);
        deepCleanVideosNext_iv = findViewById(R.id.deepCleanVideosNext_iv);
        deepCleanAudiosNext_iv = findViewById(R.id.deepCleanAudiosNext_iv);
        deepCleanAppDataNext_iv = findViewById(R.id.deeCleanAppDataNext_iv);
        deepCleanLargeFilesNext_iv = findViewById(R.id.deepCleanLargeFilesNext_iv);
        deepCleanInstallationPkgsNext_iv = findViewById(R.id.deepCleanInstallationPkgsNext_iv);





        deepCleanMainTotalDataSize_tv = findViewById(R.id.deepCleanMainTotalDataSize_tv);
         deepCleanImagesSize_tv = findViewById(R.id.deepCleanImagesSize_tv);
        deepCleanVideosSize_tv = findViewById(R.id.deepCleanVideosSize_tv);
        deepCleanAudiosDataSize_tv = findViewById(R.id.deepCleanAudiosDataSize_tv);
        deepCleanLargeFileSize_tv = findViewById(R.id.deepCleanLargeFileSize_tv);
        deepCleanInstallationPkgseSize_tv = findViewById(R.id.deepCleanInstallationPkgseSize_tv);
        deepCleanWhatsAppDataSize_tv = findViewById(R.id.deepCleanWhatsAppDataSize_tv);
         deepCleanDetail_tv = findViewById(R.id.deepCleanDetail_tv);
        deepCleanAppDataSize_tv = findViewById(R.id.deepCleanAppDataSize_tv);


        deepClean_pb= findViewById(R.id.deepClean_pb);


        initIntents();
        new DeepCleanerTask().execute();

    }

    @SuppressLint("StaticFieldLeak")
    class DeepCleanerTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
             return null;
        }

        @Override
        protected void onPostExecute(String s) {





            float bUDataSize = utils.getAllSize(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Databases");
            float cHDataSize = utils.getAllSize(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Backups");
            float whatsAppAudioSize = utils.getAllSize(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Audio");
            float whatsAppVideoSize = utils.getAllSize(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Video");
            float whatsAppDocumentsSize = utils.getAllSize(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Documents");
            float whatsAppImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Images");

            float facebookSavedImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Facebook");
            float messengerSavedImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Messenger");
            float messengerSavedVideosSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Messenger");
            float instagramSavedImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Instagram");
            float instagramSavedVideosSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Instagram");

            float whatsAppDataSize = bUDataSize + cHDataSize + whatsAppAudioSize + whatsAppVideoSize + whatsAppDocumentsSize + whatsAppImagesSize;
            float imagesSize = utils.getAllIAAsSize("images");
            float VideosSize = utils.getAllIAAsSize("videos");
            float audiosSize = utils.getAllIAAsSize("audios");
            float docSize = utils.getAllDocSize(String.valueOf( Environment.getExternalStorageDirectory()));
            float pkgSize = utils.getAllPkgsSize(String.valueOf(Environment.getExternalStorageDirectory()));

            float totalSize = whatsAppDataSize+imagesSize+VideosSize+audiosSize+docSize+pkgSize;
            int size = utils.getCalculatedDataSize(totalSize).length();
            String dataPrefix;


             deepCleanImagesSize_tv.setText(utils.getCalculatedDataSize(imagesSize));
            deepCleanVideosSize_tv.setText(utils.getCalculatedDataSize(VideosSize));
            deepCleanAudiosDataSize_tv.setText(utils.getCalculatedDataSize(audiosSize));
            deepCleanLargeFileSize_tv.setText(utils.getCalculatedDataSize(docSize));
            deepCleanInstallationPkgseSize_tv.setText(utils.getCalculatedDataSize(pkgSize));
            deepCleanWhatsAppDataSize_tv.setText(utils.getCalculatedDataSize(whatsAppDataSize));
            deepCleanAppDataSize_tv.setText(utils.getCalculatedDataSize(facebookSavedImagesSize+messengerSavedImagesSize+messengerSavedVideosSize+instagramSavedImagesSize+instagramSavedVideosSize));

            deepCleanWhatsApp_cl.setVisibility(View.GONE);
            deeCleanImages_cl.setVisibility(View.GONE);
            deeCleanVideos_cl.setVisibility(View.GONE);
            deeCleanAudios_cl.setVisibility(View.GONE);
            deeCleanAppData_cl.setVisibility(View.GONE);
            deeCleanInstallationPkg_cl.setVisibility(View.GONE);


            int totalStorage = (int) utils.getTotalStorage();// utils.getPercentage(,totalSize);
            ValueAnimator animator = ValueAnimator.ofInt(0, (int) utils.getCalculatedDataSizeFloat(totalSize));
            animator.setInterpolator(new LinearInterpolator());
            animator.setStartDelay(0);
            animator.setDuration(3_000);
            animator.addUpdateListener(valueAnimator -> {
                int value = (int) valueAnimator.getAnimatedValue();
                deepCleanMainTotalDataSize_tv.setText(String.valueOf(value));
                if (value == (int) utils.getCalculatedDataSizeFloat(totalSize))
                {
                    deepCleanMainTotalDataSize_tv.setText( utils.getCalculatedDataSize(totalSize));
                }
            });
            animator.start();

             deepClean_pb.setMax(100);
            ValueAnimator animatorText = ValueAnimator.ofInt(0, 100);
            animatorText.setInterpolator(new LinearInterpolator());
            animatorText.setStartDelay(0);
            animatorText.setDuration(3_000);

            animatorText.addUpdateListener(valueAnimator -> {
                int value = (int) valueAnimator.getAnimatedValue();
                if (value==20)
                {
                    deepCleanWhatsApp_cl.setVisibility(View.VISIBLE);
                }
                if (value==40)
                {
                    deeCleanImages_cl.setVisibility(View.VISIBLE);
                }
                if (value==50)
                {
                    deeCleanVideos_cl.setVisibility(View.VISIBLE);
                }
                if (value==60)
                {
                    deeCleanAudios_cl.setVisibility(View.VISIBLE);
                }
                if (value==70)
                {
                    deepCleanLargeFiles_cl.setVisibility(View.VISIBLE);
                }
                if (value==80)
                {
                    deeCleanAppData_cl.setVisibility(View.VISIBLE);
                }
                if (value==100)
                {
                    deeCleanInstallationPkg_cl.setVisibility(View.VISIBLE);
                    deepCleanDetail_tv.setText("Scan is completed");
                    deepClean_pb.setVisibility(View.INVISIBLE);

                }
                deepClean_pb.setProgress(value);


            });
            animatorText.start();

            super.onPostExecute(s);
        }
    }

    private void intentFun(Class<?> cls) {
        Intent intent = new Intent(DeepCleanAct.this, cls);
        intent.putExtra("isSend", false);
        startActivity(intent);

    }
    private void initIntents()
    {
        // Constraints
        deepCleanWhatsApp_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                sNewActivityAds(new CleanWhatsAppAct(),false);
             }
        });
        deeCleanImages_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAllImagesAct.class);
            }
        });
        deeCleanVideos_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAllVideosAct.class);
            }
        });
        deeCleanAudios_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                 sNewActivityAds(new DeepCleanAllAudiosAct(),false);

            }
        });
        deeCleanAppData_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAppDataAct.class);
            }
        });

        deepCleanLargeFiles_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAllDocsAct.class);
            }
        });
        deeCleanInstallationPkg_cl.setOnClickListener(v -> {
            if (permissions.permission()) {
                 sNewActivityAds(new DeepCleanAllPackagesAct(),false);

            }
        });

        //deepClean Next imageViews

        deepCleanWhatsAppNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(CleanWhatsAppAct.class);
            }
        });
        deepCleanImagesNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                 sNewActivityAds(new DeepCleanAllImagesAct(),false);

            }
        });
        deepCleanVideosNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                 sNewActivityAds(new DeepCleanAllVideosAct(),false);
            }
        });
        deepCleanAudiosNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                 sNewActivityAds(new DeepCleanAllAudiosAct(),false);

            }
        });
        deepCleanAppDataNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAppDataAct.class);
            }
        });
        deepCleanLargeFilesNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAllDocsAct.class);
                sNewActivityAds(new DeepCleanAllDocsAct(),false);

            }
        });
        deepCleanInstallationPkgsNext_iv.setOnClickListener(v -> {
            if (permissions.permission()) {
                intentFun(DeepCleanAllPackagesAct.class);
            }
        });
    }
}