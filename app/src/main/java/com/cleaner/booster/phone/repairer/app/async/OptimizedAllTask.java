package com.cleaner.booster.phone.repairer.app.async;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.adapters.AllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class OptimizedAllTask extends AsyncTask<Void, Integer, String> {

    @SuppressLint("StaticFieldLeak")
    Context context;
    @SuppressLint("StaticFieldLeak")
    private Utils utils;
    private LoadingDialog loadingDialog;
    ActivityManager activityManager;

    public OptimizedAllTask(Context context) {
        this.context = context;
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context,"Optimizing");
    }

    @Override
    protected void onPreExecute() {
        activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        for (int i = 0; i < utils.getSystemActiveApps().size(); i++) {
            activityManager.killBackgroundProcesses(utils.getSystemActiveApps().get(i));
            publishProgress(i);
        }
         return null;
    }

    @Override
    protected void onPostExecute(String s) {
         loadingDialog.dismiss();
    }

}
