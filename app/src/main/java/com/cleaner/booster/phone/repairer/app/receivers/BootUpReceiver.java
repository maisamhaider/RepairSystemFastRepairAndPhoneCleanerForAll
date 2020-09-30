package com.cleaner.booster.phone.repairer.app.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.cleaner.booster.phone.repairer.app.services.SmartChargeService;

public class BootUpReceiver extends BroadcastReceiver {

    private static final String TAG = "BootUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        /****** For Start Activity *****/
//        Intent i = new Intent(context, MyActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(i);


        Log.w(TAG, "onReceive: Boot complete" );

        // TODO add prefence
        /***** For start Service  ****/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(new Intent(context, SmartChargeService.class));
        }else{
            context.startService(new Intent(context, SmartChargeService.class));
        }
    }   

}