package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;

public class OnImageVideoAct extends AppCompatActivity {

    VideoView videoPlayer_vv;
    ImageView myImage_iv1, backFromImageVideo_iv, saveImageVideo_tv, shareImageVideo_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_image_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final Utils utils = new Utils(this);

        videoPlayer_vv = findViewById(R.id.videoPlayer_vv);
        myImage_iv1 = findViewById(R.id.myImage_iv1);
        backFromImageVideo_iv = findViewById(R.id.backFromImageVideo_iv);
        saveImageVideo_tv = findViewById(R.id.saveImageVideo_tv);
        shareImageVideo_iv = findViewById(R.id.shareImageVideo_iv);

        Intent intent = getIntent();
        final String data = intent.getStringExtra("imageOrVideoPath");
        boolean isSaved = intent.getBooleanExtra("isSaved", false);

        if (isSaved) {
            saveImageVideo_tv.setVisibility(View.GONE);
         }

        if (data.endsWith("mp4")) {

            videoPlayer_vv.setVideoPath(data);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoPlayer_vv);
            mc.setMediaPlayer(videoPlayer_vv);
            videoPlayer_vv.setMediaController(mc);

            videoPlayer_vv.setMediaController(mc);
            videoPlayer_vv.start();

        } else {

            Uri image = Uri.parse(data);
            myImage_iv1.setImageURI(image);

        }

        backFromImageVideo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        saveImageVideo_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.copyFileOrDirectory(data);
            }
        });
        shareImageVideo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data.endsWith("mp4")) {
                    Uri uriImage = Uri.parse(data);
                    ArrayList<Uri> oneImageArrayList = new ArrayList<>();
                    oneImageArrayList.add(uriImage);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, oneImageArrayList);
                    sendIntent.setType("video/*");

                    startActivity(Intent.createChooser(sendIntent, "Share Video to"));
                    startActivity(sendIntent);

                } else {

                    Uri uriImage = Uri.parse(data);
                    ArrayList<Uri> oneImageArrayList = new ArrayList<>();
                    oneImageArrayList.add(uriImage);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, oneImageArrayList);
                    sendIntent.setType("image/*");

                    startActivity(Intent.createChooser(sendIntent, "Share Image to"));
                    startActivity(sendIntent);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoPlayer_vv.isPlaying()) {
            videoPlayer_vv.pause();
        }
    }
}