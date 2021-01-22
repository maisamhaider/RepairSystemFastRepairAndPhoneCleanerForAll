package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.DeviceRooted;

public class RootCheckerAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_checker);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView tvRooted = findViewById(R.id.tv_rooted);
        if (DeviceRooted.isRooted()) {
            tvRooted.setText("Your device is rooted ");
        } else {
            tvRooted.setText("Your device is not rooted ");
        }
//        Toast.makeText(this, "Device is rooted "+DeviceRooted.isRooted(), Toast.LENGTH_SHORT).show();
    }
}