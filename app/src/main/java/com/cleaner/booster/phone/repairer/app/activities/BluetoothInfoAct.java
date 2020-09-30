package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
 import android.widget.TextView;
import android.widget.Toast;

import com.cleaner.booster.phone.repairer.app.R;

public class BluetoothInfoAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        TextView blueOff_tv,blueName_tv,blueAddress_tv,blueMode_tv,blueDiscovery_tv;
        blueOff_tv = findViewById(R.id.blueOff_tv);
        blueName_tv = findViewById(R.id.blueName_tv);
        blueAddress_tv = findViewById(R.id.blueAddress_tv);
        blueMode_tv = findViewById(R.id.blueMode_tv);
        blueDiscovery_tv = findViewById(R.id.blueDiscovery_tv);


        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter==null)
        {
            Toast.makeText(this, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        }
        else

        {

            if (bluetoothAdapter.getState()==BluetoothAdapter.STATE_OFF)
            {
                blueOff_tv.setText("off");
            }
            else if (bluetoothAdapter.getState()==BluetoothAdapter.STATE_ON){

                blueOff_tv.setText("on");
            }

            blueName_tv.setText(bluetoothAdapter.getName());
            blueAddress_tv.setText(bluetoothAdapter.getAddress());
            blueMode_tv.setText(String.valueOf(bluetoothAdapter.getScanMode()));

            if (bluetoothAdapter.isDiscovering())
            {
                blueDiscovery_tv.setText("on");
            }else {
            blueDiscovery_tv.setText("off");
        }}
    }
}