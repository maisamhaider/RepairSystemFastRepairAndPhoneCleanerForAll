package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.R;

import java.io.IOException;
import java.io.InputStream;


public class ProcessorDetailAct extends AppCompatActivity {

    TextView textView ;
    ProcessBuilder processBuilder;
    String holder = "";
    String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
    InputStream inputStream;
    Process process ;
    byte[] byteArray;
    private int counter=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor_detail);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );

        LinearLayout llSensor = findViewById(R.id.ll_sensor);

        byteArray = new byte[1024];
        try{
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while(inputStream.read(byteArray) != -1){
                holder = holder + new String(byteArray);
            }
            inputStream.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
        String[] infoList = holder.split("\n");
        for (int i = 0; i < infoList.length; i++) {
            if (infoList[i].contains("Hardware")){
                counter = i+1;
                break;
            }
        }


        for (int i = 0; i < infoList.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.processor_item, null);
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,10);
            view.setLayoutParams(params);

            TextView tvTitle = view.findViewById(R.id.tv_title);
            TextView tvDesc = view.findViewById(R.id.tv_desc);

            try {
                String[] titleDetail = infoList[i].split(":");
                tvTitle.setText(titleDetail[0]);
                tvDesc.setText(titleDetail[1]);
                llSensor.addView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}