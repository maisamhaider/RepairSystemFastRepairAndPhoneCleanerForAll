package com.cleaner.booster.phone.repairer.app.receivers;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.cleaner.booster.phone.repairer.app.activities.ChargingLockedScreenAct;

import static android.content.Intent.ACTION_SCREEN_ON;


public class FastChargingChargerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        boolean onScreenOn = intent.getAction().equals(ACTION_SCREEN_ON);


        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        int batterySource = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);

        Intent i = new Intent(context, ChargingLockedScreenAct.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (batterySource == BatteryManager.BATTERY_PLUGGED_AC) {
            if (myKM != null && myKM.inKeyguardRestrictedInputMode()) {
                context.startActivity(i);
            } else if (level == 100) {
                context.startActivity(i);
            }

        } else if (batterySource == BatteryManager.BATTERY_PLUGGED_USB) {
            if (myKM != null && myKM.inKeyguardRestrictedInputMode() ) {
                context.startActivity(i);
            } else if (level == 100) {
                context.startActivity(i);
            }

        } else if (batterySource == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            if (myKM != null && myKM.inKeyguardRestrictedInputMode()) {
                context.startActivity(i);
            } else if (level == 100) {
                context.startActivity(i);
            }
        }
    }
}