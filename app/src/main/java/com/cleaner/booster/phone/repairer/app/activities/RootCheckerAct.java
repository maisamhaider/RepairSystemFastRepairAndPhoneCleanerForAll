package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.DeviceRooted;

import java.util.List;

public class RootCheckerAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_checker);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView tvRooted = findViewById(R.id.tv_rooted);
        if (DeviceRooted.isRooted()) {
            tvRooted.setText("Device is rooted ");
        } else {
            tvRooted.setText("Device is not rooted ");
        }
//        Toast.makeText(this, "Device is rooted "+DeviceRooted.isRooted(), Toast.LENGTH_SHORT).show();
    }
}