package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.adapters.FastChargeAllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.database.Db;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class FastAllAppsTask  {

    Context context;
    private FastChargeAllAppsAdapter appsAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> list;
    Utils utils;
    LoadingDialog loadingDialog;
    Db db ;

    public FastAllAppsTask(Context context, FastChargeAllAppsAdapter appsAdapter,
                           RecyclerView recyclerView,Db db) {
        this.context = context;
        this.appsAdapter = appsAdapter;
        this.recyclerView = recyclerView;
        this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);
        this.db = db;
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
                recyclerView.setLayoutManager(linearLayoutManager);
                appsAdapter.setList(list,db);
                recyclerView.setAdapter(appsAdapter);
                appsAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
            }
        },2500);


    }

}
