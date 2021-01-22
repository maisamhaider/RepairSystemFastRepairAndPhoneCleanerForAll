package com.cleaner.booster.phone.repairer.app.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.receivers.FastChargingChargerReceiver;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SmartChargeService extends Service {
    public static final String TAG = "CustomService";
    private Context context;
    private SharedPreferences preferences;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName,
                NotificationManager.IMPORTANCE_MIN);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                    createNotificationChannel(notificationManager) : "Phone Repair";
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Phone Repair")
                    .setContentText("Apps service run to perform function correctly.")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Apps service run to perform function correctly"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentInfo(" ")
                    .setCategory(NotificationCompat.CATEGORY_SERVICE).build();
            startForeground(100, notification);
        }


        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        FastChargingChargerReceiver fastChargingChargerReceiver = new FastChargingChargerReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        IntentFilter intentChargingFilter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        getApplicationContext().registerReceiver(fastChargingChargerReceiver, filter);
        getApplicationContext().registerReceiver(fastChargingChargerReceiver, intentFilter);
        getApplicationContext().registerReceiver(fastChargingChargerReceiver, intentChargingFilter);



        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                if ((preferences.getLong("lock_charge_delay", Calendar.getInstance().getTimeInMillis())+6_000)==Calendar.getInstance().getTimeInMillis()) {
                    getApplicationContext().registerReceiver(fastChargingChargerReceiver, filter);
                    getApplicationContext().registerReceiver(fastChargingChargerReceiver, intentFilter);
                    getApplicationContext().registerReceiver(fastChargingChargerReceiver, intentChargingFilter);
                }
               if (preferences.getBoolean("first_lock_charge",true)) {
                    getApplicationContext().registerReceiver(fastChargingChargerReceiver, filter);
                    getApplicationContext().registerReceiver(fastChargingChargerReceiver, intentFilter);
                    getApplicationContext().registerReceiver(fastChargingChargerReceiver, intentChargingFilter);
                }

            }
        },4_000,1_000);
        return START_STICKY;
    }




    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android
        AlarmManager alarmService = (AlarmManager)
                getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 100, restartServicePI);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

}
