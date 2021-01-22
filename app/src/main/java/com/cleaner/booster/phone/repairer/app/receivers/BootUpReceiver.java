package com.cleaner.booster.phone.repairer.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.cleaner.booster.phone.repairer.app.services.SmartChargeService;

public class BootUpReceiver extends BroadcastReceiver {

    private static final String TAG = "BootUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        /***** For start Service  ****/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(new Intent(context, SmartChargeService.class));
        }else{
            context.startService(new Intent(context, SmartChargeService.class));
        }
    }   

}