package com.cleaner.booster.phone.repairer.app.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.activities.UnInstallAppAct;
import com.cleaner.booster.phone.repairer.app.adapters.AllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AllAppsTask  {

    @SuppressLint("StaticFieldLeak")
    Context context;
    private AllAppsAdapter allAppsAdapter;
    @SuppressLint("StaticFieldLeak")
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> list;
    private Utils utils;
    private LoadingDialog loadingDialog;


    public AllAppsTask(Context context, AllAppsAdapter allAppsAdapter,
                       RecyclerView recyclerView) {
        this.context = context;
        this.allAppsAdapter = allAppsAdapter;
        this.recyclerView = recyclerView;
        this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);
        linearLayoutManager = new LinearLayoutManager(context);
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        list = utils.GetAllInstalledApkInfo();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                allAppsAdapter.setList(list);
                recyclerView.setAdapter(allAppsAdapter);
                allAppsAdapter.notifyDataSetChanged();
                if (context instanceof UnInstallAppAct){
                    ((UnInstallAppAct)context).startAnimation();
                }
                loadingDialog.dismiss();
            }
        },2500);
    }

}
