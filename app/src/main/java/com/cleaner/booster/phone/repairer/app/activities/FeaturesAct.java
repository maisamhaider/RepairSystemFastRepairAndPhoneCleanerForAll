package com.cleaner.booster.phone.repairer.app.activities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cleaner.booster.phone.repairer.app.R;

import java.util.ArrayList;

public class FeaturesAct extends AppCompatActivity {
    TextView wifiDirect_tv2, bluetooth_tv2, bluetoothLE_tv2,
            cameraFlash_tv2, cameraFront_tv, microphone_tv2, nFC_tv2, multitouch_tv2, consumerIR_tv2, printing_tv2, gSM_tv2, fingerprint_tv2,
            appWidgets_tv2, sIP_tv2, sIPBasedVOIP_tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wifiDirect_tv2 = findViewById(R.id.wifiDirect_tv2);
        bluetooth_tv2 = findViewById(R.id.bluetooth_tv2);
        bluetoothLE_tv2 = findViewById(R.id.bluetoothLE_tv2);
        cameraFlash_tv2 = findViewById(R.id.cameraFlash_tv2);
        cameraFront_tv = findViewById(R.id.cameraFront_tv);
        microphone_tv2 = findViewById(R.id.microphone_tv2);
        nFC_tv2 = findViewById(R.id.nFC_tv2);
        multitouch_tv2 = findViewById(R.id.multitouch_tv2);
        consumerIR_tv2 = findViewById(R.id.consumerIR_tv2);
        printing_tv2 = findViewById(R.id.printing_tv2);
        gSM_tv2 = findViewById(R.id.gSM_tv2);
        fingerprint_tv2 = findViewById(R.id.fingerprint_tv2);
        appWidgets_tv2 = findViewById(R.id.appWidgets_tv2);
        sIP_tv2 = findViewById(R.id.sIP_tv2);
        sIPBasedVOIP_tv2 = findViewById(R.id.sIPBasedVOIP_tv2);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {

        }
        isWifiDirectSupported(this);


    }

    @SuppressLint("SetTextI18n")
    private void isWifiDirectSupported(Context ctx) {
        int i = 0;
        PackageManager pm = ctx.getPackageManager();
        FeatureInfo[] features = pm.getSystemAvailableFeatures();

        ArrayList<String> fArrayList = new ArrayList<>();

        for (FeatureInfo info : features) {
            fArrayList.add(info.name);
        }

        if (fArrayList.contains("android.hardware.wifi.direct")) {
            wifiDirect_tv2.setText("Available");
        } else {
            wifiDirect_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.bluetooth")) {
            bluetooth_tv2.setText("Available");
        } else {
            bluetooth_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.microphone")) {
            microphone_tv2.setText("Available");
        } else {
            microphone_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.camera.flash")) {
            cameraFlash_tv2.setText("Available");
        } else {
            cameraFlash_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.camera.front")) {
            cameraFront_tv.setText("Available");
        } else {
            cameraFront_tv.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.touchscreen.multitouch.distinct")) {
            multitouch_tv2.setText("Available");
        } else {
            multitouch_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.nfc")) {
            nFC_tv2.setText("Available");
        } else {
            nFC_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.bluetooth_le")) {
            bluetoothLE_tv2.setText("Available");
        } else {
            bluetoothLE_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.software.print")) {
            printing_tv2.setText("Available");
        } else {
            printing_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.telephony.gsm")) {
            gSM_tv2.setText("Available");
        } else {
            gSM_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.fingerprint")) {
            fingerprint_tv2.setText("Available");
        } else {
            fingerprint_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.hardware.consumerir")) {
            consumerIR_tv2.setText("Available");
        } else {
            consumerIR_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.software.app_widgets")) {
            appWidgets_tv2.setText("Available");
        } else {
            appWidgets_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.software.sip")) {
            sIP_tv2.setText("Available");
        } else {
            sIP_tv2.setText("Not Available");
        }
        if (fArrayList.contains("android.software.sip.voip")) {
            sIPBasedVOIP_tv2.setText("Available");
        } else {
            sIPBasedVOIP_tv2.setText("Not Available");
        }
    }
}