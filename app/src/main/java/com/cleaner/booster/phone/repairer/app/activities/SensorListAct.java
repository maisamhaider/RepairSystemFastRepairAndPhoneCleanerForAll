package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.R;

import java.util.List;

public class SensorListAct extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensorlist);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LinearLayout llSensor = findViewById(R.id.ll_sensor);
//        TextView mSensorAvailables  = findViewById(R.id.);

        // Get the SensorManager
        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // List of Sensors Available
        List<Sensor> mSensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // Print how may Sensors are there
//        mSensorsTot.setText(mSensorList.size()+" "+this.getString(R.string.sensors)+"!");

        // Print each Sensor available using sSensList as the String to be printed
        String sSensList = new String("");
        Sensor tmp;
        int x, i;
        for (i = 0; i < mSensorList.size(); i++) {

            tmp = mSensorList.get(i);
//            sSensList = " " + sSensList + tmp.getName() + "\n"; // Add the sensor name to the string of sensors available
//            viewArrayList = new ArrayList<>(mSensorList.size());
                View view = LayoutInflater.from(this).inflate(R.layout.sensor_item, null);
            LinearLayout.LayoutParams params = new
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,10);
            view.setLayoutParams(params);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setText(tmp.getName());
                llSensor.addView(view);

        }
    }
}