package com.cleaner.booster.phone.repairer.app.async;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.cleaner.booster.phone.repairer.app.activities.UnInstallAppAct;
import com.cleaner.booster.phone.repairer.app.adapters.AllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


public class AllAppsTask extends AsyncTask<Void, Integer, String> {

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
    }

    @Override
    protected void onPreExecute() {
        linearLayoutManager = new LinearLayoutManager(context);
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        list = utils.GetAllInstalledApkInfo();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
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

}
